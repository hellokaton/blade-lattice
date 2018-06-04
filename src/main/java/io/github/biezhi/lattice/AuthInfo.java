package io.github.biezhi.lattice;

import lombok.Data;

import java.util.Set;

/**
 * @author biezhi
 * @date 2018/6/4
 */
@Data
public class AuthInfo<T> {

    private String username;

    private T user;

    private Set<String> roles;
    private Set<String> permissions;

}
