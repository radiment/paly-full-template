package service.executors;

import play.data.DynamicForm;
import play.mvc.Http;
import service.CommandExecutor;
import service.DownloadCommandExecutor;
import service.fs.PathEncoder;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileCommandExecutor implements CommandExecutor, DownloadCommandExecutor {

    @Inject
    private PathEncoder pathEncoder;

	@Override
	public File execute(DynamicForm dynamicForm) throws IOException {
		String target = dynamicForm.get("target");
		boolean download = "1".equals(dynamicForm.get("download"));

        PathEncoder.FsItem fsItem = pathEncoder.getPathByHash(target);

        Http.Response response = Http.Context.current().response();

        response.setContentType(Files.probeContentType(fsItem.path));

        if (download) {
            response.setContentType("application/x-download");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-disposition", "attachment; filename=" + fsItem.path.getFileName().toString());
        }

        return fsItem.path.toFile();
	}
}
