package service;

import play.data.DynamicForm;

import java.io.IOException;

public interface CommandExecutor {
    Object execute(DynamicForm dynamicForm) throws IOException;
}
