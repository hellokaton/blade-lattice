# lattice

## Usage

**config**

```java
Lattice lattice = new Lattice();
lattice.loginUrl("/login").unauthorizedUrl("/unauthorized");
```

**enable lattice**

```java
Blade.me().use(new LatticeMiddleware());
```

**login**

```java
@Inject
private Lattice lattice;

@PostRoute("login")
@JSON
public RestResponse doLogin(){
    LoginToken token = new LoginToken();
    token.setUsername();
    token.setPassword();
    lattice.login(token);
    return RestResponse.ok(); 
}
```

**define realm**

```java
@Bean
public class MyRealm implements LatticeRealm<User> {

    @Override
    public AuthInfo<User> doAuthenticate(LoginToken loginToken) {
        // Query DB or other
        AuthInfo<User> authenticateInfo = new AuthInfo<>();
        authenticateInfo.setUser(new User());
        authenticateInfo.setUsername(loginToken.getUsername());
        return authenticateInfo;
    }

    @Override
    public void doAuthorize(AuthInfo<User> authInfo) {
        // Query DB
        
    }

}
```

## License

[MIT](LICENSE)