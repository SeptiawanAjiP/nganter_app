package com.nganter.com.objek;

/**
 * Created by aji on 11/10/2017.
 */

public class Pesanan {
    private int idPesanan;
    private String kategori;
    private String toko;
    private String tanggal;
    private String waktu;
    private String isiPesanan;
    private String jenis;

    public Pesanan(){

    }

    public Pesanan(String toko,String waktu,String isi){
        this.toko = toko;
        this.waktu = waktu;
        this.isiPesanan = isi;
    }

    public Pesanan(String toko,String waktu,String isi,String jenis){
        this.toko = toko;
        this.waktu = waktu;
        this.isiPesanan = isi;
        this.jenis = jenis;
    }

    public int getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(int idPesanan) {
        this.idPesanan = idPesanan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getIsiPesanan() {
        return isiPesanan;
    }

    public void setIsiPesanan(String isiPesanan) {
        this.isiPesanan = isiPesanan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
