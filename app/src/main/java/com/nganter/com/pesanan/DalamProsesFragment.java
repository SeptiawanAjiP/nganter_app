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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nganter.com.NganterApp;
import com.nganter.com.R;
import com.nganter.com.SessionManager;
import com.nganter.com.koneksi.Alamat;
import com.nganter.com.objek.Pesanan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aji on 11/10/2017.
 */

public class DalamProsesFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RelativeLayout rl;
    private TextView message;
    private SessionManager sessionManager;
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

        sessionManager = new SessionManager(getContext());

        getData();
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

    public void getData(){
        final ArrayList<Pesanan> pesananArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Alamat.ALAMT_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("respon");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Pesanan pesanan = new Pesanan();
                        pesanan.setIdPesanan(Integer.parseInt(object.getString("id_order")));
                        pesanan.setJenis(object.getString("kategori"));
                        pesanan.setIsiPesanan(object.getString("pesanan"));
                        pesanan.setWaktu(object.getString("jam_antar"));
                        pesanan.setTanggal(object.getString("create_at"));
                        pesananArrayList.add(pesanan);
                    }

                    setRecyclerView(pesananArrayList);
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("kode","dalam_proses");
                map.put("id_pelanggan",sessionManager.getUserAkun().getIdPelanggan());
                return map;
            }
        };
        NganterApp.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


}
