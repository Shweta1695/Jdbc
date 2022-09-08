package com.offpen.sp_pen.utils;

import android.app.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class variables extends Application {
    public static final String url = "jdbc:mysql://192.168.0.109:3306/ScanPen_1?characterEncoding=utf8";
    public static final String user = "user";
    public static final String pass = "user@123";
    public static Connection con;

    {
        try {
            con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}