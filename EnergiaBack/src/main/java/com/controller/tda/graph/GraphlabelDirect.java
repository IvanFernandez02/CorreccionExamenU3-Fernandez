package com.controller.tda.graph;

import java.lang.reflect.Array;
import java.util.HashMap;

import com.controller.tda.list.LinkedList;

public class GraphlabelDirect<E> extends GraphDirect {
    protected E labels[];
    protected HashMap<E, Integer> dictVertices;
    private Class<E> clazz;

    public GraphlabelDirect(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices);
        this.clazz = clazz;
        labels = (E[]) Array.newInstance(clazz, nro_vertices + 1);
        dictVertices = new HashMap<>();
    }

    public Boolean is_edgeL(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
            return is_edges(getVerticeL(v1), getVerticeL(v2));
        } else {
            throw new Exception("Grafo no etiquetado");
        }
    }
    

    public void insertEdgeL(E v1, E v2, Float wiegth) throws Exception {
        if (isLabelsGraph()) {
            add_edge(getVerticeL(v1), getVerticeL(v2), wiegth);

        } else {
            throw new Exception("Grafo no etiquetado");
        }
    }

    public void insertEdgeL(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
             insertEdgeL(v1, v2, Float.NaN);
            //System.out.println(getVerticeL(v1)+"___"+getVerticeL(v2));	
            //add_edge(getVerticeL(v1), getVerticeL(v2), Float.NaN);
        } else {
            throw new Exception("Grafo no etiquetado");
        }
    }

    public LinkedList<Adyecencia> adycenciasL(E v) throws Exception{
        if (isLabelsGraph()) {
            return adyecencias(getVerticeL(v));
        } else {
            throw new Exception("Grafo no etiquetado");
        }
    }

    public void labelsVertices(Integer v, E data) {
        labels[v] = data;
        dictVertices.put(data, v);
    }

    public Boolean isLabelsGraph() {
        Boolean band = true;
        for (int i = 1; i < labels.length; i++) {
            if (labels[i] == null) {
                band = false;
                break;
            }
        }
        return band;
    }
    
    public Integer getVerticeL(E label) {
        return dictVertices.get(label);
    }
    
    public E getLabelL(Integer v1) {
        return labels[v1];
    }

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder();
        try {
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo.append("V").append(i).append(" [")
                        .append(getLabelL(i).toString()).append("]")
                        .append("\n");
                LinkedList<Adyecencia> lista = adyecencias(i);
                if (!lista.isEmpty()) {
                    Adyecencia[] ady = lista.toArray();
                    for (Adyecencia a : ady) {
                        grafo.append("ady V").append(a.getdestination())
                                .append(" weight: ").append(a.getweight()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            grafo.append("Error: ").append(e.getMessage());
        }
        return grafo.toString();
    }

    public String drawGraph() {
        StringBuilder grafo = new StringBuilder();

        grafo.append("digraph G {\n");
        try {
            grafo.append("digraph G {\n");
            for (int i = 1; i <= this.nro_vertices(); i++) {
                grafo.append("V").append(i).append(" [")
                        .append("label=\"").append(getLabelL(i).toString()).append("\"]")
                        .append("\n");
                LinkedList<Adyecencia> lista = adyecencias(i);
                if (!lista.isEmpty()) {
                    Adyecencia[] ady = lista.toArray();
                    for (Adyecencia a : ady) {
                        grafo.append("V").append(i).append(" -> V").append(a.getdestination())
                                .append(" [label=\"").append(a.getweight()).append("\"]\n");
                    }
                }
            }
            grafo.append("}");
        } catch (Exception e) {
            grafo.append("Error: ").append(e.getMessage());
        }
        return grafo.toString();
    }
}