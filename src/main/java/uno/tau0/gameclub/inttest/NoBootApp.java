package uno.tau0.gameclub.inttest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NoBootApp {
    static void main() {
        ApplicationContext context = new AnnotationConfigApplicationContext(NoBootConfiguration.class);
        IntCarrier number = (IntCarrier) context.getBean("answer");
        IO.println(number.getVal());
        IntUser user1 = context.getBean("niceUser", IntUser.class);
        IntUser user2 = context.getBean("modernUser", IntUser.class);
        IntUser user3 = context.getBean("answerUser", IntUser.class);
        IntUser user4 = context.getBean("intUser", IntUser.class);

        IO.println(user1.getVal() + "... " +  user1.getReaction());
        IO.println(user2.getVal() + "... " +  user2.getReaction());
        IO.println(user3.getVal() + "... " +  user3.getReaction());
        IO.println(user4.getVal() + "... " +  user4.getReaction());
    }
}
