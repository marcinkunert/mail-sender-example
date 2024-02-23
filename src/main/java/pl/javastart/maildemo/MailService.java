package pl.javastart.maildemo;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {
    
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired JavaMailSenderImpl mailSender;

    public MailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String receiver, String subject, String content) {

        MimeMessage msg = new MimeMessage(session);

        Context ctx = new Context();
        ctx.setVariable("");

        String html = templateEngine.process("basic-email.html", ctx);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setFrom("elonmusk@x.com");
        simpleMailMessage.setText(content);

        mailSender.send(msg);
    }

    void asd(SimpleMailMessage simpleMailMessage) {
        Session session = mailSender.getSession();

        Store store = null;
        try {
            store = session.getStore("imap");
            store.connect("imap.yandex.ru", 993, "FROM_ACC", "FROM_PASS");
            Folder folder = store.getFolder("Sent");
            folder.open(Folder.READ_WRITE);
            simpleMailMessage.setFlag(Flags.Flag.SEEN, true);
            folder.appendMessages(new Message[]{simpleMailMessage});
            store.close();
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
}
