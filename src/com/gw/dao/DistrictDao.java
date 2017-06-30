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
import com.gw.bean.District;

public class DistrictDao {
    private QueryRunner qr = new TxQueryRunner();

    public List<District> loadDistrict(String cityID) {
        String sql = "SELECT districtid,districtname FROM S_District WHERE CityID=?";
        List<Map<String, Object>> listMap;
        try {
            listMap = qr.query(sql, new MapListHandler(), cityID);
            List<District> listDistrict = new ArrayList<District>();
            for (Map<String, Object> map : listMap) {
                District dist = CommonUtils.toBean(map, District.class);
                listDistrict.add(dist);
            }
            return listDistrict;
        } catch (SQLException e) {
            // 打印详细异常信息
            e.printStackTrace();
            // 将异常包装后接着往上抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加区
     * 
     * @param dist
     * @return 添加后的自增ID
     */
    public int addDistrict(District dist) {
        String sql = "INSERT INTO S_District(districtname,cityid) VALUES(?,?) SELECT @@IDENTITY";
        Object obj;
        try {
            obj = qr.query(sql, new ScalarHandler(), dist.getDistrictname(), dist.getCityid());
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
     * 根据区ID删除省
     * 
     * @param distId
     * @return 是否删除成功
     */
    public boolean delDistrictByDid(int distId) {
        String sql = "DELETE S_District WHERE districtid=?";
        try {
            return qr.update(sql, distId) > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回删除失败
            return false;
        }
    }

    /**
     * 修改区名
     * 
     * @param dist
     * @return
     */
    public boolean updateProvNameByPid(District dist) {
        String sql = "UPDATE S_District SET districtname=? WHERE districtid=?";
        try {
            return qr.update(sql, dist.getDistrictname(), dist.getDistrictid()) > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回修改失败
            return false;
        }
    }

    /**
     * 获知该键值下是否有引用
     * 
     * @param cityId
     * @return
     */
    public boolean isHaveChild(String cityId) {
        String sql = "SELECT COUNT(1) FROM S_District WHERE cityid=?";
        Object obj;
        try {
            obj = qr.query(sql, new ScalarHandler(), cityId);
            Number number = (Number) obj;
            return number.intValue() > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回false
            return false;
        }
    }
}
