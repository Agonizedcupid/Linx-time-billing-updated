package com.aariyan.linxtimeandbilling.Model;

public class PostingModel {
    private String WorkDone,JobId,CustomerName,ProjectName,StartDate,EndDate,TotalTime,Times,
            Uid,Km,BillableTime,strAddEmail,intCompanyID;

    public PostingModel(){}

    public PostingModel(String workDone, String jobId, String customerName, String projectName, String startDate, String endDate, String totalTime, String times, String uid, String km, String billableTime, String strAddEmail, String intCompanyID) {
        WorkDone = workDone;
        JobId = jobId;
        CustomerName = customerName;
        ProjectName = projectName;
        StartDate = startDate;
        EndDate = endDate;
        TotalTime = totalTime;
        Times = times;
        Uid = uid;
        Km = km;
        BillableTime = billableTime;
        this.strAddEmail = strAddEmail;
        this.intCompanyID = intCompanyID;
    }

    public String getWorkDone() {
        return WorkDone;
    }

    public void setWorkDone(String workDone) {
        WorkDone = workDone;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getKm() {
        return Km;
    }

    public void setKm(String km) {
        Km = km;
    }

    public String getBillableTime() {
        return BillableTime;
    }

    public void setBillableTime(String billableTime) {
        BillableTime = billableTime;
    }

    public String getStrAddEmail() {
        return strAddEmail;
    }

    public void setStrAddEmail(String strAddEmail) {
        this.strAddEmail = strAddEmail;
    }

    public String getIntCompanyID() {
        return intCompanyID;
    }

    public void setIntCompanyID(String intCompanyID) {
        this.intCompanyID = intCompanyID;
    }
}
