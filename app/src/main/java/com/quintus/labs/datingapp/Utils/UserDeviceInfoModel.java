package com.quintus.labs.datingapp.Utils;

import java.io.Serializable;

public class UserDeviceInfoModel implements Serializable {
    private String deviceType;
    private String appVersion;

    public UserDeviceInfoModel(String deviceType, String appVersion) {
        this.deviceType = deviceType;
        this.appVersion = appVersion;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}