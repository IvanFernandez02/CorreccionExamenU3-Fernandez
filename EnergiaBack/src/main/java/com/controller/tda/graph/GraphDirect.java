package com.controller.tda.graph;
import com.controller.tda.list.LinkedList;
import com.controller.tda.list.ListEmptyException;

public class GraphDirect  extends Graph{
    private Integer nro_vertices;
    private Integer nro_edges;

    private LinkedList<Adyecencia> listaAdyecencia[];

        @SuppressWarnings("unchecked")
    public GraphDirect(Integer nro_vertices){
        this.nro_vertices = nro_vertices;
        this.nro_edges = 0;
        listaAdyecencia = new LinkedList[nro_vertices + 1];
        for (int i = 0; i <= nro_vertices; i++) {
            listaAdyecencia[i] = new LinkedList<Adyecencia>();
        }
    }

    //nro_edges son de la clase graph 
    
    public Integer nro_edges() {
        return this.nro_edges;
    }

    public Integer nro_vertices() {
        return this.nro_vertices;
    }
    //si los vertices esta en la parte correcta sacamos las listas de adyecencias
    @Override
    public Boolean is_edges(Integer v1, Integer v2) throws Exception {
        Boolean is = false;
        if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
            LinkedList<Adyecencia> lista = listaAdyecencia[v1];
            if (!lista.isEmpty()) {
                Adyecencia[] matrix = lista.toArray();
                for (int i = 0; i < matrix.length; i++) {
                    Adyecencia aux = matrix[i];
                    if (aux.getdestination().intValue() == v2.intValue()) {
                        is = true;
                        break;
                    }
                }
            }
        } else {
            throw new  ListEmptyException("El vertice no existe");
        }
        return is;
        
    }

    public Float wieght_edge(Integer v1, Integer v2) throws Exception {
        Float weight = Float.NaN;
        if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
            LinkedList<Adyecencia> lista = listaAdyecencia[v1];
            if (!lista.isEmpty()) {
                Adyecencia[] matrix = lista.toArray();
                for (int i = 0; i < matrix.length; i++) {
                    Adyecencia aux = matrix[i];
                    if (aux.getdestination().intValue() == v2.intValue()) {
                        weight = aux.getweight();

                        break;
                    }
                }
            }
        } else {
            throw new  ListEmptyException("El vertice no existe");
        }
        System.out.println(weight);
        return weight;


    }

    public void add_edge(Integer v1, Integer v2, Float weight) throws Exception {
        if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
            if (!is_edges(v1, v2)) {
                nro_edges++;
                Adyecencia aux = new Adyecencia(v2, weight);
                aux.setweight(weight);
                aux.setdestination(v2);
                listaAdyecencia[v1].add(aux);
                
            }
        } else {
            throw new  ListEmptyException("El vertice no existe");
        }
        
    }

    @Override
    public void add_edge(Integer v1, Integer v2) throws Exception {
        this.add_edge(v1, v2,Float.NaN);
    }

    public LinkedList<Adyecencia> adyecencias(Integer v1) {
        return listaAdyecencia[v1];
    }

    public LinkedList<Adyecencia>[] getListaAdyecencia() {
        return this.listaAdyecencia;
    }

    public Integer setNro_edges(Integer nro_edges) {
        return this.nro_edges = nro_edges;
    }

    @Override
    protected Float getWeight2(int nodo, int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWeight2'");
    }




    
}
