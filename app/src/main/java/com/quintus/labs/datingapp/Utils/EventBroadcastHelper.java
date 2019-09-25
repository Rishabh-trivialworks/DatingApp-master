package com.quintus.labs.datingapp.Utils;


import com.quintus.labs.datingapp.eventbus.Events;
import com.quintus.labs.datingapp.eventbus.GlobalBus;
import com.quintus.labs.datingapp.rest.Response.AddressList;

public class EventBroadcastHelper {



    public static void sendAddressUpdate(AddressList addressList) {
        GlobalBus.getBus().post(new Events.AddressUpdate(addressList));
    }
    public static void sendPaymentUpdate(int bookingid, String status) {
        GlobalBus.getBus().post(new Events.paymentUpdated(bookingid,status));
    }

    public static  void  sendPaymentStatus(){
        GlobalBus.getBus().post(new Events.paymentUpdateRsa());
    }
}
