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

public class SelesaiFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private RelativeLayout rl;
    private TextView message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selesai,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.selesai_rv);
        rl = (RelativeLayout)view.findViewById(R.id.memuat);
        message = (TextView)view.findViewById(R.id.message_status);

        ArrayList<Pesanan> arrayList = new ArrayList<>();

        Pesanan pe1 = new Pesanan("Quatrro112","17 September 2017, 18:00","Pesanan Anda mie goreng 3 bungkus lengkap dengan kuah panas dan es teh 5 bungkus, es nya dibungkus");
        Pesanan pe2 = new Pesanan("Shushu","17 September 2017, 18:00","Pesanan Anda mie goreng 3 bungkus lengkap dengan kuah panas dan es teh 5 bungkus, es nya dibungkus");
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
            Log.d("sinta0",pesananArrayList.size()+"");
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setNestedScrollingEnabled(false);


            SelesaiAdapter selesaiAdapter = new SelesaiAdapter(getActivity(),getContext(),pesananArrayList);
            recyclerView.setAdapter(selesaiAdapter);

        }
    }
}
