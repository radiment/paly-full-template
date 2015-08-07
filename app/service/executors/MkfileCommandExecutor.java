package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;

public class MkfileCommandExecutor implements CommandExecutor {

    @Override
    public Object execute(DynamicForm dynamicForm) {
        String target = dynamicForm.get("target");
        String name = dynamicForm.get("name");

		/*FsItemEx fsi = super.findItem(fsService, target);
        FsItemEx dir = new FsItemEx(fsi, name);
		dir.createFile();
		json.put("added", new Object[] { getFsItemInfo(request, dir) });*/
        return new Object();
    }
}
