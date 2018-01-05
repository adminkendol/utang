package com.source.utang.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.source.utang.R;
import com.source.utang.adapters.AdapterNasabahRecyclerView;
import com.source.utang.models.Mnasabah;

import java.util.ArrayList;

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
        database = FirebaseDatabase.getInstance().getReference();

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
        FloatingActionButton fab =(FloatingActionButton) view.findViewById(R.id.fabB);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "TEST", Toast.LENGTH_LONG).show();
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment mFrag = new AddNasabah();
                t.replace(R.id.container, mFrag);
                t.commit();
                getActivity().setTitle("Add new");
            }
        });
        return view;
    }
}
