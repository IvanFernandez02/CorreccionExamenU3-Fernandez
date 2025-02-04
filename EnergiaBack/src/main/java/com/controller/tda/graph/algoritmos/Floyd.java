package com.controller.tda.graph.algoritmos;

import com.controller.tda.graph.GraphLabelNoDirect;

public class Floyd {
    private GraphLabelNoDirect<String> grafo;
    private int inicio;
    private int fin;
    private float[][] distancias;
    private int[][] siguiente;

    public Floyd(GraphLabelNoDirect<String> grafo, int inicio, int fin) {
        this.grafo = grafo;
        this.inicio = inicio;
        this.fin = fin;
        int n = grafo.nro_vertices();
        this.distancias = new float[n + 1][n + 1];
        this.siguiente = new int[n + 1][n + 1];
    }

    public String caminoCorto() throws Exception {
        int n = grafo.nro_vertices();

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                    siguiente[i][j] = -1;
                } else {
                    try {
                        float peso = grafo.getWeigth2(i, j);
                        if (Float.isNaN(peso) || peso <= 0) {
                            distancias[i][j] = Float.MAX_VALUE;
                            siguiente[i][j] = -1;
                        } else {
                            distancias[i][j] = peso;
                            siguiente[i][j] = j;
                        }
                    } catch (Exception e) {
                        distancias[i][j] = Float.MAX_VALUE;
                        siguiente[i][j] = -1;
                    }
                }
            }
        }

        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (distancias[i][k] != Float.MAX_VALUE && distancias[k][j] != Float.MAX_VALUE &&
                        distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }

        return reconstruirCamino(inicio, fin);
    }

    private String reconstruirCamino(int inicio, int fin) {
        if (siguiente[inicio][fin] == -1) {
            return "No hay camino";
        }

        StringBuilder camino = new StringBuilder();
        int actual = inicio;
        float distanciaTotal = 0; 

        while (actual != fin) {
            if (siguiente[actual][fin] == -1) {
                return "Error";
            }
            camino.append(actual).append(" -> ");
            distanciaTotal += distancias[actual][siguiente[actual][fin]];
            actual = siguiente[actual][fin];
        }
        camino.append(fin);
        distanciaTotal += distancias[actual][fin];

        System.out.println("Distancia total recorrida: " + distanciaTotal);

        return "Camino: " + camino.toString() + "\nDistancia total: " + distanciaTotal;
    }
}
