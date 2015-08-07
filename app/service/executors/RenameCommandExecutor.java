package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;
import service.fs.FsExecutor;
import service.fs.PathEncoder;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RenameCommandExecutor implements CommandExecutor {

	@Inject
	private PathEncoder pathEncoder;
	@Inject
	private FsExecutor fsExecutor;

	@Override
	public Object execute(DynamicForm dynamicForm) throws IOException {
		String target = dynamicForm.get("target");
		String name = dynamicForm.get("name");

        Map<String, Object> result = new HashMap<>();

		PathEncoder.FsItem fsItem = pathEncoder.getPathByHash(target);
		Path path = fsItem.path;

        Path renamed = Files.move(path, path.getParent().resolve(name));

        result.put("added", Collections.singletonList(fsExecutor.getEx(fsItem.volumeId, renamed)));
        result.put("removed", Collections.singletonList(target));

        return result;
	}
}
