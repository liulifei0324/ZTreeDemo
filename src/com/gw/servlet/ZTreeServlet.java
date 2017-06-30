package com.gw.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.guowei.commons.CommonUtils;
import com.guowei.servlet.BaseServlet;
import com.gw.bean.City;
import com.gw.bean.District;
import com.gw.bean.Province;
import com.gw.dao.CityDao;
import com.gw.dao.DistrictDao;
import com.gw.dao.ProvinceDao;

/**
 * 此类负责处理页面所有异步请求
 * 
 * @author 太阳乐无忧
 * 
 */
public class ZTreeServlet extends BaseServlet {
    private ProvinceDao provDao = new ProvinceDao();

    private CityDao cityDao = new CityDao();

    private DistrictDao distDao = new DistrictDao();

    /**
     * 版本号
     */
    private static final long serialVersionUID = -1616956509382466927L;

    /**
     * 初始化加载数据
     * 
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String init(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        String level = request.getParameter("level");
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hm = null;
        if (level == null) {// 第一次加载数据没有传递level
            hm = new HashMap<String, Object>(); // 最外层，父节点
            hm.put("id", 0);// id属性 ，数据传递
            hm.put("name", "中国行政区划"); // name属性，显示节点名称
            hm.put("icon", "css/img/diy/china.png"); // 图标属性，显示DIY图标
            hm.put("isParent", "true");// 是否为父节点
            list.add(hm);
        } else if (level.equals("0")) {// level=0表示当前操作节点是省级
            List<Province> listProv = provDao.loadProvince();
            for (Province prov : listProv) {
                hm = new HashMap<String, Object>();
                hm.put("id", prov.getProvinceid());
                hm.put("name", prov.getProvincename());
                hm.put("icon", "css/img/diy/sheng.png");
                if (cityDao.isHaveChild(prov.getProvinceid())) {// 判断是否有子节点
                    hm.put("isParent", "true");
                } else {
                    hm.put("isParent", "false");
                }
                list.add(hm);
            }
        } else if (level.equals("1")) {// level=1表示当前操作节点是市级
            List<City> listCity = cityDao.loadCity(id);
            for (City city : listCity) {
                hm = new HashMap<String, Object>();
                hm.put("id", city.getCityid());
                hm.put("name", city.getCityname());
                hm.put("icon", "css/img/diy/shi.png");
                if (distDao.isHaveChild(city.getCityid())) {// 判断是否有子节点
                    hm.put("isParent", "true");
                } else {
                    hm.put("isParent", "false");
                }
                list.add(hm);
            }
        } else if (level.equals("2")) {// level=2表示当前操作节点是区级
            List<District> listDistrict = distDao.loadDistrict(id);
            for (District dist : listDistrict) {
                hm = new HashMap<String, Object>();
                hm.put("id", dist.getDistrictid());
                hm.put("name", dist.getDistrictname());
                hm.put("icon", "css/img/diy/qu.png");
                hm.put("isParent", "false");
                list.add(hm);
            }
        }
        // 将list转换成JSON字符串并打印
        response.getWriter().print(JSONArray.fromObject(list));
        return null;
    }

    /**
     * 添加省
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String addProvince(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Province prov = CommonUtils.toBean(request.getParameterMap(), Province.class);
        ProvinceDao provDao = new ProvinceDao();
        // 获取添加省记录后的自增ID
        int afterInsertPid = provDao.addProvince(prov);
        response.getWriter().print(afterInsertPid);
        return null;
    }

    /**
     * 添加市
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String addCity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        City city = CommonUtils.toBean(request.getParameterMap(), City.class);
        CityDao cityDao = new CityDao();
        // 获取添加市记录后的自增ID
        int afterInsertCid = cityDao.addCity(city);
        response.getWriter().print(afterInsertCid);
        return null;
    }

    /**
     * 添加区
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String addDistrict(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        District dist = CommonUtils.toBean(request.getParameterMap(), District.class);
        DistrictDao distDao = new DistrictDao();
        // 获取添加区记录后的自增ID
        int afterInsertDid = distDao.addDistrict(dist);
        response.getWriter().print(afterInsertDid);
        return null;
    }

    /**
     * 修改省名
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String updateProvName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Province prov = CommonUtils.toBean(request.getParameterMap(), Province.class);
        ProvinceDao provDao = new ProvinceDao();
        boolean result = provDao.updateProvNameByPid(prov);
        response.getWriter().print(result);
        return null;
    }

    /**
     * 修改市名
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String updateCityName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        City city = CommonUtils.toBean(request.getParameterMap(), City.class);
        CityDao cityDao = new CityDao();
        boolean result = cityDao.updateProvNameByPid(city);
        response.getWriter().print(result);
        return null;
    }

    /**
     * 修改区名
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String updateDistName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        District dist = CommonUtils.toBean(request.getParameterMap(), District.class);
        DistrictDao distDao = new DistrictDao();
        boolean result = distDao.updateProvNameByPid(dist);
        response.getWriter().print(result);
        return null;
    }

    /**
     * 删除省
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String delProvinceByPid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int provId = Integer.parseInt(request.getParameter("provId"));
        ProvinceDao provDao = new ProvinceDao();
        boolean result = provDao.delProvinceByPid(provId);
        response.getWriter().print(result);
        return null;
    }

    /**
     * 删除市
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String delCityByCid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int cityId = Integer.parseInt(request.getParameter("cityId"));
        CityDao cityDao = new CityDao();
        boolean result = cityDao.delCityByCid(cityId);
        response.getWriter().print(result);
        return null;
    }

    /**
     * 删除区
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String delDistrictByDid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int distId = Integer.parseInt(request.getParameter("distId"));
        DistrictDao distDao = new DistrictDao();
        boolean result = distDao.delDistrictByDid(distId);
        response.getWriter().print(result);
        return null;
    }

}
