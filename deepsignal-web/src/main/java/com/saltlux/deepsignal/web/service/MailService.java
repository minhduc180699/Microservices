package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.InquiryAnswerEmail;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.service.dto.AccountDTO;
import com.saltlux.deepsignal.web.service.dto.MailParamsDTO;
import com.saltlux.deepsignal.web.service.template.MailContentBuilder;
import io.netty.util.internal.StringUtil;
import org.hibernate.validator.internal.util.StringHelper;
import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String VERIFY_CODE = "verifyCode";

    private static final String DATE = "date";

    private static final String DAYSHARE = "dayShare";

    private static final String LINK = "link";

    private static final String MESSAGE = "message";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private ApplicationProperties applicationProperties;

    @Autowired
    private RMapCache<String, String> mapEmailCode;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private UserRepository userRepository;

    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    public RMapCache<String, String> getMapEmailCode() {
        return mapEmailCode;
    }

    public void setMapEmailCode(RMapCache<String, String> mapEmailCode) {
        this.mapEmailCode = mapEmailCode;
    }

    public Session buildSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", username);
        properties.put("mail.password", password);

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        return session;
    }

    @Async
    public void sendMultipartEmail(List<String> to, String subject, String content) {
        Session session = buildSession();
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(jHipsterProperties.getMail().getFrom(), "Deepsignal"));
            for (String s : to) {
                InternetAddress toAddress = new InternetAddress(s);
                message.addRecipient(Message.RecipientType.TO, toAddress);
            }
            message.setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            MimeBodyPart imagePart = new MimeBodyPart();
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("images/deepsignal_logo_mail.png");
            ByteArrayDataSource byteArrayResource = new ByteArrayDataSource(inputStream, "image/png");
            imagePart.setDataHandler(new DataHandler(byteArrayResource));
            imagePart.setHeader("Content-ID", "<image-avatar>");
            imagePart.setDescription(MimeBodyPart.INLINE);

            multipart.addBodyPart(imagePart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to); //email address
            message.setFrom(new InternetAddress(jHipsterProperties.getMail().getFrom(), "Deepsignal"));
            message.setSubject(subject); // title
            message.setText(content, isHtml); //content
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey, String code) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey() == null ? Constants.DEFAULT_LANGUAGE : user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        if (!StringUtil.isNullOrEmpty(code)) {
            context.setVariable(VERIFY_CODE, code);
        }
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendMultipartEmailWithTemplate(User user, String template, String titleKey, MailParamsDTO mailParamsDTO) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey() == null ? Constants.DEFAULT_LANGUAGE : user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        if (!StringUtil.isNullOrEmpty(mailParamsDTO.getConfirmCode())) {
            context.setVariable(VERIFY_CODE, mailParamsDTO.getConfirmCode());
        }
        if (!StringUtil.isNullOrEmpty(mailParamsDTO.getDay())) {
            context.setVariable(DATE, mailParamsDTO.getDay());
        }
        if (!StringUtil.isNullOrEmpty(mailParamsDTO.getMessage())) {
            context.setVariable(MESSAGE, mailParamsDTO.getMessage());
            context.setVariable(LINK, mailParamsDTO.getLinkShare());
            context.setVariable(DAYSHARE, mailParamsDTO.getDayShare());
        }

        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(template, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        if (mailParamsDTO.getEmails() != null) {
            List<String> stringList = mailParamsDTO.getEmails();
            stringList.add(user.getEmail());
            sendMultipartEmail(stringList, subject, content);
        } else {
            List<String> stringList = new ArrayList<>();
            stringList.add(user.getEmail());
            sendMultipartEmail(stringList, subject, content);
        }
    }

    @Async
    public void sendActivationEmail(User user, String codeConfirm) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        MailParamsDTO mailParamsDTO = new MailParamsDTO();
        mailParamsDTO.setConfirmCode(codeConfirm);
        sendMultipartEmailWithTemplate(user, "mail/mailVerification", "email.verification.title", mailParamsDTO);
    }

    @Async
    public void sendEmailNoticeActivation(User user) {
        log.debug("Sending activationNotice email to '{}'", user.getEmail());
        Date date = new Date();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy_MM_dd");
        String day = sdfDay.format(date);
        MailParamsDTO mailParamsDTO = new MailParamsDTO();
        mailParamsDTO.setDay(day);
        sendMultipartEmailWithTemplate(user, "mail/mailWelcome", "email.notice.title", mailParamsDTO);
    }

    @Async
    public void sendEmailSharePost(User user, MailParamsDTO mailParamsDTO) {
        sendMultipartEmailWithTemplate(user, "mail/mailSharePost", "email.share.title", mailParamsDTO);
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title", null);
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title", null);
    }

    @Async
    public void sendMail(InquiryAnswerEmail inquiryAnswerEmail) {
        try {
            MimeMessagePreparator preparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(
                    mimeMessage,
                    !StringHelper.isNullOrEmptyString(inquiryAnswerEmail.getFileAnswer()) ||
                    !StringHelper.isNullOrEmptyString(inquiryAnswerEmail.getFileQuestion())
                );
                messageHelper.setTo(InternetAddress.parse(inquiryAnswerEmail.getEmail()));
                messageHelper.setSubject(inquiryAnswerEmail.getTitle());
                String content = mailContentBuilder.build(inquiryAnswerEmail);
                messageHelper.setText(content, true);

                // add the file attachment
                if (!StringHelper.isNullOrEmptyString(inquiryAnswerEmail.getFileAnswer())) {
                    File fileAnswer = new File(properties.getFilesUpload().getLocation() + inquiryAnswerEmail.getFileAnswer());
                    FileSystemResource frAnswer = new FileSystemResource(fileAnswer);
                    messageHelper.addAttachment(frAnswer.getFilename(), frAnswer);
                }

                if (!StringHelper.isNullOrEmptyString(inquiryAnswerEmail.getFileQuestion())) {
                    File fileQuestion = new File(properties.getFilesUpload().getLocation() + inquiryAnswerEmail.getFileQuestion());
                    FileSystemResource frQuestion = new FileSystemResource(fileQuestion);
                    messageHelper.addAttachment(frQuestion.getFilename(), frQuestion);
                }
            };
            javaMailSender.send(preparator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean mapEmailCode(String email, String code) {
        if (code == null || code == "") {
            return false;
        }
        try {
            mapEmailCode.put(email, code, 5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCodeEmail(AccountDTO accountDTO) {
        if (accountDTO.getEmailCode().equals("") || accountDTO.getEmailCode() == null) {
            return false;
        }
        if (accountDTO.getEmailCode().equals(mapEmailCode.get(accountDTO.getEmail()))) {
            //            Optional<User> optionalUser = userRepository.findOneByLogin(accountDTO.getPhoneNumber());
            //            User user = optionalUser.get();
            //            user.setEmail(accountDTO.getEmail());
            //            try {
            //                userRepository.save(user);
            //            } catch (Exception e) {
            //                return false;
            //            }
            mapEmailCode.remove(accountDTO.getEmail());
            return true;
        } else {
            return false;
        }
    }

    public String codeConfirm() {
        StringBuilder codeConfirm = new StringBuilder();
        for (int i = 0; i < properties.getTwilio().getLengthCode(); i++) {
            int random = ThreadLocalRandom.current().nextInt(0, 10);
            codeConfirm.append(random);
        }
        return codeConfirm.toString();
    }
}
