package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreateDataOnStartup {
    @Autowired
    GroupRespository groupRespository;

    @EventListener(ApplicationReadyEvent.class)
    public void createData() {
        if (!groupRespository.existsById(1L)) {
            groupRespository.save(new Group("default"));
        }
    }
}
