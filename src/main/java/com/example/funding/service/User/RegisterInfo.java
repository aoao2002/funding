package com.example.funding.service.User;

public class RegisterInfo {
    String email;
    String pwd;
    String name;

    String identity;

    public RegisterInfo(String email, String pwd, String name, String identity) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }
}
