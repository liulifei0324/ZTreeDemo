package com.gw.bean;

public class City implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 5018545437980667058L;

    /** 市id */
    private String cityid;

    /** 市名 */
    private String cityname;

    /** 省id */
    private String provinceid;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    @Override
    public String toString() {
        return "City [cityid=" + cityid + ", cityname=" + cityname + ", provinceid=" + provinceid + "]";
    }

}