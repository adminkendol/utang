package com.source.utang.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.source.utang.R;
import com.source.utang.adapters.AdapterNasabahRecyclerView;
import com.source.utang.adapters.AdapterUtangRecyclerView;
import com.source.utang.models.Mdebt;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

/**
 * Created by chandra on 04/01/2018.
 */

public class DataUtang extends Fragment {

    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Mdebt> daftarUtang;
    public DataUtang(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listutang, container, false);
        /**
         * Inisialisasi RecyclerView & komponennya
         */
        rvView = (RecyclerView) view.findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rvView.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading");
        progressDialog.show();
        /**
         * Mengambil data barang dari Firebase Realtime DB
         */
        database.child("debt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarUtang = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Mdebt utang = noteDataSnapshot.getValue(Mdebt.class);
                    utang.setKey(noteDataSnapshot.getKey());
                    daftarUtang.add(utang);
                }
                adapter = new AdapterUtangRecyclerView(daftarUtang, getActivity());
                rvView.setAdapter(adapter);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
        FloatingActionButton fab =(FloatingActionButton) view.findViewById(R.id.fabB);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.adddebt, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
                alertDialogBuilderUserInput.setView(mView);
                final EditText etNama = (EditText) mView.findViewById(R.id.namaD);
                final EditText etNominal = (EditText) mView.findViewById(R.id.nominalD);
                final EditText etTgl = (EditText) mView.findViewById(R.id.tglD);
                final TextView etId = (TextView) mView.findViewById(R.id.myIdD);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                if(!isEmpty(etNama.getText().toString()) && !isEmpty(etNama.getText().toString()) && !isEmpty(etTgl.getText().toString()))
                                    submitUtang(new Mdebt(etId.getText().toString(), etNama.getText().toString(), etTgl.getText().toString(), etNominal.getText().toString()));
                                else
                                    Snackbar.make(getActivity().findViewById(R.id.bt_submit), "Data utang tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                                InputMethodManager imm = (InputMethodManager)
                                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(
                                        etNama.getWindowToken(), 0);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });
        return view;
    }
    private void submitUtang(Mdebt mdebt) {
        database.child("debt").push().setValue(mdebt).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(getView(), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment mFrag = new DataUtang();
                t.replace(R.id.container, mFrag);
                t.commit();
            }
        });
    }
    public void updateUtang(Mdebt mdebt,String key, final Context context, final View mView) {

        Log.d("KEY NAME NASABAH EXE", key);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference db = firebaseDatabase.getReference();
        db.child("debt") //akses parent index, ibaratnya seperti nama tabel
                .child(key) //select barang berdasarkan key
                .setValue(mdebt) //set value barang yang baru
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Snackbar.make(mView, "Data berhasil diubah", Snackbar.LENGTH_LONG).show();
                        Log.d("OKAY DB", "SUCCESS");
                        //finish();
                    }
                });

    }

}
