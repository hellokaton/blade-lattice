package io.github.biezhi.lattice;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.WebContext;
import com.blade.mvc.hook.Signature;
import com.blade.mvc.hook.WebHook;
import io.github.biezhi.lattice.annotation.Logical;
import io.github.biezhi.lattice.annotation.Permissions;
import io.github.biezhi.lattice.annotation.Roles;
import io.github.biezhi.lattice.annotation.Users;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * LatticeMiddleware
 *
 * @author biezhi
 * @date 2018/6/4
 */
@Slf4j
public class LatticeMiddleware implements WebHook {

    @Inject
    private Lattice lattice;

    @Override
    public boolean before(Signature signature) {

//        Lattice lattice = WebContext.blade().ioc().getBean(Lattice.class);

        Method   action     = signature.getAction();
        Class<?> controller = action.getDeclaringClass();

        Users users = action.getAnnotation(Users.class);
        if (null == users) {
            users = controller.getAnnotation(Users.class);
        }
        if (null != users && null == lattice.loginUser()) {
            return lattice.onAuthenticateFail(signature.response());
        }

        Roles roles = action.getAnnotation(Roles.class);
        if (null == roles) {
            roles = controller.getAnnotation(Roles.class);
        }
        if (null != roles) {
            if (null == lattice.loginUser()) {
                return lattice.onAuthenticateFail(signature.response());
            }
            if (!this.checkRoles(lattice.authInfo(), roles)) {
                return lattice.onAuthorizeFail(signature.response());
            }
        }

        Permissions permissions = action.getAnnotation(Permissions.class);
        if (null == permissions) {
            permissions = controller.getAnnotation(Permissions.class);
        }

        if (null != permissions) {
            if (null == lattice.loginUser()) {
                return lattice.onAuthenticateFail(signature.response());
            }
            if (!this.checkPermissions(lattice.authInfo(), permissions)) {
                return lattice.onAuthorizeFail(signature.response());
            }
        }

        return true;
    }

    private boolean checkRoles(AuthInfo authInfo, Roles roles) {
        Set<String> authInfoRoles = authInfo.getRoles();
        String[]    roleArray     = roles.value();

        return arrayContains(authInfoRoles, roleArray, roles.logical());
    }

    private boolean checkPermissions(AuthInfo authInfo, Permissions permissions) {
        Set<String> authInfoPermissions = authInfo.getPermissions();
        String[]    permissionArray     = permissions.value();
        return arrayContains(authInfoPermissions, permissionArray, permissions.logical());
    }

    private boolean arrayContains(Set<String> sets, String[] array, Logical logical) {
        if (logical == Logical.AND) {
            return sets.containsAll(Arrays.asList(array));
        } else if (logical == Logical.OR) {
            for (String s : array) {
                if (sets.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

}
