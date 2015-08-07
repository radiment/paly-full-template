package service.executors;

import play.data.DynamicForm;
import play.mvc.Http;
import service.CommandExecutor;
import service.fs.FsExecutor;
import service.fs.FsItemEx;
import service.fs.PathEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class UploadCommandExecutor implements CommandExecutor {

	@Inject
	private PathEncoder pathEncoder;
    @Inject
    private FsExecutor fsExecutor;

	@Override
	public Object execute(DynamicForm dynamicForm) throws IOException {

        String target = dynamicForm.get("target");
        Map<String, Object> result = new HashMap<>();

        List<FsItemEx> added = new ArrayList<>();

        PathEncoder.FsItem dst = pathEncoder.getPathByHash(target);

        Http.MultipartFormData body = Http.Context.current().request().body().asMultipartFormData();
        for (Http.MultipartFormData.FilePart filePart : body.getFiles()) {
            Path path = filePart.getFile().toPath();
            Path resultPath = Files.move(path, dst.path.resolve(path.getFileName()));
            added.add(fsExecutor.getEx(dst.volumeId, resultPath));
        }

        result.put("added", added);

		return result;
	}

}
