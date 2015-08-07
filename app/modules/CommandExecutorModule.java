package modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import play.Configuration;
import play.Environment;
import service.CommandExecutor;
import service.executors.OpenCommandExecutor;

public class CommandExecutorModule extends AbstractModule {

    private final Environment environment;
    private final Configuration configuration;

    public CommandExecutorModule(Environment environment, Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bind(CommandExecutor.class)
                .annotatedWith(Names.named("open"))
                .to(OpenCommandExecutor.class);

        // Expect configuration like:
        // hello.en = "myapp.EnglishHello"
        // hello.de = "myapp.GermanHello"
        Configuration executorConf = configuration.getConfig("command.executor");

        // Iterate through all the languages and bind the
        // class associated with that language. Use Play's
        // ClassLoader to load the classes.
        for (String key: executorConf.subKeys()) {
            try {
                String bindingClassName = executorConf.getString(key);
                Class<? extends CommandExecutor> bindingClass =
                        environment.classLoader().loadClass(bindingClassName)
                                .asSubclass(CommandExecutor.class);
                bind(CommandExecutor.class)
                        .annotatedWith(Names.named(key))
                        .to(bindingClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
