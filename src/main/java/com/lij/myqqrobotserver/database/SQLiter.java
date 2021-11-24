package com.lij.myqqrobotserver.database;


import com.lij.myqqrobotserver.entity.Img;

import java.sql.*;

/**
 * @author Celphis
 */
public class SQLiter {

    public static String jarpath;
    public static Connection conn;

    public static void setJarPath(String path) {
        jarpath = path;
    }

    public static void load() {
        connect();
    }

    public static void connect() {
        String driver = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:" + jarpath + "\\database.db";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("连接数据库时失败.");
            System.exit(1);
        }
    }

    public static Img getArtworkIdRandom() {
        String sql = "SELECT `imgid`,`workid`,`savetype`,`url` FROM `CrawledArtworks` WHERE `savetype` = 0 ORDER BY RANDOM() LIMIT 1";
        String imgid = null;
        Integer savetype = null;
        Integer workid = null;
        String url = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isClosed()) {
                imgid = rs.getString("imgid");
                workid = rs.getInt("workid");
                savetype = rs.getInt("savetype");
                url = rs.getString("url");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Img(imgid, workid, savetype, url);
    }
}
