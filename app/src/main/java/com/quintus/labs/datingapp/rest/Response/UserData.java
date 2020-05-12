
package com.quintus.labs.datingapp.rest.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quintus.labs.datingapp.Matched.Users;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.MediaModel;
import com.quintus.labs.datingapp.xmpp.utils.UserDeviceInfoModel;
import com.quintus.labs.datingapp.xmpp.utils.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Serializable {
    public UserData(UserModel userModel) {
        this.name = userModel.getName();
        //this.surName = userModel.getSurName();
        this.email = userModel.getEmail();
        this.id = userModel.getId();
        this.username = userModel.getUsername();
        this.isPremiumUser = userModel.isPremiumUser();
        this.receivePrivateMsg = userModel.isReceivePrivateMsg();
        this.onWhoseSide = userModel.getOnWhoseSide();
        this.deviceInfo = userModel.getDeviceInfo();
        this.isBlocked = userModel.isBlocked();
        this.hideReadReceipt=userModel.isHideReadReceipt();

        MediaModel mediaModel = new MediaModel();
        mediaModel.setMediaPath(userModel.getProfileImage());
        mediaModel.setThumbnailPath(userModel.getProfileImageThumbnail());
        mediaModel.setMediaType(AppConstants.ApiParamValue.MEDIA_TYPE_IMAGE);
        this.profileImage = mediaModel;
    }
    public UserData(Users userModel) {
        this.name = userModel.getName();
        //this.surName = userModel.getSurName();
        this.email = "";
        this.id = Integer.parseInt(userModel.getUserId());
        this.username = userModel.getName();
        this.isPremiumUser = true;
        this.receivePrivateMsg = true;
        this.onWhoseSide = "OTHER";
        List<UserDeviceInfoModel> list = new ArrayList<>();
        list.add(new UserDeviceInfoModel("android","1.0.0"));
        this.deviceInfo = list;
        this.isBlocked = false;
        this.hideReadReceipt=false;

        MediaModel mediaModel = new MediaModel();
        mediaModel.setMediaPath(userModel.getProfileImageUrl());
        mediaModel.setThumbnailPath(userModel.getProfileImageUrl());
        mediaModel.setMediaType(AppConstants.ApiParamValue.MEDIA_TYPE_IMAGE);
        this.profileImage = mediaModel;
    }
    public UserData(UserData userModel) {
        this.name = userModel.getFullName();
        this.fullName = userModel.getFullName();
        this.username = userModel.getFullName();

        this.email = userModel.getEmail();
        this.id = userModel.getId();
        this.isPremiumUser = true;
        this.receivePrivateMsg = true;
        this.onWhoseSide = "OTHER";
        List<UserDeviceInfoModel> list = new ArrayList<>();
        list.add(new UserDeviceInfoModel("android","1.0.0"));
        this.deviceInfo = list;
        this.isBlocked = false;
        this.hideReadReceipt=false;



    }

    private  String username;
    private  boolean isBlocked;
    private  String onWhoseSide;
    private  List<UserDeviceInfoModel> deviceInfo;
    private  boolean hideReadReceipt;
    private  MediaModel profileImage;

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("userToken")
    @Expose
    private String userToken;
    @SerializedName("km")
    @Expose
    private Object km;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("rsaActive")
    @Expose
    private boolean rsaActive;

    @SerializedName("rsaExpiry")
    @Expose
    private String rsaExpiry;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("interested")
    @Expose
    private String interested;

    @SerializedName("minRange")
    @Expose
    private int minRange;

    @SerializedName("maxRange")
    @Expose
    private int maxRange;

    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("about")
    @Expose
    private String about;

    @SerializedName("media")
    @Expose
    private ArrayList<ImageModel> media;

    @SerializedName("height")
    @Expose
    private Double height;

    @SerializedName("kids")
    @Expose
    private String kids;

    @SerializedName("pets")
    @Expose
    private String pets;

    @SerializedName("lookingFor")
    @Expose
    private String lookingFor;

    @SerializedName("smoking")
    @Expose
    private String smoking;

    @SerializedName("drinking")
    @Expose
    private String drinking;

    @SerializedName("education")
    @Expose
    private String education;

    @SerializedName("exercise")
    @Expose
    private String exercise;

    @SerializedName("zodiac")
    @Expose
    private String zodiac;

    @SerializedName("religion")
    @Expose
    private String religion;

    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("interests")
    @Expose
    private List<Interest> interests = null;

    @SerializedName("politicalLeanings")
    @Expose
    private String politicalLeanings;

    @SerializedName("isPremiumUser")
    @Expose
    private boolean isPremiumUser;

    @SerializedName("subscription")
    @Expose
    private Subscription subscription;

    @SerializedName("subscriptionId")
    @Expose
    private int subscriptionId;


    private boolean receivePrivateMsg;
    private boolean receivePrivateMsgNotification;

    private boolean userPresenceStatus;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Object getKm() {
        return km;
    }

    public void setKm(Object km) {
        this.km = km;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isRsaActive() {
        return rsaActive;
    }

    public void setRsaActive(boolean rsaActive) {
        this.rsaActive = rsaActive;
    }

    public String getRsaExpiry() {
        return rsaExpiry;
    }

    public void setRsaExpiry(String rsaExpiry) {
        this.rsaExpiry = rsaExpiry;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public int getMinRange() {
        return minRange;
    }

    public void setMinRange(int minRange) {
        this.minRange = minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public ArrayList<ImageModel> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<ImageModel> media) {
        this.media = media;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
        this.drinking = drinking;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isReceivePrivateMsg() {
        return receivePrivateMsg;
    }

    public void setReceivePrivateMsg(boolean receivePrivateMsg) {
        this.receivePrivateMsg = receivePrivateMsg;
    }

    public String getUsername() {
        return username;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }

    public String getOnWhoseSide() {
        return onWhoseSide;
    }

    public List<UserDeviceInfoModel> getDeviceInfo() {
        return deviceInfo;
    }

    public boolean isHideReadReceipt() {
        return hideReadReceipt;
    }

    public MediaModel getProfileImage() {
        return profileImage;
    }

    public boolean isReceivePrivateMsgNotification() {
        return receivePrivateMsgNotification;
    }

    public void setReceivePrivateMsgNotification(boolean receivePrivateMsgNotification) {
        this.receivePrivateMsgNotification = receivePrivateMsgNotification;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPremiumUser(boolean premiumUser) {
        isPremiumUser = premiumUser;
    }

    public void setOnWhoseSide(String onWhoseSide) {
        this.onWhoseSide = onWhoseSide;
    }

    public void setDeviceInfo(List<UserDeviceInfoModel> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setHideReadReceipt(boolean hideReadReceipt) {
        this.hideReadReceipt = hideReadReceipt;
    }

    public void setProfileImage(MediaModel profileImage) {
        this.profileImage = profileImage;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public String getPoliticalLeanings() {
        return politicalLeanings;
    }

    public void setPoliticalLeanings(String politicalLeanings) {
        this.politicalLeanings = politicalLeanings;
    }

    public boolean isUserPresenceStatus() {
        return userPresenceStatus;
    }

    public void setUserPresenceStatus(boolean userPresenceStatus) {
        this.userPresenceStatus = userPresenceStatus;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
