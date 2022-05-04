package com.hellokaton.lattice;

import org.junit.Test;

/**
 * @author biezhi
 * @date 2018/6/4
 */
public class Example {

    @Test
    public void testUseLattice() {
        Lattice lattice = new Lattice();
        lattice.loginUrl("/login").unauthorizedUrl("/unauthorized");

        LoginToken loginToken = new LoginToken();
        lattice.login(loginToken);

    }

}
