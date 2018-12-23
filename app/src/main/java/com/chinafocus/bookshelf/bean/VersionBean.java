package com.chinafocus.bookshelf.bean;

public class VersionBean {

//    {
//        "downLoadUri": "http://192.168.123.106:8080/app-release.apk",
//            "versionCode": "2",
//            "versionDes": "2.0发布了，舒服爽，做了如下更新。。。。",
//            "versionName": "2.0"
//    }
    private String downLoadUri;
    private String versionCode;
    private String versionDes;
    private String versionName;

    public String getDownLoadUri() {
        return downLoadUri;
    }

    public void setDownLoadUri(String downLoadUri) {
        this.downLoadUri = downLoadUri;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionDes() {
        return versionDes;
    }

    public void setVersionDes(String versionDes) {
        this.versionDes = versionDes;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
