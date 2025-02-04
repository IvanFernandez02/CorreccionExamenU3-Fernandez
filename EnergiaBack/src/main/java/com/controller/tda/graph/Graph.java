package com.controller.tda.graph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.controller.Dao.CargaElectricaDao;
import com.controller.tda.list.LinkedList;
import com.controller.tda.list.ListEmptyException;
import com.models.CargaElectrica;

import static java.lang.Math.*;

public abstract class Graph {

    // Ruta para guardar el archivo
    public static String filePath = "data/";
    private Map<Integer, CargaElectrica> vertexModels = new HashMap<>();
    public abstract Integer nro_vertices();
    public abstract Integer nro_edges();
    public abstract Boolean is_edges(Integer v1, Integer v2) throws Exception;
    public abstract Float wieght_edge(Integer v1, Integer v2) throws Exception;
    public abstract void add_edge(Integer v1, Integer v2) throws Exception;
    public abstract void add_edge(Integer v1, Integer v2, Float weight) throws Exception;
    public abstract LinkedList<Adyecencia> adyecencias(Integer v1);

    // Método para obtener el ID de un vértice
    public Integer getVertex(Integer label) throws Exception {
        return label;
    }

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder();
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo.append("Vertice: ").append(i).append("\n");
                LinkedList<Adyecencia> lista = this.adyecencias(i);
                if (!lista.isEmpty()) {
                    Adyecencia[] ady = lista.toArray();
                    for (Adyecencia a : ady) {
                        grafo.append("ady: V").append(a.getdestination())
                                .append(" weight: ").append(a.getweight()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grafo.toString();
    }

    public void saveGraphLabel(String filename) throws Exception {
        JsonArray graphArray = new JsonArray();
        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject vertexObject = new JsonObject();
            vertexObject.addProperty("labelId", this.getVertex(i));

            JsonArray destinationsArray = new JsonArray();
            LinkedList<Adyecencia> adyacencias = this.adyecencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adj = adyacencias.get(j);
                    JsonObject destinationObject = new JsonObject();
                    destinationObject.addProperty("from", this.getVertex(i));
                    destinationObject.addProperty("to", adj.getdestination());
                    destinationsArray.add(destinationObject);
                }
            }
            vertexObject.add("destinations", destinationsArray);
            graphArray.add(vertexObject);
        }

        Gson gson = new Gson();
        String json = gson.toJson(graphArray);

        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(filePath + filename)) {
            fileWriter.write(json);
        }
    }

    public void cargarModelosDesdeDao() throws ListEmptyException {
        CargaElectricaDao cargaElectricaDao = new CargaElectricaDao();
        LinkedList<CargaElectrica> cargaElectricaList = cargaElectricaDao.getListAll();

        for (int i = 0; i < cargaElectricaList.getSize(); i++) {
            CargaElectrica cargaElectrica = cargaElectricaList.get(i);
            vertexModels.put(cargaElectrica.getid(), cargaElectrica);
        }
    }

    public void loadGraph(String filename) throws Exception {

        try (FileReader fileReader = new FileReader(filePath + filename)) {
            Gson gson = new Gson();
            JsonArray graphArray = gson.fromJson(fileReader, JsonArray.class);

            for (JsonElement vertexElement : graphArray) {
                JsonObject vertexObject = vertexElement.getAsJsonObject();

                Integer labelId = vertexObject.get("labelId").getAsInt();

                CargaElectrica model = vertexModels.get(labelId);

                if (model == null) {
                    continue;
                }

                this.addVertexWithModel(labelId, model);

                JsonArray destinationsArray = vertexObject.getAsJsonArray("destinations");

                for (JsonElement destinationElement : destinationsArray) {
                    JsonObject destinationObject = destinationElement.getAsJsonObject();

                    Integer from = destinationObject.get("from").getAsInt();
                    Integer to = destinationObject.get("to").getAsInt();

                    CargaElectrica modelFrom = vertexModels.get(from);
                    CargaElectrica modelTo = vertexModels.get(to);

                    if (modelFrom == null || modelTo == null) {
                    } else {

                        Float weight = (float) calcularDistancia(modelFrom, modelTo);

                        this.add_edge(from, to, weight);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Borrar Adyacencias para crear nuevas
    public void clearEdges() {
        for (int i = 1; i <= this.nro_vertices(); i++) {
            this.adyecencias(i).reset();
        }
    }

    // Crear las nuevas adyacencias de manera aleatoria
    public void loadGraphWithRandomEdges(String filename) throws Exception {
        loadGraph(filename);
        cargarModelosDesdeDao();
        clearEdges();
        Random random = new Random();
        for (int i = 1; i <= this.nro_vertices(); i++) {
            LinkedList<Adyecencia> existingEdges = this.adyecencias(i);
            int connectionsCount = existingEdges.getSize();

            while (connectionsCount < 3) {
                // Generamos un vértice aleatorio
                int randomVertex = random.nextInt(this.nro_vertices()) + 1;
                while (randomVertex == i || is_edges(i, randomVertex)) {
                    randomVertex = random.nextInt(this.nro_vertices()) + 1;
                }
                CargaElectrica modelFrom = vertexModels.get(i);
                CargaElectrica modelTo = vertexModels.get(randomVertex);

                float weight = (float) calcularDistancia(modelFrom, modelTo);
                add_edge(i, randomVertex, weight);
                connectionsCount++;
            }
        }
        saveGraphLabel(filename);
    }

    public void addVertexWithModel(Integer vertexId, CargaElectrica model) {
        vertexModels.put(vertexId, model);
    }

    public JsonArray obtainWeights() throws Exception {
        JsonArray result = new JsonArray();

        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject vertexInfo = new JsonObject();
            CargaElectrica model = vertexModels.get(i);
            if (model != null) {
                vertexInfo.addProperty("carga", model.getCarga());
            }
            vertexInfo.addProperty("labelId", this.getVertex(i));
            JsonArray destinations = new JsonArray();
            LinkedList<Adyecencia> adyacencias = this.adyecencias(i);

            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adj = adyacencias.get(j);
                    JsonObject destinationInfo = new JsonObject();
                    destinationInfo.addProperty("from", this.getVertex(i));
                    destinationInfo.addProperty("to", adj.getdestination());
                    destinationInfo.addProperty("weight", adj.getweight());
                    destinations.add(destinationInfo);
                }
            }

            vertexInfo.add("destinations", destinations);
            result.add(vertexInfo);
        }

        return result;
    }

    public boolean existsFile(String filename) {
        File file = new File(filePath + filename);
        return file.exists();
    }







                        /*meto para calcular distancia entre las cargas electricas*/
    public static double calcularDistancia(CargaElectrica carga1, CargaElectrica carga2) {
        if (carga1.getY() == null || carga1.getX() == null ||
            carga2.getY() == null || carga2.getX() == null) {
            return Double.NaN;
        }
        
        double dx = carga2.getX() - carga1.getX();
        double dy = carga2.getY() - carga1.getY();
        
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Método principal para guardar el grafo
    public void guardarGrafo() {
        try {
            String filename = "CargaElectricagrafo.json";
            saveGraphLabel(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // Método para obtener los datos del grafo en formato Vis.js
    public JsonObject getVisGraphData() throws Exception {
        JsonObject visGraph = new JsonObject();

        JsonArray nodes = new JsonArray();
        JsonArray edges = new JsonArray();

        for (int i = 1; i <= this.nro_vertices(); i++) {
            JsonObject node = new JsonObject();
            CargaElectrica model = vertexModels.get(i);
            if (model != null) {
                node.addProperty("carga", model.getCarga());
            }
            node.addProperty("id", i);
            node.addProperty("label", "V" + i);

            node.addProperty("color", "#EA7913"); // Color de los nodos Naranjas
            nodes.add(node);

            LinkedList<Adyecencia> adyacencias = this.adyecencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adj = adyacencias.get(j);
                    JsonObject edge = new JsonObject();
                    edge.addProperty("from", i);
                    edge.addProperty("to", adj.getdestination());
                    edge.addProperty("weight", adj.getweight());

                    edge.addProperty("color", "#3B83BD"); // Color de las aristas Azules
                    edges.add(edge);
                }
            }
        }

        // Añadir nodos y aristas al objeto principal
        visGraph.add("nodes", nodes);
        visGraph.add("edges", edges);

        return visGraph;
    }

    protected abstract Float getWeight2(int nodo, int i);







    

                                                                        // HEURISTICAS
    private static final double K = 9e9; 
    private static final double VELOCIDAD = 284000.0; // km/h

    public double calcularEnergia(CargaElectrica carga1, CargaElectrica carga2) {
        double distancia = calcularDistancia(carga1, carga2);
        double q1 = Double.parseDouble(carga1.getCarga());
        double q2 = Double.parseDouble(carga2.getCarga());
        return K * (Math.abs(q1 * q2)) / Math.pow(distancia * 1000, 2); // Convertir distancia a metros
    }

    public double calcularTiempoRecorrido(double distancia) {
        return distancia / VELOCIDAD;
    }

    public String calcularRutaEnergia(Integer inicio, Integer fin) throws Exception {
        if (!vertexModels.containsKey(inicio) || !vertexModels.containsKey(fin)) {
            throw new Exception("Vértices no válidos");
        }

        int n = nro_vertices();
        double[][] energias = new double[n][n];
        int[][] siguiente = new int[n][n];


        // Inicializar matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                energias[i][j] = Double.POSITIVE_INFINITY;
                siguiente[i][j] = -1;
            }
        }

        // Llenar la matriz de energías con las conexiones existentes
        for (int i = 1; i <= n; i++) {
            LinkedList<Adyecencia> adyacencias = adyecencias(i);
            for (int j = 0; j < adyacencias.getSize(); j++) {
                Adyecencia adj = adyacencias.get(j);
                CargaElectrica carga1 = vertexModels.get(i);
                CargaElectrica carga2 = vertexModels.get(adj.getdestination());
                if (carga1 != null && carga2 != null) {
                    energias[i-1][adj.getdestination()-1] = calcularEnergia(carga1, carga2);
                    siguiente[i-1][adj.getdestination()-1] = adj.getdestination()-1;
                }
            }
            energias[i-1][i-1] = 0;
            siguiente[i-1][i-1] = i-1;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (energias[i][k] != Double.POSITIVE_INFINITY && 
                        energias[k][j] != Double.POSITIVE_INFINITY) {
                        double nuevaEnergia = energias[i][k] + energias[k][j];
                        if (nuevaEnergia < energias[i][j]) {
                            energias[i][j] = nuevaEnergia;
                            siguiente[i][j] = siguiente[i][k];
                        }
                    }
                }
            }
        }

        if (energias[inicio-1][fin-1] == Double.POSITIVE_INFINITY) {
            return "ERROR: Estos puntos no estan conectados";
        }

        LinkedList<Integer> camino = new LinkedList<>();
        int actual = inicio-1;
        while (actual != fin-1) {
            if (actual == -1 || camino.getSize() > n) {
                return "Error: No se puede completar la ruta";
            }
            camino.add(actual + 1);
            actual = siguiente[actual][fin-1];
        }
        camino.add(fin);

        // Construir el resultado
        StringBuilder path = new StringBuilder();
        StringBuilder result = new StringBuilder();
        double energiaTotal = energias[inicio-1][fin-1];
        double distanciaTotal = 0;

        for (int i = 0; i < camino.getSize() - 1; i++) {
            CargaElectrica cargaActual = vertexModels.get(camino.get(i));
            CargaElectrica cargaSiguiente = vertexModels.get(camino.get(i + 1));
            
            path.append(cargaActual.getCarga());
            path.append(" -> ");
            
            if (i == camino.getSize() - 2) {
                path.append(cargaSiguiente.getCarga());
            }
            
            distanciaTotal += calcularDistancia(cargaActual, cargaSiguiente);
        }

        double tiempoHoras = calcularTiempoRecorrido(distanciaTotal);
        
        result.append("Ruta más eficiente: ").append(path.toString()).append("\n");
        result.append("Energía total requerida: ").append(String.format("%d", Math.round(energiaTotal))).append(" Joules\n");
        result.append("Distancia total: ").append(String.format("%d", Math.round(distanciaTotal * 100))).append(" unidades\n");
        result.append("Tiempo estimado de recorrido: ").append((distanciaTotal * 100)*VELOCIDAD).append(" horas\n");

        return result.toString();
    }

}
