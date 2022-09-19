# Gradle sendMail plugin

## Usage

To be able to load the plugin, modify (or create)  the `settings.gradle` file:

```gradle
pluginManagement {
    repositories {
        mavenCentral()
    }
}
```

### Simple

We have to
- load the plugin
- set the config

Create a `build.gradle` file with the following content
and replace the `smtpServer.*` settings with your mail-host settings.  
Also the `mail.from` and `mail.to` needs a replacement:

```gradle
plugins {
    id 'sendMail' version("0.0.1-SNAPSHOT")
}

sendMail {
    smtpServer {
        host "smtp.foobar.de"
        port 587
        user "mailHostLogin"
        password "mailHostPassword"
    }
    mail {
        from "foo@bar.de"
        to "narf@pinky-and-brain.de"
        attachments layout.projectDirectory.file("build.gradle"),
                    layout.projectDirectory.file("settings.gradle"),
        body "Hello!!"
    }
}
```

### Hidden config

The configuration is pretty similar to the simple version.  
The main difference is, that we keep the credentials outside.


In the `~/.gradle/` directory modify or create file `gradle.properties`
and add following properties:

    mailHost=smtp.foobar.de
    mailPort=587
    mailUser=mailHostLogin
    mailPassword=mailHostPassword

The `build.gradle` file is similar to the simple config, except, 
that we provide the credentials via variables instead literals:  
```gradle
plugins {
    id 'sendMail' version("0.0.1-SNAPSHOT")
}

sendMail {
    smtpServer {
        host "${mailHost}"
        port "${mailPort}"
        user "${mailUser}"
        password "${mailPassword}"
    }
    mail {
        from "foo@bar.de"
        to "narf@pinky-and-brain.de"
        attachments layout.projectDirectory.file("build.gradle"),
                    layout.projectDirectory.file("settings.gradle"),
        body "Hello!!"
    }
}
```

## Send the mail

Into the project directory call

    ./gradlew sendMail

or - if you want to see more information -

    ./gradlew --info sendMail
