package pl.javastart.maildemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MailDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(MailDemoApplication.class, args);
        MailService mailService = ctx.getBean(MailService.class);
        mailService.sendEmail("odbiorca@wp.pl", "Darmowa Tesla", "Wygrałeś darmową teslę");
    }

}
