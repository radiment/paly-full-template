package service.executors;

import play.data.DynamicForm;
import play.data.Form;
import service.CommandExecutor;
import service.UtilException;
import service.fs.FsExecutor;
import service.fs.FsItemEx;
import service.fs.PathEncoder;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PasteCommandExecutor implements CommandExecutor {

    @Inject
    private PathEncoder pathEncoder;
    @Inject
    private FsExecutor fsExecutor;

	@Override
	public Object execute(DynamicForm dynamicForm) {
        Form<PasteCommand> pasteCommandForm = Form.form(PasteCommand.class).bindFromRequest();

        PasteCommand pasteCommand = pasteCommandForm.get();

        Map<String, Object> result = new HashMap<>();

        PathEncoder.FsItem dst = pathEncoder.getPathByHash(pasteCommand.dst);

        List<PathEncoder.FsItem> moveFiles = pasteCommand.targets.stream().map(pathEncoder::getPathByHash)
                .collect(Collectors.toList());

        List<FsItemEx> added;
        if (pasteCommand.cut == 1) {
            added = fsExecutor.move(moveFiles, dst);
            result.put("removed", pasteCommand.targets);
        } else {
            added = fsExecutor.copy(moveFiles, dst);
        }


        result.put("added", added);

		return result;
	}

    public static class PasteCommand {
        public String src;
        public String dst;
        public int cut;
        public List<String> targets;
    }

}
