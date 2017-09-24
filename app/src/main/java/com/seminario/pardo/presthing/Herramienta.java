package com.seminario.pardo.presthing;

/**
 * Created by Maxi on 31/08/2017.
 */
public class Herramienta {

    private int id;
    private String nombre_herramienta;
    private String desc_herramienta;
    private byte [] img_herramienta;
    private int estado; //Agregado

    public Herramienta(byte[] img_herramienta, int id, String nombre_herramienta, String desc_herramienta, int estado) {
        this.img_herramienta = img_herramienta;
        this.id = id;
        this.nombre_herramienta = nombre_herramienta;
        this.desc_herramienta = desc_herramienta;
        this.estado = estado; //Agregado para cuando este prestado y setear internamente
    }

    //Agregado
    public void setEstado(int estado){this.estado=estado;}

    public int getEstado(){return estado;}
    //Fin agregado

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_herramienta() {
        return nombre_herramienta;
    }

    public void setNombre_herramienta(String nombre_herramienta) {
        this.nombre_herramienta = nombre_herramienta;
    }

    public String getDesc_herramienta() {
        return desc_herramienta;
    }

    public void setDesc_herramienta(String desc_herramienta) {
        this.desc_herramienta = desc_herramienta;
    }

    public byte[] getImg_herramienta() {
        return img_herramienta;
    }

    public void setImg_herramienta(byte[] img_herramienta) {
        this.img_herramienta = img_herramienta;
    }
}
