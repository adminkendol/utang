package com.source.utang.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.source.utang.R;
import com.source.utang.fragments.DataNasabah;
import com.source.utang.models.Mnasabah;

import java.util.ArrayList;

/**
 * Created by chandra on 04/01/2018.
 */

public class AdapterNasabahRecyclerView extends RecyclerView.Adapter<AdapterNasabahRecyclerView.ViewHolder>{
    private ArrayList<Mnasabah> daftarNasabah;
    private Context context;
    FirebaseDataListener listener;

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
        CardView cvMain;

        ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.nama);
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
        System.out.println("BARANG DATA one by one "+position+daftarNasabah.size());
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *  Kodingan untuk tutorial Selanjutnya :p Read detail data
                 */
                //context.startActivity(FirebaseDBReadSingleActivity.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
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
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                //Button editButton = (Button) dialog.findViewById(R.id.bt_edit_data);
                //Button delButton = (Button) dialog.findViewById(R.id.bt_delete_data);

                //apabila tombol edit diklik
                /*editButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                //context.startActivity(FirebaseDBCreateActivity.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
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
                );*/
                return true;
            }
        });
        holder.tvTitle.setText(name);
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
