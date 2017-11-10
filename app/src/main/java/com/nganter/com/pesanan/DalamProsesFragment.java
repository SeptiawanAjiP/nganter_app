package com.nganter.com.pesanan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nganter.com.R;
import com.nganter.com.objek.Pesanan;

import java.util.ArrayList;

/**
 * Created by aji on 11/10/2017.
 */

public class DalamProsesFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RelativeLayout rl;
    private TextView message;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dalam_proses,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.dalam_proses_rv);
        rl = (RelativeLayout)view.findViewById(R.id.memuat);
        message = (TextView)view.findViewById(R.id.message_status);

        ArrayList<Pesanan> arrayList = new ArrayList<>();

        Pesanan pe1 = new Pesanan("Quatrro112","17 September 2017, 18:00","Pesanan Anda mie goreng 3 bungkus lengkap dengan kuah panas dan es teh 5 bungkus, es nya dibungkus","makanan");
        Pesanan pe2 = new Pesanan("Praketa","17 Januari 2017, 18:00","Kopi 3 bungkus","barang");
        arrayList.add(pe1);
        arrayList.add(pe2);
        setRecyclerView(arrayList);
        return view;
    }

    private void setRecyclerView(ArrayList<Pesanan> pesananArrayList){
        if(pesananArrayList.size()==0){
            rl.setVisibility(View.VISIBLE);
            message.setText("Belum ada pesanan");
        }else{
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setNestedScrollingEnabled(false);


            DalamProsesAdapter dalamProsesAdapter = new DalamProsesAdapter(getActivity(),getContext(),pesananArrayList);
            recyclerView.setAdapter(dalamProsesAdapter);

        }
    }


}
