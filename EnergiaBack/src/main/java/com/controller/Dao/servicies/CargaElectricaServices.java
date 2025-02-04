package com.controller.Dao.servicies;

import com.controller.Dao.CargaElectricaDao;
import com.controller.tda.list.LinkedList;
import com.models.CargaElectrica;

public class CargaElectricaServices {
    private CargaElectricaDao obj;

    public CargaElectricaServices() {
        obj = new CargaElectricaDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public LinkedList<CargaElectrica> listAll() {
        return obj.getListAll();
    }

    public CargaElectrica getCargaElectrica() {
        return obj.getCargaElectrica();
    }

    public void setCargaElectrica(CargaElectrica cargaElectrica) {
        obj.setCargaElectrica(cargaElectrica);
    }

    public CargaElectrica get(Integer id) throws Exception {
        return obj.get(id);
    }

    public Boolean delete(Integer id) throws Exception {
        return obj.delete(id);
    }

    public String calcularCaminoCorto(int inicio, int fin, int algoritmo) throws Exception {
        return obj.caminoCorto(inicio, fin, algoritmo);
    }
} 