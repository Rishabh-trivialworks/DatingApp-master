package com.quintus.labs.datingapp.rest.RequestModel;

/**
 * Created by Narendra on 20/05/2017.
 */
public class ChangePasswordModel {
    String oldPassword,newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public ChangePasswordModel(String password, String newPassword) {
        this.oldPassword = password;
        this.newPassword = newPassword;
    }



    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


}
