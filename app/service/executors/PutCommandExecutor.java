package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;

public class PutCommandExecutor implements CommandExecutor {
	@Override
	public Object execute(DynamicForm dynamicForm) {

		String target = dynamicForm.get("target");
		/*FsItemEx fsi = super.findItem(fsService, target);
		OutputStream os = fsi.openOutputStream();
		IOUtils.write(request.getParameter("content"), os, "utf-8");
		os.close();
		json.put("changed", new Object[] { super.getFsItemInfo(request, fsi) });*/
		return null;
	}
}
