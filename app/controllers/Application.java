package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.User;
import play.Routes;
import play.data.Form;
import play.mvc.*;
import play.mvc.Http.Session;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import providers.MyUsernamePasswordAuthProvider.MySignup;

import views.html.*;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.feth.play.module.pa.user.AuthUser;

public class Application extends Controller {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";

    public Result index() {
        return ok(index.render());
    }

    public static User getLocalUser(final Session session) {
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session);
        return User.findByAuthUserIdentity(currentAuthUser);
    }

    @Restrict(@Group(UserManagement.USER_ROLE))
    public Result restricted() {
        final User localUser = Application.getLocalUser(session());
        return ok(restricted.render(localUser));
    }

    @Restrict(@Group(UserManagement.USER_ROLE))
    public Result downloads() {
        final User localUser = Application.getLocalUser(session());
        return ok(downdloads.render());
    }

    @Restrict(@Group(UserManagement.USER_ROLE))
    public Result profile() {
        final User localUser = Application.getLocalUser(session());
        return ok(profile.render(localUser));
    }

    public Result login() {
        return ok(login.render(MyUsernamePasswordAuthProvider.LOGIN_FORM));
    }

    public Result doLogin() {
        Authenticate.noCache(response());
        final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not fill everything properly
            return badRequest(login.render(filledForm));
        } else {
            // Everything was filled
            return UsernamePasswordAuthProvider.handleLogin(ctx());
        }
    }

    public Result signup() {
        return ok(signup.render(MyUsernamePasswordAuthProvider.SIGNUP_FORM));
    }

    public Result jsRoutes() {
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        controllers.routes.javascript.Signup.forgotPassword()))
                .as("text/javascript");
    }

    public Result doSignup() {
        Authenticate.noCache(response());
        final Form<MySignup> filledForm = MyUsernamePasswordAuthProvider.SIGNUP_FORM
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not fill everything properly
            return badRequest(signup.render(filledForm));
        } else {
            // Everything was filled
            // do something with your part of the form before handling the user
            // signup
            return UsernamePasswordAuthProvider.handleSignup(ctx());
        }
    }

    public static String formatTimestamp(final long t) {
        return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
    }

}