package com.controller.tda.graph.algoritmos;
import com.controller.tda.graph.GraphLabelNoDirect;
import com.controller.tda.graph.Adyecencia;
import com.controller.tda.list.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class BellmanFord {
    private GraphLabelNoDirect<String> grafo;
    private int inicio;
    private int fin;
    private Map<Integer, Float> distancias;
    private Map<Integer, Integer> padres;

    public BellmanFord(GraphLabelNoDirect<String> grafo, int inicio, int fin) {
        this.grafo = grafo;
        this.inicio = inicio;
        this.fin = fin;
        this.distancias = new HashMap<>();
        this.padres = new HashMap<>();
    }

    public String caminoCorto() throws Exception {
        int n = grafo.nro_vertices();

        for (int i = 1; i <= n; i++) {
            distancias.put(i, Float.MAX_VALUE);
            padres.put(i, -1);
        }
        distancias.put(inicio, 0f);

        for (int i = 1; i < n; i++) {
            for (int u = 1; u <= n; u++) {
                LinkedList<Adyecencia> adyacencias = grafo.adyecencias(u);
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyecencia adyacencia = adyacencias.get(j);
                    int v = adyacencia.getdestination();
                    float peso = adyacencia.getweight();
                    
                    if (distancias.get(u) != Float.MAX_VALUE && distancias.get(u) + peso < distancias.get(v)) {
                        distancias.put(v, distancias.get(u) + peso);
                        padres.put(v, u);
                    }
                }
            }
        }

        for (int u = 1; u <= n; u++) {
            LinkedList<Adyecencia> adyacencias = grafo.adyecencias(u);
            for (int j = 0; j < adyacencias.getSize(); j++) {
                Adyecencia adyacencia = adyacencias.get(j);
                int v = adyacencia.getdestination();
                float peso = adyacencia.getweight();
                if (distancias.get(u) != Float.MAX_VALUE && distancias.get(u) + peso < distancias.get(v)) {
                    return "Grafo con ciclo negativo";
                }
            }
        }

        return reconstruirCamino(inicio, fin);
    }

    private String reconstruirCamino(int inicio, int fin) throws Exception {
        if (distancias.get(fin) == Float.MAX_VALUE) {
            return "No hay camino";
        }

        StringBuilder camino = new StringBuilder();
        int actual = fin;
        float distanciaTotal = 0;

        while (actual != -1) {
            if (padres.get(actual) != -1) {
                distanciaTotal += grafo.getWeigth2(padres.get(actual), actual);
            }
            camino.insert(0, actual + " -> ");
            actual = padres.get(actual);
        }
        camino.delete(camino.length() - 4, camino.length());

        System.out.println("Distancia total recorrida: " + distanciaTotal);
        return "Camino: " + camino.toString() + "\nDistancia total: " + distanciaTotal;
    }
}
