package de.zebrajaeger.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

/**
 * The plugin itself.
 */
public abstract class SendMailPlugin implements Plugin<Project> {

  public static final String ID = "sendMail";

  @Override
  public void apply(Project project) {
    final SendMailExtension extension = project.getExtensions().create(ID, SendMailExtension.class);
    final TaskProvider<SendMailTask> task = project.getTasks().register(ID, SendMailTask.class);
    task.get().dependsOn(extension.getDependsOn().toArray());
  }
}
