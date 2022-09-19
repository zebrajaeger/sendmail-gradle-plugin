package de.zebrajaeger;

import groovy.lang.Closure;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.gradle.tooling.exceptions.UnsupportedBuildArgumentException;
import org.gradle.util.internal.ConfigureUtil;
import org.slf4j.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class SendMailExtension {
    private final SmtpServer smtpServer = new SmtpServer();
    private final Mail mail = new Mail();

    Boolean infoLogging = true;

    @SuppressWarnings("unused")
    void smtpServer(Closure<?> config) {
        ConfigureUtil.configure(config, smtpServer);
    }

    @SuppressWarnings("unused")
    void mail(Closure<?> config) {
        ConfigureUtil.configure(config, mail);
    }

    @SuppressWarnings("unused")
    public SendMailExtension infoLogging(Boolean infoLogging) {
        this.infoLogging = infoLogging;
        return this;
    }

    @SuppressWarnings("unused")
    public SendMailExtension infoLogging(String infoLogging) {
        this.infoLogging = Boolean.parseBoolean(infoLogging);
        return this;
    }

    public void validate(final Logger log) {
        List<String> messages = new LinkedList<>();

        // server
        if (StringUtils.isBlank(smtpServer.getHost())) {
            final String msg = "smtpServer.host is mandatory";
            log.error(msg);
            messages.add(msg);
        }
        if (smtpServer.getPort() == null) {
            final String msg = "smtpServer.port is mandatory";
            log.error(msg);
            messages.add(msg);
        }

        // mail
        if (StringUtils.isBlank(mail.getFrom())) {
            final String msg = "mail.from is mandatory";
            log.error(msg);
            messages.add(msg);
        }
        if (StringUtils.isBlank(mail.getTo())) {
            final String msg = "mail.to is mandatory";
            log.error(msg);
            messages.add(msg);
        }
        if (mail.failOnMissingAttachment == null) {
            final String msg = "mail.failOnMissingAttachment is null but mandatory";
            log.error(msg);
            messages.add(msg);
        }else{
            if (mail.failOnMissingAttachment) {
                for(File f : mail.attachments){
                    if(!f.exists()){
                        final String msg = String.format("mail.attachments file does not exist: '%s'", f);
                        log.error(msg);
                        messages.add(msg);
                    }
                }
            }
        }

        if (!messages.isEmpty()) {
            throw new UnsupportedBuildArgumentException(String.join("; ", messages));
        }
    }
}
