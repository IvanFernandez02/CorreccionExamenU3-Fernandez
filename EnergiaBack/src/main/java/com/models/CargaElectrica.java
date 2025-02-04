package com.models;

public class CargaElectrica {
    private Integer id;
    private String carga;
    private Double y;
    private Double x;

    public CargaElectrica(Integer id, String carga, Double x, Double y) {
        this.id = id;
        this.carga = carga;
        this.y = y;
        this.x = x;
    }

    public Integer getid() {
        return this.id;
    }

    public void setid(Integer idCargaElectrica) {
        this.id = idCargaElectrica;
    }

    public String getCarga() {
        return this.carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public CargaElectrica() {
    }
} 