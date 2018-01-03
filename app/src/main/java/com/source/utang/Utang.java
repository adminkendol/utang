package com.source.utang;

/**
 * Created by chandra on 02/01/2018.
 */

public class Utang {


    private String nasabah_id;
    private String nasabah_name;
    private String phone;
    private String alamat;
    private String email;
    private String utang;
    private String user_id;
    private String capture;

    public void setNasabahId (String nasabah_id){
        this.nasabah_id = nasabah_id;
    }
    public String getNasabahId(){return nasabah_id;}

    public void setNasabahName (String nasabah_name){
        this.nasabah_name = nasabah_name;
    }
    public String getNasabahName(){return nasabah_name;}

    public void setPhone (String phone){
        this.phone = phone;
    }
    public String getPhone(){return phone;}

    public void setAlamat (String alamat){
        this.alamat = alamat;
    }
    public String getAlamat(){return alamat;}

    public void setEmail (String email){
        this.email = email;
    }
    public String getEmail(){return email;}

    public void setUtang (String utang){
        this.utang = utang;
    }
    public String getUtang(){return utang;}

    public void setUserId (String user_id){
        this.user_id = user_id;
    }
    public String getUserId(){return user_id;}

    public void setCapture (String capture){
        this.capture = capture;
    }
    public String getCapture(){return capture;}
}
