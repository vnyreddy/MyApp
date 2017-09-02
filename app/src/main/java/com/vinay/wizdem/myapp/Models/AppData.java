package com.vinay.wizdem.myapp.Models;

/**
 * Created by vinay_1 on 8/20/2017.
 */

public class AppData {

    public String about_me, app_link, skype, email,
            experience, home, ln_link, name, phone_number, stack_link;
    public AppData(){

    }

    public AppData(String about_me, String app_link, String contact, String email, String experience, String home,
                   String ln_link, String name, String phone_number, String stack_link) {
        this.about_me = about_me;
        this.app_link = app_link;
        this.skype = skype;
        this.email = email;
        this.experience = experience;
        this.home = home;
        this.ln_link = ln_link;
        this.name = name;
        this.phone_number = phone_number;
        this.stack_link = stack_link;
    }

    public String getAbout_me() {
        return about_me;
    }

    public String getApp_link() {
        return app_link;
    }

    public String getSkype() {
        return skype;
    }

    public String getEmail() {
        return email;
    }

    public String getExperience() {
        return experience;
    }

    public String getHome() {
        return home;
    }

    public String getLn_link() {
        return ln_link;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getStack_link() {
        return stack_link;
    }
}