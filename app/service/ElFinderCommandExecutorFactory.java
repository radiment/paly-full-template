package service;

import com.google.inject.name.Names;
import play.api.inject.BindingKey;
import play.api.inject.QualifierInstance;
import play.inject.Injector;
import scala.Option;

import javax.inject.Inject;

public class ElFinderCommandExecutorFactory implements CommandExecutorFactory {

    @Inject
    Injector injector;

    @Override
    public CommandExecutor get(String command) {
        return injector.instanceOf(new BindingKey<>(CommandExecutor.class,
                Option.apply(new QualifierInstance<>(Names.named(command)))));
    }
}
