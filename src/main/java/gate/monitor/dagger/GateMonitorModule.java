package gate.monitor.dagger;

import dagger.Module;
import dagger.Provides;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.inject.Singleton;

@Module
public class GateMonitorModule {

    @Provides
    @Singleton
    Validator provideValidator() {
        try (ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()){
            return validatorFactory.getValidator();
        }
    }
}
