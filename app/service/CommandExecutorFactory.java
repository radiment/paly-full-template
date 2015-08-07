package service;

import com.google.inject.ImplementedBy;

@ImplementedBy(ElFinderCommandExecutorFactory.class)
public interface CommandExecutorFactory {

    CommandExecutor get(String command);
}
