package com.gw.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.guowei.commons.CommonUtils;
import com.guowei.jdbc.TxQueryRunner;
import com.gw.bean.City;

public class CityDao {
    private QueryRunner qr = new TxQueryRunner();

    public List<City> loadCity(String provID) {
        String sql = "SELECT cityname,cityid FROM S_City WHERE ProvinceID=?";
        List<Map<String, Object>> listMap;
        try {
            listMap = qr.query(sql, new MapListHandler(), provID);
            List<City> listCity = new ArrayList<City>();
            for (Map<String, Object> map : listMap) {
                City city = CommonUtils.toBean(map, City.class);
                listCity.add(city);
            }
            return listCity;
        } catch (SQLException e) {
            // 打印详细异常信息
            e.printStackTrace();
            // 将异常包装后接着往上抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加市
     * 
     * @param city
     * @return 添加后的自增ID
     */
    public int addCity(City city) {
        String sql = "INSERT INTO S_City(cityname,provinceid) VALUES(?,?) SELECT @@IDENTITY";
        Object obj;
        try {
            obj = qr.query(sql, new ScalarHandler(), city.getCityname(), city.getProvinceid());
            Number number = (Number) obj;
            return number.intValue();
        } catch (SQLException e) {
            // 打印详细异常信息
            e.printStackTrace();
            // 将异常包装后接着往上抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据市ID删除省
     * 
     * @param cityId
     * @return 是否删除成功
     */
    public boolean delCityByCid(int cityId) {
        String sql = "DELETE S_City WHERE cityid=?";
        try {
            return qr.update(sql, cityId) > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回删除失败
            return false;
        }
    }

    /**
     * 修改市名
     * 
     * @param city
     * @return
     */
    public boolean updateProvNameByPid(City city) {
        String sql = "UPDATE S_City SET cityname=? WHERE cityid=?";
        try {
            return qr.update(sql, city.getCityname(), city.getCityid()) > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回修改失败
            return false;
        }
    }

    /**
     * 获知该键值下是否有引用
     * 
     * @param provId
     * @return
     */
    public boolean isHaveChild(String provId) {
        String sql = "SELECT COUNT(1) FROM S_City WHERE provinceid=?";
        Object obj;
        try {
            obj = qr.query(sql, new ScalarHandler(), provId);
            Number number = (Number) obj;
            return number.intValue() > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回false
            return false;
        }
    }
}
