package com.quintus.labs.datingapp.rest.services;


import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.rest.RequestModel.AddAddressRequest;
import com.quintus.labs.datingapp.rest.RequestModel.ChangepasswordRequest;
import com.quintus.labs.datingapp.rest.RequestModel.EditProfileUpdateRequest;
import com.quintus.labs.datingapp.rest.RequestModel.HelpCenterRequestModel;
import com.quintus.labs.datingapp.rest.RequestModel.LoginRequest;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentRSARequest;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentRequest;
import com.quintus.labs.datingapp.rest.RequestModel.RegisterRequest;
import com.quintus.labs.datingapp.rest.RequestModel.SaveBookingModel;
import com.quintus.labs.datingapp.rest.Response.AddressList;
import com.quintus.labs.datingapp.rest.Response.CatagoryList;
import com.quintus.labs.datingapp.rest.Response.MapItem;
import com.quintus.labs.datingapp.rest.Response.MyBooking;
import com.quintus.labs.datingapp.rest.Response.RSAList;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.ServiceproviderList;
import com.quintus.labs.datingapp.rest.Response.SubCategoryList;
import com.quintus.labs.datingapp.rest.Response.UserData;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGIN)
    Call<ResponseModel<UserData>> login(@Body LoginRequest loginRequest);


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SIGNUP)
    Call<ResponseModel<UserData>> signup(@Body RegisterRequest registerRequest);

    //
    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.CATEGORYLIST)
    Call<ResponseModel<List<CatagoryList>>> getCategoryList();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SUBCATEGORYLIST + "/{CategoryId}")
    Call<ResponseModel<SubCategoryList>> getSubCategoryList(@Path(ServiceConstants.CATEGORYID) int categoryid);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.ADDADRESS)
    Call<ResponseModel<AddressList>> addAddress(@Body AddAddressRequest addAddressRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.ADDADRESS)
    Call<ResponseModel<List<AddressList>>> getAddress();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PROVIDERLIST )
    Call<ResponseModel<List<ServiceproviderList>>> getProviderList(@Query(ServiceConstants.OBJECTID) int objectId
            , @Query("serviceType") String serviceType);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SAVEBOOKING)
    Call<ResponseModel<Map<String, String>>> savebooking(@Body SaveBookingModel saveBookingModel);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SAVEBOOKING)
    Call<ResponseModel<List<MyBooking>>> myBookings();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GETUSER)
    Call<ResponseModel<UserData>> getUserDetails();


    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.GETUSER)
    Call<ResponseModel<UserData>> setUserDetails(@Body EditProfileUpdateRequest editProfileUpdateRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SENDFEEDBACK)
    Call<ResponseModel<Map<String, String>>> sendHelpCenter(@Body HelpCenterRequestModel helpCenterRequestModel);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGOUT)
    Call<ResponseModel<Map<String, String>>> logOut();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.CHANGEPASSWORD)
    Call<ResponseModel<Map<String, String>>> ChangePassword(@Body ChangepasswordRequest changepasswordRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.INITIATEPAYMENT+ "/{id}")
    Call<ResponseModel<Map<String, String>>> initiatePayment(@Path("id") int bookingid);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.FINALPAYMENT+ "/{id}")
    Call<ResponseModel<MyBooking>> paymentToServer(@Path("id") int bookingid, @Body PaymentRequest paymentRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.INITIATE)
    Call<ResponseModel<Map<String, String>>> initiatePaymentSubscribe();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.RSA)
    Call<ResponseModel<List<RSAList>>> rsaList();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.RSAPayment)
    Call<ResponseModel<UserData>> paymentToRsaServer(@Body PaymentRSARequest paymentRequest);


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.RSAMAPAPI+ "/{id}")
    Call<ResponseModel<MapItem>> rsaMapApi(@Path("id") int rsaServiceKey);
}
