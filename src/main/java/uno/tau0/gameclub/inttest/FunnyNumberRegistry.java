package uno.tau0.gameclub.inttest;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FunnyNumberRegistry {
    private HashMap<Number, String> funnySet = new HashMap<>();
    private String defaultMessage;
    private int numbersCalled = 0;

    FunnyNumberRegistry(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }


    /// Maybe... factory !!???
    public void addFunnyNumber(Number number, String message) {
        funnySet.put(number, message);
    }

    public String getMessage(Number number) {
        numbersCalled++;
        return funnySet.getOrDefault(number, defaultMessage) + " (Called " + numbersCalled + " times)";
    }
}
