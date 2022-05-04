package com.hellokaton.lattice;

/**
 * @author biezhi
 * @date 2018/6/4
 */
public interface LatticeRealm<T> {

    AuthInfo<T> doAuthenticate(LoginToken loginToken);

    void doAuthorize(AuthInfo<T> authInfo);

}
