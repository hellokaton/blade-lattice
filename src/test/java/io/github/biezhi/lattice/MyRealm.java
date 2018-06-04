package io.github.biezhi.lattice;

/**
 * @author biezhi
 * @date 2018/6/4
 */
public class MyRealm implements LatticeRealm<User> {

    @Override
    public AuthInfo<User> doAuthenticate(LoginToken loginToken) {
        AuthInfo<User> authenticateInfo = new AuthInfo<>();
        authenticateInfo.setUser(new User());
        authenticateInfo.setUsername(loginToken.getUsername());

        return authenticateInfo;
    }

    @Override
    public void doAuthorize(AuthInfo<User> authInfo) {

    }

}
