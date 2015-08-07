package controllers;

import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.annotation.JsonFormat;
import models.SecurityRole;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.users.permissions;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class UserManagement extends Controller {

    public static final String USER_ROLE = "user";
    public static final String ADMIN_ROLE = "admin";
    public static final String CMS_ADMIN_ROLE = "cms_admin";
    public static final String TESTER_ROLE = "tester";

    @Restrict(@Group(ADMIN_ROLE))
    public Result putRoleToUser(Long userId, String roleName) {
        User user = User.find.byId(userId);
        if (!user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName))) {
            SecurityRole role = SecurityRole.findByRoleName(roleName);
            user.getRoles().add(role);
            user.save();
        }
        return ok();
    }

    public static List<String> getRoles() {
        return Arrays.asList(USER_ROLE, ADMIN_ROLE, TESTER_ROLE, CMS_ADMIN_ROLE);
    }

    @Restrict(@Group(ADMIN_ROLE))
    public Result getRolesList() {
        return ok(toJson(getRoles()));
    }

    @Restrict(@Group(ADMIN_ROLE))
    public Result removeRole(Long userId, String roleName) {
        User user = User.find.byId(userId);
        ListIterator<? extends Role> iterator = user.getRoles().listIterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            if (role.getName().equals(roleName)) {
                iterator.remove();
                user.save();
                return noContent();
            }
        }
        return notFound();
    }

    @Restrict(@Group(ADMIN_ROLE))
    public Result getUsers() {
        List<User> dbUsers = User.find.where().eq("active", true).findList();
        List<ArxanUser> users = dbUsers.stream().map(ArxanUser::new).collect(Collectors.toList());
        return ok(toJson(users));
    }

    @Restrict(@Group(ADMIN_ROLE))
    public Result manageUserPermissions() {
        final User localUser = Application.getLocalUser(session());
        return ok(permissions.render(localUser));
    }

    public class ArxanUser {
        public Long id;

        public String email;

        public String name;

        public String firstName;

        public String lastName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
        public Date lastLogin;

        public List<String> roles;

        public ArxanUser(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.name = user.getName();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.lastLogin = user.getLastLogin();
            roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        }
    }
}
