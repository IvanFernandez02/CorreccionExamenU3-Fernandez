package com.controller.tda.graph;

import com.controller.tda.list.ListEmptyException;

public class GraphNoDirect extends GraphDirect {
    public GraphNoDirect(Integer nro_vertices) {
        super(nro_vertices);
    }

    public void add_edge(Integer v1, Integer v2, Float weight) throws Exception {
        if (v1.intValue() <= nro_vertices() && v2.intValue() <= nro_vertices()) {
            if (!is_edges(v1, v2)) {
                
                setNro_edges(nro_edges() +1);
                Adyecencia aux = new Adyecencia(v2, weight);
                aux.setweight(weight);
                aux.setdestination(v2);
                getListaAdyecencia()[v1].add(aux);

                Adyecencia aux2 = new Adyecencia(v1, weight);
                aux2.setweight(weight);
                aux2.setdestination(v1);
                getListaAdyecencia()[v2].add(aux2);
            }
        } else {
            throw new  ListEmptyException("El vertice no existe");
        }
    }   
}
