package de.zebrajaeger;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public abstract class SendMailPlugin implements Plugin<Project> {

    public static final String ID = "sendMail";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(ID, SendMailExtension.class);
        project.getTasks().register(ID, SendMailTask.class);
    }
}
