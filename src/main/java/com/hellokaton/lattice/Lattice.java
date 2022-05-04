package com.hellokaton.lattice;

import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.mvc.RouteContext;
import com.hellokaton.blade.mvc.WebContext;
import com.hellokaton.blade.mvc.http.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author biezhi
 * @date 2018/6/4
 */
@Slf4j
public class Lattice {

    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;
    private String sessionKey = Constant.DEFAULT_SESSION_KEY;

    private Consumer<RouteContext> authenticateFailAction = ctx -> ctx.redirect(this.loginUrl());
    private Consumer<RouteContext> authorizeFailAction = ctx -> ctx.response().unauthorized().redirect(this.unauthorizedUrl());

    private Set<String> excludeUrls = new HashSet<>();

    @Inject
    private LatticeRealm<?> realm;

    public Lattice() {
        this.excludeUrls.add("/static/");
        this.excludeUrls.add("/");
        this.excludeUrls.add("/index");
        this.excludeUrls.add("/login");
    }

    public Lattice realm(LatticeRealm latticeRealm) {
        this.realm = latticeRealm;
        return this;
    }

    public Lattice loginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    public Lattice successUrl(String successUrl) {
        this.successUrl = successUrl;
        return this;
    }

    public Lattice unauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
        return this;
    }

    public String loginUrl() {
        return this.loginUrl;
    }

    public String successUrl() {
        return this.successUrl;
    }

    public String unauthorizedUrl() {
        return this.unauthorizedUrl;
    }

    public boolean onAuthenticateFail(RouteContext ctx) {
        this.authenticateFailAction.accept(ctx);
        return false;
    }

    public boolean onAuthorizeFail(RouteContext ctx) {
        this.authorizeFailAction.accept(ctx);
        return false;
    }

    public Lattice excludes(String... urls) {
        if (null != urls && urls.length > 0) {
            this.excludeUrls.addAll(Arrays.asList(urls));
        }
        return this;
    }

    public Set<String> excludeUrls() {
        return this.excludeUrls;
    }

    public void login(LoginToken loginToken) {
        AuthInfo authInfo = realm.doAuthenticate(loginToken);

        Session session = WebContext.request().session();
        session.attribute(this.sessionKey, authInfo);

        realm.doAuthorize(authInfo);
    }

    public AuthInfo authInfo() {
        return WebContext.request().session().attribute(this.sessionKey);
    }

    public Object loginUser() {
        return Optional.ofNullable(authInfo()).map(AuthInfo::getUser).orElse(null);
    }

}