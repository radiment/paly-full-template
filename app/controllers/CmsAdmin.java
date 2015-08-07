package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.CMSPage;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.cms.admin.*;

import java.util.List;

public class CmsAdmin extends Controller {

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result updatePage(String pageName) {
        Form<CMSPage> pageForm = Form.form(CMSPage.class).bindFromRequest();
        if (pageForm.hasErrors()) {
            return badRequest(pageForm.errorsAsJson());
        }
        CMSPage page = CMSPage.find.byId(pageName);
        if (page == null) {
            return notFound();
        }
        CMSPage cmsPage = pageForm.get();
        page.setBody(cmsPage.getBody());
        page.setTitle(cmsPage.getTitle());
        page.save();
        response().setHeader("location", routes.Frontend.show(page.getName()).url());
        return noContent();
    }

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result deletePage(String pageName) {
        CMSPage cmsPage = CMSPage.find.byId(pageName);
        if (cmsPage == null) {
            return notFound();
        }
        cmsPage.delete();
        return noContent();
    }

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result index() {
        List<CMSPage> pages = CMSPage.find.all();
        return ok(index.render(pages));
    }

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result editPage(String pageName) {
        CMSPage page = CMSPage.find.byId(pageName);
        return ok(edit.render(page, false));
    }

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result newPage() {
        CMSPage page = new CMSPage();
        page.setTitle("New Page");
        return ok(edit.render(page, true));
    }

    @Restrict(@Group(UserManagement.CMS_ADMIN_ROLE))
    public Result savePage() {
        Form<CMSPage> pageForm = Form.form(CMSPage.class).bindFromRequest();
        if (pageForm.hasErrors()) {
            return badRequest(pageForm.errorsAsJson());
        }
        CMSPage cmsPage = pageForm.get();
        cmsPage.save();
        response().setHeader("location", routes.Frontend.show(cmsPage.getName()).url());
        return noContent();
    }

}
