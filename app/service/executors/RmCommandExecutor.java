package service.executors;

import play.data.DynamicForm;
import play.data.Form;
import service.CommandExecutor;
import service.fs.PathEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class RmCommandExecutor implements CommandExecutor {

	@Inject
	private PathEncoder pathEncoder;

	@Override
	public Object execute(DynamicForm dynamicForm) throws IOException {
		Map<String, Object> result = new HashMap<>();
		List<String> removed = new ArrayList<>();

		RmCommand rmCommand = Form.form(RmCommand.class).bindFromRequest().get();

		for (String target : rmCommand.targets) {
			if (!pathEncoder.isVolume(target)) {
				PathEncoder.FsItem file = pathEncoder.getPathByHash(target);
				Files.delete(file.path);
				removed.add(target);
			}
		}

		result.put("removed", removed);

		return result;
	}

	public static class RmCommand {
		public List<String> targets;
	}
}
