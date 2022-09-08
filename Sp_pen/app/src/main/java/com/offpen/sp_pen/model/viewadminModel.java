package com.offpen.sp_pen.model;

public class viewadminModel {

    String img_path,f_name,user_type, user_id;

    public viewadminModel() {
    }

    public viewadminModel( String f_name, String user_type) {
        this.f_name = f_name;
        this.user_type = user_type;
    }

    public viewadminModel( String f_name, String user_type, String user_id) {
        this.img_path = img_path;
        this.f_name = f_name;
        this.user_type = user_type;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }
}
