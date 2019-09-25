package com.quintus.labs.datingapp.eventbus;


import com.quintus.labs.datingapp.rest.Response.AddressList;

public class Events {


    public static class AddressUpdate {
        public AddressList addressList;

        public AddressUpdate(AddressList addressList) {
            this.addressList = addressList;
        }

        public AddressList getAddressList() {
            return addressList;
        }
    }



    public static class paymentUpdated {
        private int bookinId;
        private String status;

        public paymentUpdated(int bookinId, String status) {
            this.bookinId = bookinId;
            this.status = status;

        }

        public int getBookinId() {
            return bookinId;
        }

        public void setBookinId(int bookinId) {
            this.bookinId = bookinId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class paymentUpdateRsa
    {

    }

}
