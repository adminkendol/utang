package com.source.utang.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.source.utang.R;
import com.source.utang.models.Mnasabah;

import static android.text.TextUtils.isEmpty;

/**
 * Created by chandra on 04/01/2018.
 */

public class AddNasabah extends Fragment {
    private DatabaseReference database;
    private Button btSubmit;
    private EditText etNama;
    private EditText etPhone;
    private EditText etEmail;
    public AddNasabah(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addnasabah, container, false);
        etNama = (EditText) view.findViewById(R.id.nama);
        etPhone = (EditText) view.findViewById(R.id.phone);
        etEmail = (EditText) view.findViewById(R.id.email);
        btSubmit = (Button) view.findViewById(R.id.bt_submit);
        database = FirebaseDatabase.getInstance().getReference();

        //final Mnasabah mnsb = (Mnasabah) getIntent().getSerializableExtra("data");
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(etNama.getText().toString()) && !isEmpty(etPhone.getText().toString()) && !isEmpty(etEmail.getText().toString()))
                    submitNasabah(new Mnasabah(etNama.getText().toString(), etPhone.getText().toString(), etEmail.getText().toString()));
                else
                    Snackbar.make(view.findViewById(R.id.bt_submit), "Data barang tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        etNama.getWindowToken(), 0);
            }
        });
        return view;
    }
    private void submitNasabah(Mnasabah mnasabah) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database.child("nasabah").push().setValue(mnasabah).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etNama.setText("");
                etPhone.setText("");
                etEmail.setText("");
                Snackbar.make(getActivity().findViewById(R.id.bt_submit), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment mFrag = new Listnasabah();
                t.replace(R.id.container, mFrag);
                t.commit();
                getActivity().setTitle("Add new");
            }
        });
    }

}
