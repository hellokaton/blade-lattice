package io.github.biezhi.lattice;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.WebContext;
import com.blade.mvc.http.Response;
import com.blade.mvc.http.Session;
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

    private Consumer<Response> authenticateFailAction = response -> response.redirect(this.loginUrl());
    private Consumer<Response> authorizeFailAction    = response -> response.unauthorized().redirect(this.unauthorizedUrl());

    private Set<String> excludeUrls = new HashSet<>();

    @Inject
    private LatticeRealm<?> realm;

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

    public boolean onAuthenticateFail(Response response) {
        this.authenticateFailAction.accept(response);
        return false;
    }

    public boolean onAuthorizeFail(Response response) {
        this.authorizeFailAction.accept(response);
        return false;
    }

    public Lattice excludes(String... urls) {
        if (null != urls && urls.length > 0) {
            this.excludeUrls.addAll(Arrays.asList(urls));
        }
        return this;
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