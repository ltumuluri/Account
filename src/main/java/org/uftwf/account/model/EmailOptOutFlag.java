package org.uftwf.account.model;

public class EmailOptOutFlag {
    private String mbsid;
    private boolean flag;
    public EmailOptOutFlag(String mbsid, boolean flag){
        this.mbsid=mbsid;
        this.flag=flag;
    }
    public String getMbsid() {
        return mbsid;
    }

    public void setMbsid(String mbsid) {
        this.mbsid = mbsid;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
