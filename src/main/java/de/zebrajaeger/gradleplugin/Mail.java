package de.zebrajaeger.gradleplugin;


import java.io.File;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.tasks.Nested;
import org.gradle.tooling.exceptions.UnsupportedBuildArgumentException;

/**
 * The mail specific part of the plugin configuration.
 */
@Data
public class Mail {

  public Mail() {
  }

  private String from;
  private String to;
  private String subject;
  private String body;
  private List<File> attachments = new LinkedList<>();
  private Boolean failOnMissingAttachment = true;

  @SuppressWarnings("unused")
  public Mail from(String from) {
    this.from = from;
    return this;
  }

  @SuppressWarnings("unused")
  public Mail to(String to) {
    this.to = to;
    return this;
  }

  @SuppressWarnings("unused")
  public Mail subject(String subject) {
    this.subject = subject;
    return this;
  }

  @SuppressWarnings("unused")
  public Mail body(String body) {
    this.body = body;
    return this;
  }

  @SuppressWarnings("unused")
  public Mail attachments(Object... os) {
    List<String> msg = new LinkedList<>();

    attachments.clear();
    for (Object o : os) {
      if (String.class.equals(o.getClass())) {
        attachments.add(new File(o.toString()));
      } else if (File.class.equals(o.getClass())) {
        attachments.add((File) o);
      } else if (o instanceof FileSystemLocation) {
        attachments.add(((FileSystemLocation) o).getAsFile());
      } else {
        msg.add(o.getClass() + " is not supported and will be ignored: " + o);
      }
    }

    if (!msg.isEmpty()) {
      throw new UnsupportedBuildArgumentException("Error (attachments): " + String.join("; ", msg));
    }

    return this;
  }

  @SuppressWarnings("unused")
  public Mail failOnMissingAttachment(Boolean failOnMissingAttachment) {
    this.failOnMissingAttachment = failOnMissingAttachment;
    return this;
  }

  @SuppressWarnings("unused")
  public Mail failOnMissingAttachment(String failOnMissingAttachment) {
    this.failOnMissingAttachment = Boolean.parseBoolean(failOnMissingAttachment);
    return this;
  }
}
