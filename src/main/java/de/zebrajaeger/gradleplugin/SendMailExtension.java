package de.zebrajaeger.gradleplugin;

import groovy.lang.Closure;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.gradle.tooling.exceptions.UnsupportedBuildArgumentException;
import org.gradle.util.internal.ConfigureUtil;
import org.slf4j.Logger;

/**
 * Plugin configuration.
 */
@Getter
@ToString
public class SendMailExtension {

  private final SmtpServer smtpServer = new SmtpServer();
  private final Mail mail = new Mail();
  private Boolean infoLogging = true;
  private final List<String> dependsOn = new LinkedList<>();

  @SuppressWarnings("unused")
  public void smtpServer(Closure<?> config) {
    ConfigureUtil.configure(config, smtpServer);
  }

  @SuppressWarnings("unused")
  public void mail(Closure<?> config) {
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

  @SuppressWarnings("unused")
  public SendMailExtension dependsOn(String dependsOn) {
    this.dependsOn.clear();
    this.dependsOn.add(dependsOn);
    return this;
  }

  @SuppressWarnings("unused")
  public SendMailExtension dependsOn(List<String> dependsOn) {
    this.dependsOn.clear();
    this.dependsOn.addAll(dependsOn);
    return this;
  }

  @SuppressWarnings("unused")
  public SendMailExtension dependsOn(String... dependsOn) {
    this.dependsOn.clear();
    this.dependsOn.addAll(Arrays.stream(dependsOn).collect(Collectors.toList()));
    return this;
  }

  /**
   * Validate the configuration.
   *
   * @param log The logger to send error messages.
   */
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
    if (smtpServer.getTls() == null) {
      final String msg = "smtpServer.tls is mandatory";
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
    if (mail.getFailOnMissingAttachment() == null) {
      final String msg = "mail.failOnMissingAttachment is null but mandatory";
      log.error(msg);
      messages.add(msg);
    } else {
      if (mail.getFailOnMissingAttachment()) {
        for (File f : mail.getAttachments()) {
          if (!f.exists()) {
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
