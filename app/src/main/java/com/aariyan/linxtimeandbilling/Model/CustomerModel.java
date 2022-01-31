package com.aariyan.linxtimeandbilling.Model;

public class CustomerModel {
    private String strCustName,strCustDesc,Uid;

    public CustomerModel(){}

    public CustomerModel(String strCustName, String strCustDesc, String uid) {
        this.strCustName = strCustName;
        this.strCustDesc = strCustDesc;
        Uid = uid;
    }

    public String getStrCustName() {
        return strCustName;
    }

    public void setStrCustName(String strCustName) {
        this.strCustName = strCustName;
    }

    public String getStrCustDesc() {
        return strCustDesc;
    }

    public void setStrCustDesc(String strCustDesc) {
        this.strCustDesc = strCustDesc;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    @Override
    public String toString() {
        return strCustDesc;
    }
}
