package uno.tau0.gameclub.inttest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan("uno.tau0.gameclub.inttest")
public class NoBootConfiguration {
    @Bean
    public IntCarrier answer() {
        return new IntCarrier(42);
    }

    @Bean
    public IntCarrier nice() {
        return new IntCarrier(69);
    }

    @Bean
    public IntCarrier modern() {
        return new IntCarrier(67);
    }

    @Bean
    public IntCarrier random() {
        return new IntCarrier(5);
    }

    @Bean
    public IntUser niceUser() {
        return new IntUser(nice(), funnyNumberRegistry());
    }

    @Bean
    public IntUser modernUser() {
        return new IntUser(modern(), funnyNumberRegistry());
    }

    @Bean
    public IntUser answerUser() {
        return new IntUser(answer(), funnyNumberRegistry());
    }

    @Bean
    public FunnyNumberRegistry funnyNumberRegistry() {
        var fng = new FunnyNumberRegistry("Not funny numba");

        fng.addFunnyNumber(69, "Nice");
        fng.addFunnyNumber(42, "Which number?!?");
        fng.addFunnyNumber(67, "Six seven!! Six Seven!! \uD83E\uDD37");

        return fng;
    }
}
