package com.gw.bean;

public class Province implements java.io.Serializable {

    /**
     * 版本号
     */
    private static final long serialVersionUID = 5995182772719947329L;

    /** 省id */
    private String provinceid;

    /** 省名 */
    private String provincename;

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    @Override
    public String toString() {
        return "Province [provinceid=" + provinceid + ", provincename=" + provincename + "]";
    }

}