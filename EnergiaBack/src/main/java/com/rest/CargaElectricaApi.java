package com.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.controller.Dao.servicies.CargaElectricaServices;
import com.controller.tda.graph.GraphLabelNoDirect;
import com.controller.tda.list.LinkedList;
import com.models.CargaElectrica;
import com.controller.Dao.CargaElectricaDao;

@Path("/cargaelectrica")
public class CargaElectricaApi {
    
    @Path("/lista")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCargaElectrica() {
        HashMap<String, Object> map = new HashMap<>();
        CargaElectricaServices cs = new CargaElectricaServices();
        map.put("msg", "Lista de Cargas Eléctricas");
        map.put("data", cs.listAll().toArray());
        if (cs.listAll().isEmpty()) {
            map.put("data", new Object[]{});
        }
        return Response.ok(map).build();		
    }

    @Path("/guardar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaServices cs = new CargaElectricaServices();
            cs.getCargaElectrica().setCarga(map.get("carga").toString());
            cs.getCargaElectrica().setX(Double.parseDouble(map.get("x").toString()));
            cs.getCargaElectrica().setY(Double.parseDouble(map.get("y").toString()));
            cs.save();

            res.put("msg", "Ok");
            res.put("data", "guardado exitoso");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/lista/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCargaElectrica(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        CargaElectricaServices cs = new CargaElectricaServices();
        try {
            cs.setCargaElectrica(cs.get(id));
        } catch (Exception e) {
            
        }
        map.put("msg", "carga electrica");
        map.put("data", cs.getCargaElectrica());
        if(cs.getCargaElectrica().getid() == null){
            map.put("data", "no existen datos de cargas eléctricas");
            return Response.status(Response.Status.NOT_FOUND).entity(map).build();
        }
        return Response.ok(map).build();
    }

    @Path("/actualizar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaServices cs = new CargaElectricaServices();
            cs.getCargaElectrica().setid(Integer.parseInt(map.get("id").toString()));
            cs.getCargaElectrica().setCarga(map.get("carga").toString());
            cs.getCargaElectrica().setX(Double.parseDouble(map.get("x").toString()));
            cs.getCargaElectrica().setY(Double.parseDouble(map.get("y").toString()));
            cs.update();
            res.put("msg", "Ok");
            res.put("data", "Actualizado exitoso");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/eliminar/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaServices cs = new CargaElectricaServices();
            cs.setCargaElectrica(cs.get(id));
            cs.delete(id);
            res.put("msg", "Ok");
            res.put("data", "Eliminado exitoso");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/crear_grafo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response grafoVerAdmin() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();
            LinkedList<CargaElectrica> listaCargaElectrica = cargaElectricaDao.getListAll();         
            cargaElectricaDao.creategraph();
            cargaElectricaDao.saveGraph(); 
            res.put("msg", "Grafo generado exitosamente");
            res.put("lista", listaCargaElectrica.toArray());
            return Response.ok(res).build();   
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/adyacencias_aleatorias")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response unionesgrafos() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();
            GraphLabelNoDirect<String> graph = cargaElectricaDao.crearuniosnes();
            System.out.println(graph.toString());
            cargaElectricaDao.saveGraph();
            res.put("msg", "Grafo actualizado exitosamente");
            res.put("data", graph.toString());
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/mapadegrafos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompleteGraphData() {
        try {
            CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();
            JsonObject graph = cargaElectricaDao.getGraphData();

            if (graph == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No se pudo obtener el grafo, el objeto está vacío").build();
            }

            System.out.println("Contenido: " + graph.getAsJsonObject());
            return Response.ok(graph.toString(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/misgrafos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGraph() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();
            JsonArray graph = cargaElectricaDao.obtainWeights();
            res.put("msg", "Grafo obtenido exitosamente");
            return Response.ok(graph.toString(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/camino_corto/{inicio}/{fin}/{algoritmo}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularCaminoCorto(@PathParam("inicio") int inicio, @PathParam("destino") int destino, @PathParam("algoritmo") int algoritmo) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();    
            JsonArray graph = cargaElectricaDao.obtainWeights();   
            String resultado = cargaElectricaDao.caminoCorto(inicio, destino, algoritmo);     
            res.put("msg", "Camino corto calculado exitosamente");
            res.put("resultado", resultado);
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/busquedaanchura/{inicio}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response busquedaanchura(@PathParam("inicio") int inicio) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();
        JsonArray graph = cargaElectricaDao.obtainWeights();
        String respuesta = cargaElectricaDao.busquedaanchura(inicio);

        try {
            res.put("respuesta", respuesta);
            return Response.ok(res).build();          
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/calcular_ruta_energia/{inicio}/{fin}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularRutaEnergia(@PathParam("inicio") int inicio, @PathParam("fin") int fin) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();
            // Primero verificamos si el grafo existe y tiene datos
            if (!cargaElectricaDao.existeGrafo()) {
                res.put("error", "El grafo no existe. Por favor, cree el grafo primero desde la sección 'Mapa de Grafos'.");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }
            
            String resultado = cargaElectricaDao.calcularRutaEnergia(inicio, fin);
            res.put("resultado", resultado);
            return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            res.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
} 