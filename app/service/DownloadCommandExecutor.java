package service;

import play.data.DynamicForm;

import java.io.File;
import java.io.IOException;

public interface DownloadCommandExecutor {
    File execute(DynamicForm dynamicForm) throws IOException;
}
