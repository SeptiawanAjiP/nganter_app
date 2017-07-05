package com.nganter.com.objek;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class Antar {
    private String alamatAmbil;
    private String alamatAntar;
    private String waktuAntar;
    private String jenisBarang;

    public Antar(String alamatAmbil,String alamatAntar,String waktuAntar,String jenisBarang){
        this.alamatAntar = alamatAntar;
        this.alamatAmbil = alamatAmbil;
        this.waktuAntar = waktuAntar;
        this.jenisBarang = jenisBarang;
    }
    public String getAlamatAmbil() {
        return alamatAmbil;
    }

    public void setAlamatAmbil(String alamatAmbil) {
        this.alamatAmbil = alamatAmbil;
    }

    public String getAlamatAntar() {
        return alamatAntar;
    }

    public void setAlamatAntar(String alamatAntar) {
        this.alamatAntar = alamatAntar;
    }

    public String getWaktuAntar() {
        return waktuAntar;
    }

    public void setWaktuAntar(String waktuAntar) {
        this.waktuAntar = waktuAntar;
    }

    public String getJenisBarang() {
        return jenisBarang;
    }

    public void setJenisBarang(String jenisBarang) {
        this.jenisBarang = jenisBarang;
    }
}
