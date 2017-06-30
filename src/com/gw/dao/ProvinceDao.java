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
import com.gw.bean.Province;

public class ProvinceDao {
    private QueryRunner qr = new TxQueryRunner();

    /**
     * 加载省
     * 
     * @return
     */
    public List<Province> loadProvince() {
        String sql = "SELECT provinceid,provincename FROM S_Province";
        List<Map<String, Object>> listMap;
        try {
            listMap = qr.query(sql, new MapListHandler());
            List<Province> listProvince = new ArrayList<Province>();
            for (Map<String, Object> map : listMap) {
                Province prov = CommonUtils.toBean(map, Province.class);
                listProvince.add(prov);
            }
            return listProvince;
        } catch (SQLException e) {
            // 打印详细异常信息
            e.printStackTrace();
            // 将异常包装后接着往上抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加省
     * 
     * @param prov
     * @return 添加后的自增ID
     */
    public int addProvince(Province prov) {
        String sql = "INSERT INTO S_Province(provincename) VALUES(?) SELECT @@IDENTITY";
        Object obj;
        try {
            obj = qr.query(sql, new ScalarHandler(), prov.getProvincename());
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
     * 根据省ID删除省
     * 
     * @param provId
     * @return 是否删除成功
     */
    public boolean delProvinceByPid(int provId) {
        String sql = "DELETE S_Province WHERE provinceid=?";
        try {
            return qr.update(sql, provId) > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回删除失败
            return false;
        }
    }

    /**
     * 修改省名
     * 
     * @param prov
     * @return
     */
    public boolean updateProvNameByPid(Province prov) {
        String sql = "UPDATE S_Province SET provincename=? WHERE provinceid=?";
        try {
            return qr.update(sql, prov.getProvincename(), prov.getProvinceid()) > 0;
        } catch (SQLException e) {
            // 如果出现异常就返回修改失败
            return false;
        }
    }

}
