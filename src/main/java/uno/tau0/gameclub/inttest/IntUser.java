package uno.tau0.gameclub.inttest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntUser {
    private IntCarrier random;
    private FunnyNumberRegistry funnyNumberRegistry;

    IntUser(IntCarrier random, FunnyNumberRegistry funnyNumberRegistry) {
        this.random = random;
        this.funnyNumberRegistry = funnyNumberRegistry;
    }

    public int getVal() {
        return random.getVal();
    }

    public String getReaction() {
        return funnyNumberRegistry.getMessage(random.getVal());
    }
}
