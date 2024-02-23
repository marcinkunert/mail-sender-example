package pl.javastart.maildemo;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    JavaMailSenderImpl mailSender;

    public MailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(String receiver, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setFrom("elonmusk@x.com");
        simpleMailMessage.setText(content);
        mailSender.send(simpleMailMessage);
    }


    public void sendHtmlMessageAndSaveToSentFolder(String receiver, String username) {
        try {
            Context ctx = new Context();
            ctx.setVariable("username", username);
            String emailContent = templateEngine.process("activation-email.html", ctx);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            message.setTo(receiver);
            message.setFrom("elon@x.com", "Elon Musk");
            message.setSubject("Aktywacja konta");
//            message.setReplyTo(replyTo);
            message.setText(emailContent, true);
            mailSender.send(mimeMessage);
//            saveToSentFolder(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveToSentFolder(MimeMessage mimeMessage) {
        try {
            Session session = mailSender.getSession();
            Store store = session.getStore("imap");
            String host = "imap.mailservice.com";
            String username = "username";
            String password = "pasword";

            store.connect(host, username, password);

            Folder sentFolder = store.getFolder("Sent");
            sentFolder.open(Folder.READ_WRITE);
            mimeMessage.setFlag(Flags.Flag.SEEN, true);
            sentFolder.appendMessages(new Message[]{mimeMessage});
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
