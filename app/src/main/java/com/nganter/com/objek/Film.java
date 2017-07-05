package com.nganter.com.objek;

/**
 * Created by Septiawan Aji Pradan on 7/5/2017.
 */

public class Film {
    private String idFilm;
    private String namaFilm;
    private String pathFoto;
    private String jamTayang;
    private int drawable;

    public Film(String idFilm,String namaFilm,String pathFoto){
        this.idFilm = idFilm;
        this.namaFilm = namaFilm;
        this.pathFoto = pathFoto;
    }

    public Film(int drawable,String namaFilm){
        this.drawable = drawable;
        this.namaFilm = namaFilm;
    }

    public String getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(String idFilm) {
        this.idFilm = idFilm;
    }

    public String getNamaFilm() {
        return namaFilm;
    }

    public void setNamaFilm(String namaFilm) {
        this.namaFilm = namaFilm;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getJamTayang() {
        return jamTayang;
    }

    public void setJamTayang(String jamTayang) {
        this.jamTayang = jamTayang;
    }
}
