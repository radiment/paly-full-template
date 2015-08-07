import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import controllers.UserManagement;
import controllers.routes;
import models.SecurityRole;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.mvc.Call;
import providers.MyStupidBasicAuthProvider;
import service.CommandExecutorFactory;
import service.ElFinderCommandExecutorFactory;

import java.util.List;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        PlayAuthenticate.setResolver(new Resolver() {

            @Override
            public Call login() {
                // Your login page
                return routes.Application.login();
            }

            @Override
            public Call afterAuth() {
                // The user will be redirected to this page after authentication
                // if no original URL was saved
                return routes.Application.index();
            }

            @Override
            public Call afterLogout() {
                return routes.Application.index();
            }

            @Override
            public Call auth(final String provider) {
                // You can provide your own authentication implementation,
                // however the default should be sufficient for most cases
                return routes.Authenticate.authenticate(provider);
            }

            @Override
            public Call askMerge() {
                return routes.Account.askMerge();
            }

            @Override
            public Call askLink() {
                return routes.Account.askLink();
            }

            @Override
            public Call onException(final AuthException e) {
                if (e instanceof AccessDeniedException) {
                    return routes.Signup
                            .oAuthDenied(((AccessDeniedException) e)
                                    .getProviderKey());
                }

                // more custom problem handling here...
                return super.onException(e);
            }
        });

        initialData();
    }

    private void initialData() {
        List<String> roles = UserManagement.getRoles();
        if (SecurityRole.find.findRowCount() != roles.size()) {
            for (final String roleName : roles) {
                SecurityRole existRole = SecurityRole.findByRoleName(roleName);
                if (existRole != null) continue;
                final SecurityRole role = new SecurityRole();
                role.setRoleName(roleName);
                role.save();
            }
        }
        if (User.findByAuthUserIdentity(MyStupidBasicAuthProvider.AUTH_USER) == null) {
            User user = User.create(MyStupidBasicAuthProvider.AUTH_USER);
            SecurityRole adminRole = SecurityRole.findByRoleName(UserManagement.ADMIN_ROLE);
            user.getRoles().add(adminRole);
            user.save();
        }
    }
}