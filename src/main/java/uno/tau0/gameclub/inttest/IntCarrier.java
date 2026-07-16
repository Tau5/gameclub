package uno.tau0.gameclub.inttest;

import lombok.Getter;

public class IntCarrier {
    @Getter
    private Integer val;

    IntCarrier(Integer val) {
        this.val = val;
    }
}
