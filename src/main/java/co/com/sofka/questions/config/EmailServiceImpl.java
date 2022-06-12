package co.com.sofka.questions.config;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

@Component
public class EmailServiceImpl {


    private static final String MESSAGE = "One question has been update click to see it. http://localhost:4200/question/";
    private static final String SUBJECT = "Someone has answer your question";
    private final JavaMailSender emailSender;
    Logger log = Logger.getLogger("EmailService");

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public static String getDefaultTemplate() {
        try {
            return Files.readString(Path.of("src/main/resources/templates/emailTemplate.html"),
                    Charset.defaultCharset());
        } catch (IOException e) {
            return "";
        }
    }

    public void sendSimpleMessage(String to) {

        SimpleMailMessage message = new SimpleMailMessage();

        try {
            message.setFrom("angularjavatest@gmail.com");
            message.setTo(to);
            message.setSubject(SUBJECT);
            message.setText(MESSAGE);
            emailSender.send(message);
            log.info(message.toString());

        } catch (MailException e) {
            log.info("error in sending mail, detail: " + e.getMessage());
        }
    }

    public void sendHTMLMessage(String to, String questionId) {
        MimeMessage msg = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom("angularjavatest@gmail.com");
            helper.setTo(to);
            helper.setSubject(SUBJECT);
            String template = getDefaultTemplate();
            template = template.replace("{text}", MESSAGE+questionId);
            helper.setText(MESSAGE, template);
            emailSender.send(msg);
            log.info(msg.toString());

        } catch (MailException | MessagingException e) {
            log.info("error in sending mail, detail: " + e.getMessage());
        }
    }
}