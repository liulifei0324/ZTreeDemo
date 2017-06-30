package com.gw.bean;

public class District implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -2730136537728638258L;

    /** 区id */
    private String districtid;

    /** 区名 */
    private String districtname;

    /** 所属市id */
    private String cityid;

    public String getDistrictid() {
        return districtid;
    }

    public void setDistrictid(String districtid) {
        this.districtid = districtid;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    @Override
    public String toString() {
        return "District [districtid=" + districtid + ", districtname=" + districtname + ", cityid=" + cityid + "]";
    }

}