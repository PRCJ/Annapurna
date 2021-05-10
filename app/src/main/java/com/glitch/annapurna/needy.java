package com.glitch.annapurna;

import com.google.firebase.database.IgnoreExtraProperties;


    @IgnoreExtraProperties
    public class needy {

        public String Phone_number;
        public String Address;
        public String needy;

        // Default constructor required for calls to
        // DataSnapshot.getValue(User.class)
        public needy() {
        }

        public needy(String phone_number, String address, String needy) {
            this.Phone_number = phone_number;
            this.Address = address;
            this.needy = needy;
        }
    }

