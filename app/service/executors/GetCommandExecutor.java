package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;

public class GetCommandExecutor implements CommandExecutor {
	/*@Override
	public void execute(FsService fsService, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		String target = request.getParameter("target");

		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();
		String content = IOUtils.toString(is, "utf-8");
		is.close();
		json.put("content", content);
	}*/

	@Override
	public Object execute(DynamicForm dynamicForm) {
		String target = dynamicForm.get("target");

		return target;
	}
}
