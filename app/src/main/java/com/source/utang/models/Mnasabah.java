package com.source.utang.models;

/**
 * Created by chandra on 04/01/2018.
 */
import android.text.Editable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Mnasabah implements Serializable{

    private String nama;
    private String phone;
    private String email;
    private String key;

    public Mnasabah(){

    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return " "+nama+"\n" +
                " "+phone +"\n" +
                " "+email;
    }
    public Mnasabah(String nm, String ph, String em){
        nama = nm;
        phone = ph;
        email = em;
    }
}
