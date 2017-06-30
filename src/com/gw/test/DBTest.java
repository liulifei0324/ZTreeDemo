package com.gw.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.guowei.jdbc.JdbcUtils;

/**
 * 测试类
 * 
 * @author 太阳乐无忧
 * 
 */
public class DBTest {
    /**
     * 测试数据库连接
     * 
     * @throws SQLException
     */
    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        System.out.println(conn);// 查看连接对象
        JdbcUtils.releaseConnection(conn);// 释放/关闭连接对象
        System.out.println(conn.isClosed());// 查看连接对象是否关闭
    }

}
