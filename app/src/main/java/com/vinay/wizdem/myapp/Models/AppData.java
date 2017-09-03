package com.vinay.wizdem.myapp.Models;

/**
 * Created by vinay_1 on 8/20/2017.
 */

public class AppData {

    private String about_me, app_link, skype, email,
            experience, home, ln_link, name, phone_number, stack_link;
    private static final String HU_MESSAGE = "Huu! Data not found..";
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
        if(about_me == null){
            return HU_MESSAGE;
        }else return about_me;
    }

    public String getApp_link() {
        if(app_link == null){
            return HU_MESSAGE;
        }else return app_link;
    }

    public String getSkype() {
        if(skype == null){
            return "Hu! skype not found..";
        }else return skype;
    }

    public String getEmail() {
        if(email == null){
            return "Hu! email not found..";
        }else return email;
    }

    public String getExperience() {
        if(experience == null){
            return HU_MESSAGE;
        } else return experience;
    }

    public String getHome() {
        if(home == null){
            return HU_MESSAGE;
        }else return home;
    }

    public String getLn_link() {
        //validations at MainActivity
        return ln_link;
    }

    public String getName() {
        //validations at MainActivity

        return name;
    }

    public String getPhone_number() {
        //validations at MainActivity

        return phone_number;
    }

    public String getStack_link() {
        //validations at MainActivity

        return stack_link;
    }
}
