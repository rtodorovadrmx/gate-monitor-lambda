package gate.monitor.dagger;

import dagger.Component;
import jakarta.validation.Validator;

import javax.inject.Singleton;

@Singleton
@Component(modules = GateMonitorModule.class)
public interface DependenciesComponent {

   public Validator getValidator();

}
