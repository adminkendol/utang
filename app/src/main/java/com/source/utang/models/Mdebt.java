package com.source.utang.models;

/**
 * Created by chandra on 04/01/2018.
 */

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Mdebt implements Serializable{

    private String id;
    private String nama;
    private String tgl;
    private String nominal;
    private String key;

    public Mdebt(){

    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getTgl() {
        return tgl;
    }
    public void setTgl(String tgl) {
        this.tgl = tgl;
    }
    public String getNominal() {
        return nominal;
    }
    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    @Override
    public String toString() {
        return " "+id+"\n" +
                " "+nama+"\n" +
                " "+tgl +"\n" +
                " "+nominal;
    }
    public Mdebt(String idN, String nmN,String tglN, String nomN){
        id = idN;
        nama = nmN;
        tgl = tglN;
        nominal = nomN;
    }
}
