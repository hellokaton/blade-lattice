# lattice

## 核心组件

- **Lattice**: 管理统一配置
- **Realm**: 用户认证和授权

## 使用

**启用 Lattice 中间件**

```java
Blade.me().use(new LatticeMiddleware());
```

**配置 Lattice 并托管到 IOC**

```java
@Bean
public class Bootstrap implements BeanProcessor {

    @Override
    public void preHandle(Blade blade) {
        Lattice lattice = new Lattice();
        lattice.loginUrl("/login").unauthorizedUrl("/unauthorized");

        lattice.realm(new JdbcRealm());

        blade.register(lattice);
    }

    @Override
    public void processor(Blade blade) {

        blade.templateEngine(new JetbrickTemplateEngine());

        // JDBC
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(blade.environment().getOrNull("jdbc.url"));
        dataSource.setUsername(blade.environment().getOrNull("jdbc.username"));
        dataSource.setPassword(blade.environment().getOrNull("jdbc.password"));

        Anima.open(dataSource);
    }

}
```

**JDBC Realm 实现**

```java
@Bean
public class JdbcRealm implements LatticeRealm<SysUser> {

    @Inject
    private UserService userService;

    @Override
    public AuthInfo<SysUser> doAuthenticate(LoginToken loginToken) {
        AuthInfo<SysUser> authInfo = new AuthInfo<>();

        SysUser sysUser = select().from(SysUser.class).where(SysUser::getUsername, loginToken.getUsername()).one();
        if (null == sysUser) {
            throw new UnknownAccountException();
        }
        String pwd = EncryptKit.md5(loginToken.getUsername() + loginToken.getPassword());
        if (!sysUser.getPassword().equals(pwd)) {
            throw new AuthenticationException();
        }
        if (UserStatus.DISABLE.getStatus().equals(sysUser.getStatus())) {
            throw new DisabledAccountException();
        }
        authInfo.setUser(sysUser);
        authInfo.setUsername(sysUser.getUsername());
        return authInfo;
    }

    @Override
    public void doAuthorize(AuthInfo<SysUser> authInfo) {
        Long userId = authInfo.getUser().getUserId();

        Set<String> roleSet = userService.findUserRoles(userId).stream()
                .map(SysRole::getRoleSign).collect(Collectors.toSet());
        authInfo.setRoles(roleSet);

        Set<String> permissions = userService.findUserPerms(userId);
        authInfo.setPermissions(permissions);
    }

}
```

**Lattice 登录**

```java
@Inject
private Lattice lattice;

@PostRoute("login")
public RestResponse doLogin(LoginToken loginToken) {
    try {
        lattice.login(loginToken);
        return RestResponse.ok();
    } catch (UnknownAccountException e) {
        return RestResponse.fail("找不到该用户");
    } catch (AuthenticationException e) {
        return RestResponse.fail("用户名或密码错误");
    } catch (DisabledAccountException e) {
        return RestResponse.fail("该用户已经被禁用");
    } catch (Exception e) {
        log.error("登录出现异常", e);
        return RestResponse.fail("登录发生错误");
    }
}
```

## License

[MIT](LICENSE)