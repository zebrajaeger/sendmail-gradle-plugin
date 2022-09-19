package de.zebrajaeger;

import jakarta.activation.FileDataSource;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.tooling.BuildException;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class SendMailTask extends DefaultTask {

    public static final Logger LOG = LoggerFactory.getLogger(SendMailTask.class);

    @TaskAction
    public void resolveLatestVersion() {
        final SendMailExtension extension = (SendMailExtension) getProject().getExtensions().findByName(SendMailPlugin.ID);
        if (extension == null) {
            throw new BuildException("Extension is null", null);
        }
        sendMail(extension);
    }

    private void sendMail(SendMailExtension extension) {
        extension.validate(LOG);

        if (StringUtils.isBlank(extension.getMail().getSubject())) {
            final String subject = "Mail send from " + getProject().getName();
            LOG.info(String.format("change empty subject to '%s'", subject));
            extension.getMail().subject(subject);
        }

        if (StringUtils.isBlank(extension.getMail().getBody())) {
            extension.getMail().body("");
        }

        final SmtpServer smtp = extension.getSmtpServer();
        final Mail mail = extension.getMail();

        LOG.info("config: {}", extension);

        Mailer mailer = MailerBuilder
                .withSMTPServer(
                        smtp.getHost(),
                        smtp.getPort(),
                        smtp.getUser(),
                        smtp.getPassword())
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(extension.getInfoLogging())
                .buildMailer();

        final EmailPopulatingBuilder mailBuilder = EmailBuilder.startingBlank()
                .from(mail.getFrom())
                .to(mail.getTo())
                .withSubject(mail.getSubject())
                .withPlainText(mail.getBody());

        for (File f : mail.getAttachments()) {
            mailBuilder.withAttachment(f.getName(), new FileDataSource(f));
        }

        mailer.sendMail(mailBuilder.buildEmail());
        LOG.info("Mail send");
    }
}
