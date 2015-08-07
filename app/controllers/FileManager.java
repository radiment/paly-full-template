package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import service.CommandExecutor;
import service.CommandExecutorFactory;
import service.DownloadCommandExecutor;
import views.html.cms.admin.*;

import javax.inject.Inject;
import java.io.IOException;

import static play.libs.Json.toJson;

public class FileManager extends Controller {

    @Inject
    CommandExecutorFactory cmdExecutorFactory;

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result control() throws IOException {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        CommandExecutor cmd = cmdExecutorFactory.get(dynamicForm.get("cmd"));
        if (cmd instanceof DownloadCommandExecutor) {
            return ok(((DownloadCommandExecutor) cmd).execute(dynamicForm));
        } else {
            return ok(toJson(cmd.execute(dynamicForm)));
        }
    }

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result fileManager() throws IOException {
        return ok(imageManager.render());
    }

}
