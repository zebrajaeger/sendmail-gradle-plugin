package de.zebrajaeger.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * the plugin itself
 */
public abstract class SendMailPlugin implements Plugin<Project> {

    public static final String ID = "sendMail";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(ID, SendMailExtension.class);
        project.getTasks().register(ID, SendMailTask.class);
    }
}
