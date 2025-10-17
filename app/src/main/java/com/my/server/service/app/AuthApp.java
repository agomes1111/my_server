package com.my.server.service.app;

import com.my.server.domain.data.ServiceApp;

public class AuthApp implements ServiceApp {
    private String path;
    private String httpVerb;
    private String loginPath;
    private String signUpPath;

    public AuthApp(){
        this.path = "/auth";
        this.httpVerb = "POST";

        this.loginPath = "/login";
        this.signUpPath = "/signUp";
    }

    public String getLoginPath() {
        return loginPath;
    }


    public String getSignUpPath() {
        return signUpPath;
    }

    @Override
    public String getHttpVerb() {
        return this.httpVerb;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setHttpVerb(String verb) {
        this.httpVerb = verb;
    }
}
