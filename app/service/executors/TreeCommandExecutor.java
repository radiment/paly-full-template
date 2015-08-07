package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;
import service.fs.FsExecutor;
import service.fs.FsItemEx;
import service.fs.PathEncoder;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TreeCommandExecutor implements CommandExecutor {

    @Inject
    private PathEncoder pathEncoder;
    @Inject
    private FsExecutor fsExecutor;

    @Override
    public Object execute(DynamicForm dynamicForm) throws IOException {
        String target = dynamicForm.get("target");

        Map<String, Object> result = new HashMap<>();
        PathEncoder.FsItem cwd = pathEncoder.getPathByHash(target);
        Map<String, FsItemEx> files = new HashMap<>();
        fsExecutor.addSubFolders(files, cwd);

        result.put("tree", files.values());
        return result;
    }
}
