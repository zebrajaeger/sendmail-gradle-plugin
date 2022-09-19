package de.zebrajaeger;

import lombok.Data;
import lombok.ToString;

@Data
class SmtpServer {
    public SmtpServer() {
    }

    String host;
    Integer port;
    String user;
    @ToString.Exclude
    String password;

    @SuppressWarnings("unused")
    public SmtpServer host(String host) {
        this.host = host;
        return this;
    }

    @SuppressWarnings("unused")
    public SmtpServer port(String port) {
        this.port = Integer.parseInt(port);
        return this;
    }

    @SuppressWarnings("unused")
    public SmtpServer port(Integer port) {
        this.port = port;
        return this;
    }

    @SuppressWarnings("unused")
    public SmtpServer user(String user) {
        this.user = user;
        return this;
    }

    @SuppressWarnings("unused")
    public SmtpServer password(String password) {
        this.password = password;
        return this;
    }
}
