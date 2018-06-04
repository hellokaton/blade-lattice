package io.github.biezhi.lattice;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.hook.Signature;
import com.blade.mvc.hook.WebHook;
import io.github.biezhi.lattice.annotation.Permissions;
import io.github.biezhi.lattice.annotation.Roles;
import io.github.biezhi.lattice.annotation.Users;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author biezhi
 * @date 2018/6/4
 */
@Slf4j
public class LatticeMiddleware implements WebHook {

    @Inject
    private Lattice lattice;

    public boolean before(Signature signature) {
        Method   action     = signature.getAction();
        Class<?> controller = action.getDeclaringClass();

        Users users = action.getAnnotation(Users.class);
        if (null == users) {
            users = controller.getAnnotation(Users.class);
        }
        if (null != users && null == lattice.loginUser()) {
            signature.response().redirect(lattice.loginUrl());
            return false;
        }

        Roles roles = action.getAnnotation(Roles.class);
        if (null == roles) {
            roles = controller.getAnnotation(Roles.class);
        }
        if (null != roles) {
            if (null == lattice.loginUser()) {
                signature.response().redirect(lattice.loginUrl());
                return false;
            }
            // TODO check role
            this.checkRoles(roles);
        }

        Permissions permissions = action.getAnnotation(Permissions.class);
        if (null == permissions) {
            permissions = controller.getAnnotation(Permissions.class);
        }

        if (null != permissions) {
            if (null == lattice.loginUser()) {
                signature.response().redirect(lattice.loginUrl());
                return false;
            }
            // TODO check permissions
            this.checkPermissions(permissions);
        }
        return true;
    }

    private void checkRoles(Roles roles) {

    }

    private void checkPermissions(Permissions permissions) {

    }

}
