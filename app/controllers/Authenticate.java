package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.controllers.AuthenticateBase;
import play.mvc.Result;

public class Authenticate extends AuthenticateBase {

    public Result authenticate(final String provider) {
        noCache(response());
        final String payload = request().getQueryString(PAYLOAD_KEY);
        return PlayAuthenticate.handleAuthentication(provider, ctx(), payload);
    }

    public Result logout() {
        noCache(response());
        return PlayAuthenticate.logout(session());
    }
}
