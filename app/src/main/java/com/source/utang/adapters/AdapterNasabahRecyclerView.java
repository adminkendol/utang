package com.source.utang.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.source.utang.R;
import com.source.utang.fragments.DataNasabah;
import com.source.utang.models.Mnasabah;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import static android.text.TextUtils.isEmpty;

/**
 * Created by chandra on 04/01/2018.
 */

public class AdapterNasabahRecyclerView extends RecyclerView.Adapter<AdapterNasabahRecyclerView.ViewHolder>{
    private ArrayList<Mnasabah> daftarNasabah;
    private Context context;
    FirebaseDataListener listener;
    private DatabaseReference database;
    public AdapterNasabahRecyclerView(ArrayList<Mnasabah> nasabahs, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarNasabah = nasabahs;
        context = ctx;
        //listener = (DataNasabah)ctx;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Inisiasi View
         * Di tutorial ini kita hanya menggunakan data String untuk tiap item
         * dan juga view nya hanyalah satu TextView
         */
        TextView tvTitle;
        TextView tvPhone;
        CardView cvMain;

        ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.nama);
            tvPhone = (TextView) v.findViewById(R.id.phone);
            cvMain = (CardView) v.findViewById(R.id.cv_main);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnasabah, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /**
         *  Menampilkan data pada view
         */
        final String name = daftarNasabah.get(position).getNama();
        final String phone = daftarNasabah.get(position).getPhone();
        final String email = daftarNasabah.get(position).getEmail();
        final String key = daftarNasabah.get(position).getKey();
        System.out.println("NASABAH DATA one by one "+position+daftarNasabah.size());
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /**
                 *  Kodingan untuk tutorial Selanjutnya :p Read detail data
                 */
                //context.startActivity(FirebaseDBReadSingleActivity.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                final View mView = layoutInflaterAndroid.inflate(R.layout.addnasabah, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                //final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                final EditText etNama = (EditText) mView.findViewById(R.id.nama);
                final EditText etPhone = (EditText) mView.findViewById(R.id.phone);
                final EditText etEmail = (EditText) mView.findViewById(R.id.email);
                etNama.setText(name);
                etPhone.setText(phone);
                etEmail.setText(email);
                Log.d("KEY NASABAH", String.valueOf(key));
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                DataNasabah dn = new DataNasabah();
                                Editable edNama = etNama.getText();
                                Editable edPhone = etPhone.getText();
                                Editable edEmail = etEmail.getText();
                                Mnasabah mn = new Mnasabah(String.valueOf(edNama),String.valueOf(edPhone),String.valueOf(edEmail));
                                //mn.setKey(key);
                                Log.d("KEY NAME NASABAH", String.valueOf(mn));
                                dn.updateNasabah(mn,key,context,mView);
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
        holder.cvMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /**
                 *  Kodingan untuk tutorial Selanjutnya :p Delete dan update data
                 */
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                //dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = (Button) dialog.findViewById(R.id.bt_edit_data);
                Button delButton = (Button) dialog.findViewById(R.id.bt_delete_data);

                //apabila tombol edit diklik
                editButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                //context.startActivity(FirebaseDBCreateActivity.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
                                //updateNasabah(new Mnasabah(name, phone, email));
                            }
                        }
                );

                //apabila tombol delete diklik
                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                listener.onDeleteData(daftarNasabah.get(position), position);
                            }
                        }
                );
                return true;
            }
        });
        holder.tvTitle.setText(name);
        holder.tvPhone.setText(phone);
    }

    @Override
    public int getItemCount() {
        /**
         * Mengembalikan jumlah item pada barang
         */
        return daftarNasabah.size();
    }
    public interface FirebaseDataListener{
        void onDeleteData(Mnasabah nasabah, int position);
    }
}
