package com.aariyan.linxtimeandbilling.Model;

public class UserListModel {
    private String uID;
    private String strPinCode;
    private String strName;
    private String intCompanyID;

    public UserListModel() {}

    public UserListModel(String uID, String strPinCode, String strName, String intCompanyID) {
        this.uID = uID;
        this.strPinCode = strPinCode;
        this.strName = strName;
        this.intCompanyID = intCompanyID;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getStrPinCode() {
        return strPinCode;
    }

    public void setStrPinCode(String strPinCode) {
        this.strPinCode = strPinCode;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getIntCompanyID() {
        return intCompanyID;
    }

    public void setIntCompanyID(String intCompanyID) {
        this.intCompanyID = intCompanyID;
    }
}
