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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.source.utang.R;
import com.source.utang.adapters.AdapterNasabahRecyclerView;
import com.source.utang.models.Mnasabah;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import static android.text.TextUtils.isEmpty;

/**
 * Created by chandra on 04/01/2018.
 */

public class DataNasabah extends Fragment {

    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Mnasabah> daftarNasabah;
    public DataNasabah(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listnasabah, container, false);
        /**
         * Inisialisasi RecyclerView & komponennya
         */
        rvView = (RecyclerView) view.findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rvView.setLayoutManager(layoutManager);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance().getReference();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading");
        //progressDialog.setTitle("database is");
        progressDialog.show();
        /**
         * Mengambil data barang dari Firebase Realtime DB
         */
        database.child("nasabah").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarNasabah = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Barang
                     * Dan juga menyimpan primary key pada object Barang
                     * untuk keperluan Edit dan Delete data
                     */
                    Mnasabah nasabah = noteDataSnapshot.getValue(Mnasabah.class);
                    nasabah.setKey(noteDataSnapshot.getKey());

                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarNasabah.add(nasabah);
                }
                /**
                 * Inisialisasi adapter dan data barang dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                adapter = new AdapterNasabahRecyclerView(daftarNasabah, getActivity());
                rvView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                progressDialog.dismiss();
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
        FloatingActionButton fab =(FloatingActionButton) view.findViewById(R.id.fabB);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.addnasabah, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
                alertDialogBuilderUserInput.setView(mView);

                //final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                final EditText etNama = (EditText) mView.findViewById(R.id.nama);
                final EditText etPhone = (EditText) mView.findViewById(R.id.phone);
                final EditText etEmail = (EditText) mView.findViewById(R.id.email);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                if(!isEmpty(etNama.getText().toString()) && !isEmpty(etPhone.getText().toString()) && !isEmpty(etEmail.getText().toString()))
                                    submitNasabah(new Mnasabah(etNama.getText().toString(), etPhone.getText().toString(), etEmail.getText().toString()));
                                else
                                    Snackbar.make(getActivity().findViewById(R.id.bt_submit), "Data nasabah tidak boleh kosong", Snackbar.LENGTH_LONG).show();

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
    private void submitNasabah(Mnasabah mnasabah) {
        database.child("nasabah").push().setValue(mnasabah).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(getView(), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment mFrag = new DataNasabah();
                t.replace(R.id.container, mFrag);
                t.commit();
            }
            /*public void onCancelled(DatabaseError databaseError){

            }*/
        });
    }
    public void updateNasabah(Mnasabah mnasabah,String key, final Context context, final View mView) {
        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */

        Log.d("KEY NAME NASABAH EXE", key);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference db = firebaseDatabase.getReference();
        //DatabaseReference db;
        //db = FirebaseDatabase.getInstance().getReference();
        db.child("nasabah") //akses parent index, ibaratnya seperti nama tabel
                .child(key) //select barang berdasarkan key
                .setValue(mnasabah) //set value barang yang baru
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
