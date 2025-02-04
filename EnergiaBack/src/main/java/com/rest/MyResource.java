package com.rest;

import java.util.LinkedList;
import java.util.Arrays;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/myresource")
public class MyResource {

    // Implementación del algoritmo de Floyd-Warshall
    public static void floydWarshall(int[][] graph) {
        int V = graph.length;
        int[][] dist = new int[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
    }

    // Implementación del algoritmo de Bellman-Ford
    public static void bellmanFord(LinkedList<int[]> edges, int V, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int i = 1; i < V; i++) {
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                int weight = edge[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                }
            }
        }
    }

    // Método para medir el tiempo de ejecución de los algoritmos
    public static String measureTime(int[][] graph, LinkedList<int[]> edges, int V) {
        long startTime, endTime;

        // Medir tiempo para Floyd-Warshall
        startTime = System.nanoTime();
        floydWarshall(graph);
        endTime = System.nanoTime();
        long floydTime = endTime - startTime;

        // Medir tiempo para Bellman-Ford
        startTime = System.nanoTime();
        bellmanFord(edges, V, 0);
        endTime = System.nanoTime();
        long bellmanTime = endTime - startTime;

        return "Tiempo con Algoritmo de Floyd: " + floydTime + " ns\n" +
               "Tiempo con Algoritmo de Bellman: " + bellmanTime + " ns";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        // Crear grafos de diferentes tamaños
        int[][] graph10 = new int[10][10];
        int[][] graph20 = new int[20][20];
        int[][] graph30 = new int[30][30];

        LinkedList<int[]> edges10 = new LinkedList<>();
        LinkedList<int[]> edges20 = new LinkedList<>();
        LinkedList<int[]> edges30 = new LinkedList<>();

        // Inicializar los grafos con valores aleatorios
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                graph10[i][j] = (int) (Math.random() * 10);
                if (graph10[i][j] != 0) {
                    edges10.add(new int[]{i, j, graph10[i][j]});
                }
            }
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                graph20[i][j] = (int) (Math.random() * 10);
                if (graph20[i][j] != 0) {
                    edges20.add(new int[]{i, j, graph20[i][j]});
                }
            }
        }

        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                graph30[i][j] = (int) (Math.random() * 10);
                if (graph30[i][j] != 0) {
                    edges30.add(new int[]{i, j, graph30[i][j]});
                }
            }
        }

        // Medir tiempos y mostrar resultados
        StringBuilder result = new StringBuilder();
        result.append("\n**********Resultados para 10 datos**********\n");
        result.append(measureTime(graph10, edges10, 10)).append("\n");

        result.append("\n**********Resultados para 20 datos**********\n");
        result.append(measureTime(graph20, edges20, 20)).append("\n");

        result.append("\n**********Resultados para 30 datos**********\n");
        result.append(measureTime(graph30, edges30, 30)).append("\n");

        String resultString = result.toString();
        System.out.println(resultString); // Imprime los resultados en la consola

        return resultString;
    }
}
