package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;
import service.fs.FsExecutor;
import service.fs.FsItemEx;
import service.fs.PathEncoder;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static service.UtilException.rethrowConsumer;

public class OpenCommandExecutor implements CommandExecutor {

    @Inject
    private PathEncoder pathEncoder;
    @Inject
    private FsExecutor fsExecutor;

    @Override
    public Object execute(DynamicForm dynamicForm) throws IOException {

        boolean init = dynamicForm.get("init") != null;
        boolean tree = dynamicForm.get("tree") != null;
        String target = dynamicForm.get("target");

        Map<String, Object> result = new HashMap<>();

        if (init) {
            result.put("api", 2.1);
            result.put("netDrivers", new Object[0]);
        }

        PathEncoder.FsItem cwd = pathEncoder.getPathByHash(target);

        Map<String, FsItemEx> files = new HashMap<>();

        if (tree) {
            pathEncoder.getVolumes().forEach(rethrowConsumer(fsItem -> fsExecutor.addSubFolders(files, fsItem)));
        }

        if (cwd == null) {
            List<PathEncoder.FsItem> volumes = pathEncoder.getVolumes();
            cwd = volumes.get(0);
        }

        fsExecutor.addChildren(files, cwd);

        result.put("files", files.values());

		result.put("cwd", fsExecutor.getEx(cwd));

        return result;
    }

	/*@Override
    public void execute(FsService fsService, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		boolean init = request.getParameter("init") != null;
		boolean tree = request.getParameter("tree") != null;
		String target = request.getParameter("target");

		Map<String, FsItemEx> files = new LinkedHashMap<String, FsItemEx>();
		if (init)
		{
			json.put("api", 2.1);
			json.put("netDrivers", new Object[0]);
		}

		if (tree)
		{
			for (FsVolume v : fsService.getVolumes())
			{
				FsItemEx root = new FsItemEx(v.getRoot(), fsService);
				files.put(root.getHash(), root);
				addSubfolders(files, root);
			}
		}

		FsItemEx cwd = findCwd(fsService, target);
		files.put(cwd.getHash(), cwd);
		addChildren(files, cwd);

		json.put("files", files2JsonArray(request, files.values()));
		json.put("cwd", getFsItemInfo(request, cwd));
		json.put("options", getOptions(request, cwd));
	}*/
}
