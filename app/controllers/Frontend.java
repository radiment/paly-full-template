package controllers;

import models.CMSPage;
import play.mvc.Controller;
import play.mvc.Result;
import service.fs.PathEncoder;
import views.html.cms.cms;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;

public class Frontend extends Controller {

    @Inject
    private PathEncoder pathEncoder;

    public Result show(String pageName) {
        CMSPage page = CMSPage.find.byId(pageName);
        return ok(cms.render(page));
    }

    public Result file(String hash) throws IOException {
        PathEncoder.FsItem fsItem = pathEncoder.getPathByHash(hash);
        response().setContentType(Files.probeContentType(fsItem.path));
        return ok(fsItem.path.toFile());
    }

}
