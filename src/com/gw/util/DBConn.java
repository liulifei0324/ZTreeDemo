package com.gw.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 单例模式获取数据库连接，未考虑多线程同步
 * 
 * @author 太阳乐无忧
 * 
 */
public class DBConn {

    public static final String JDBCDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=Test";

    public static final String USERNAME = "sa";

    public static final String PASSWORD = "sasa";

    private static Connection conn = null;

    static {
        try {
            Class.forName(JDBCDRIVER);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 获取连接对象
    public static Connection getConn() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;

    }

    // 关闭连接
    public static void closeConn(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(getConn());
        System.out.println(getConn());
        System.out.println(getConn());

    }

}
