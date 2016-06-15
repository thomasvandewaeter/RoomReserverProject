package managedbeans;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

@Named(value = "mailTimer")
@ApplicationScoped
public class MailTimer {
    @Resource
    TimerService timerService;

    public MailTimer() {
        long duration = 20000;
        Timer timer
                = timerService.createSingleActionTimer(duration, new TimerConfig());
    }

    @Timeout
    public void Timeout(Timer timer) {
        System.out.println("Timerbean: timeout occured!");
    }

}
