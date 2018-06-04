package io.github.biezhi.lattice;

import com.blade.mvc.WebContext;
import com.blade.mvc.http.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author biezhi
 * @date 2018/6/4
 */
@Slf4j
public class Lattice {

    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;
    private String sessionKey = "LATTICE";

    private Set<String> excludeUrls = new HashSet<>();

    private AuthInfo authInfo;

    private LatticeRealm latticeRealm;

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

    public Lattice excludes(String... urls) {
        if (null != urls && urls.length > 0) {
            this.excludeUrls.addAll(Arrays.asList(urls));
        }
        return this;
    }

    public void login(LoginToken loginToken) {
        this.authInfo = latticeRealm.doAuthenticate(loginToken);

        Session session = WebContext.request().session();
        session.attribute(this.sessionKey, this.authInfo);

        latticeRealm.doAuthorize(this.authInfo);

    }

    public Object loginUser() {
        return WebContext.request().session().attribute(this.sessionKey);
    }

}