package com.source.utang.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.source.utang.R;
import com.source.utang.fragments.DataNasabah;
import com.source.utang.fragments.DataUtang;
import com.source.utang.models.Mdebt;
import com.source.utang.models.Mnasabah;

import java.util.ArrayList;

/**
 * Created by chandra on 04/01/2018.
 */

public class AdapterUtangRecyclerView extends RecyclerView.Adapter<AdapterUtangRecyclerView.ViewHolder>{
    private ArrayList<Mdebt> daftarUtang;
    private Context context;
    FirebaseDataListener listener;
    private DatabaseReference database;
    public AdapterUtangRecyclerView(ArrayList<Mdebt> debts, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarUtang = debts;
        context = ctx;
        //listener = (DataNasabah)ctx;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Inisiasi View
         */
        TextView tvNama;
        TextView tvNominal;
        TextView tvTgl;
        CardView cvMain;

        ViewHolder(View v) {
            super(v);
            tvNama = (TextView) v.findViewById(R.id.nama);
            tvNominal = (TextView) v.findViewById(R.id.nominal);
            tvTgl = (TextView) v.findViewById(R.id.tgl);
            cvMain = (CardView) v.findViewById(R.id.cv_main);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemutang, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /**
         *  Menampilkan data pada view
         */
        final String name = daftarUtang.get(position).getNama();
        final String nominal = daftarUtang.get(position).getNominal();
        final String tgl = daftarUtang.get(position).getTgl();
        final String id = daftarUtang.get(position).getId();
        final String key = daftarUtang.get(position).getKey();
        System.out.println("UTANG DATA one by one "+position+daftarUtang.size());
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //context.startActivity(FirebaseDBReadSingleActivity.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                final View mView = layoutInflaterAndroid.inflate(R.layout.adddebt, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);
                //final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                final EditText etNama = (EditText) mView.findViewById(R.id.namaD);
                final EditText etNominal = (EditText) mView.findViewById(R.id.nominalD);
                final EditText etTgl = (EditText) mView.findViewById(R.id.tglD);
                final TextView etId = (TextView) mView.findViewById(R.id.myIdD);
                etNama.setText(name);
                etNominal.setText(nominal);
                etTgl.setText(tgl);
                Log.d("KEY NASABAH", String.valueOf(id));
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                DataUtang dn = new DataUtang();
                                Editable edNama = etNama.getText();
                                Editable edNominal = etNominal.getText();
                                Editable edTgl = etTgl.getText();
                                CharSequence edId = etId.getText();
                                Mdebt mn = new Mdebt(String.valueOf(edId),String.valueOf(edNama),String.valueOf(edTgl),String.valueOf(edNominal));
                                Log.d("DATA DEBT", String.valueOf(mn));
                                dn.updateUtang(mn,key,context,mView);
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
                                listener.onDeleteData(daftarUtang.get(position), position);
                            }
                        }
                );
                return true;
            }
        });
        holder.tvNama.setText(name);
        holder.tvNominal.setText(nominal);
    }
    @Override
    public int getItemCount() {
        return daftarUtang.size();
    }
    public interface FirebaseDataListener{
        void onDeleteData(Mdebt mdebt, int position);
    }
}