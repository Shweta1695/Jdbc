package com.offpen.sp_pen.model;

public class viewstudentmodel {

    String f_name, l_name, passcode, f_lang, t_lang, gender, img_path;
    int id;

    public viewstudentmodel(String f_name, String l_name, String passcode, String f_lang, String t_lang, String gender, String img_path, int id) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.passcode = passcode;
        this.f_lang = f_lang;
        this.t_lang = t_lang;
        this.gender = gender;
        this.img_path = img_path;
        this.id = id;
    }

    public viewstudentmodel(String f_name, int id) {
        this.f_name = f_name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public viewstudentmodel() {
    }


    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getF_lang() {
        return f_lang;
    }

    public void setF_lang(String f_lang) {
        this.f_lang = f_lang;
    }

    public String getT_lang() {
        return t_lang;
    }

    public void setT_lang(String t_lang) {
        this.t_lang = t_lang;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
