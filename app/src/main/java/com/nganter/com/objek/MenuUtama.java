package com.nganter.com.objek;

/**
 * Created by Septiawan Aji Pradan on 6/14/2017.
 */

public class MenuUtama {
    private String nama;
    private String pathFoto;
    private int drawabel;

    public MenuUtama(String nama,int drawabel){
        this.nama = nama;
        this.drawabel = drawabel;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public int getDrawabel() {
        return drawabel;
    }

    public void setDrawabel(int drawabel) {
        this.drawabel = drawabel;
    }
}
