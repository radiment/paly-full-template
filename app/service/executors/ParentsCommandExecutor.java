package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;

public class ParentsCommandExecutor implements CommandExecutor {
    @Override
    public Object execute(DynamicForm dynamicForm) {
        String target = dynamicForm.get("target");
        return new Object();
    }
    /*@Override
    public void execute(FsService fsService, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		String target = request.getParameter("target");

		Map<String, FsItemEx> files = new HashMap<String, FsItemEx>();
		FsItemEx fsi = findItem(fsService, target);
		while (!fsi.isRoot())
		{
			super.addSubfolders(files, fsi);
			fsi = fsi.getParent();
		}

		json.put("tree", files2JsonArray(request, files.values()));
	}*/
}
