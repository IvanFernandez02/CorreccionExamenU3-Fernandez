package com.controller.tda.graph.algoritmos;

import com.controller.tda.graph.GraphLabelNoDirect;
import com.controller.tda.list.LinkedList;

public class BusquedaAnchura {
    private GraphLabelNoDirect<String> grafo;
    private int inicio;

    public BusquedaAnchura(GraphLabelNoDirect<String> grafo, int inicio) {
        this.grafo = grafo;
        this.inicio = inicio;
    }

    public String recorrerGrafo() throws Exception {
        int n = grafo.nro_vertices();
        boolean[] visitados = new boolean[n + 1];
        for (int i = 0; i <= n; i++) {
            visitados[i] = false;
        }
        
        LinkedList<Integer> cola = new LinkedList<>();
        cola.add(inicio);
        visitados[inicio] = true;

        LinkedList<Integer> recorrido = new LinkedList<>();
        
        while (!cola.isEmpty()) {
            int nodoActual = cola.deleteFirst(); 
            recorrido.add(nodoActual);

            LinkedList<Integer> cercas = obtenerCercas(nodoActual);
            Integer[] cercasArray = cercas.toArray();
            if (cercasArray != null) {
                for (Integer cerca : cercasArray) {
                    if (!visitados[cerca]) {
                        visitados[cerca] = true;
                        cola.add(cerca);
                    }
                }
            }
        }

        return "RECORRIDO ANCHUURA: " + recorrido.toString();
    }

    private LinkedList<Integer> obtenerCercas(int nodo) throws Exception {
        LinkedList<Integer> cercas = new LinkedList<>();
        int n = grafo.nro_vertices();

        for (int i = 1; i <= n; i++) {
            try {
                Float peso = grafo.getWeigth2(nodo, i);
                if (peso != null && peso > 0) {
                    cercas.add(i);
                }
            } catch (Exception e) {
            }
        }

        return cercas;
    }
}