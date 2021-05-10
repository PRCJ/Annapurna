package com.glitch.annapurna;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Donar {

    public String Address;
    public String phonenumber;
    public String roti;
    public String sabji;
    public String dal;
    public String chawal;
    public String other;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Donar() {
    }

    public Donar(String address, String phonenumber,String roti,String sabji,String dal,String chawal,String other) {
        this.Address = address;
        this.phonenumber = phonenumber;
        this.roti = roti;
        this.sabji = sabji;
        this.dal = dal;
        this.chawal = chawal;
        this.other = other;
    }
}