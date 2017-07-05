package com.nganter.com.objek;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class Order {
    private String toko;
    private String pesanan;
    private String jamAntar;
    private String alamatAntar;

    public Order(String toko,String pesanan,String jamAntar,String alamatAntar){
        this.toko = toko;
        this.pesanan = pesanan;
        this.jamAntar = jamAntar;
        this.alamatAntar = alamatAntar;
    }
    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getPesanan() {
        return pesanan;
    }

    public void setPesanan(String pesanan) {
        this.pesanan = pesanan;
    }

    public String getAlamatAntar() {
        return alamatAntar;
    }

    public void setAlamatAntar(String alamatAntar) {
        this.alamatAntar = alamatAntar;
    }

    public String getJamAntar() {
        return jamAntar;
    }

    public void setJamAntar(String jamAntar) {
        this.jamAntar = jamAntar;
    }
}
