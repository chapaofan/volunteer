package com.volunteer.volunteer.model;

public class UserInformation {
    private Integer mainId;

    private String openId;

    private String falseName;

    private String headPictureUrl;

    private String realName;

    private String sex;

    private String telNo;

    private String wechat;

    private String department;

    private String school;

    private String organization;

    private String registTime;

    /**暂时不用，以后记得规范
    * position 数字代表：
    * 11 正队长 12 副队长
    * 21 正部长 22 副部长
    * 3  部员   4  游客
    */
    private String position;

    public Integer getMainId() {
        return mainId;
    }

    public void setMainId(Integer mainId) {
        this.mainId = mainId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getFalseName() {
        return falseName;
    }

    public void setFalseName(String falseName) {
        this.falseName = falseName == null ? null : falseName.trim();
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl == null ? null : headPictureUrl.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "mainId=" + mainId +
                ", openId='" + openId + '\'' +
                ", falseName='" + falseName + '\'' +
                ", headPictureUrl='" + headPictureUrl + '\'' +
                ", realName='" + realName + '\'' +
                ", sex='" + sex + '\'' +
                ", telNo='" + telNo + '\'' +
                ", wechat='" + wechat + '\'' +
                ", department='" + department + '\'' +
                ", school='" + school + '\'' +
                ", organization='" + organization + '\'' +
                ", registTime='" + registTime + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}