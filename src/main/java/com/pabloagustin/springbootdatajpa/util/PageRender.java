package com.pabloagustin.springbootdatajpa.util;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageRender<T> {

    private String url;

    //Lista de clientes
    private Page<T> page;

    private int totalPaginas;

    private int numElementosPorPagina;

    private int paginaActual;

    private List<PageItem> paginas;

    // Constructors
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<PageItem>();

        // Relacionado con el '5' dado en el size del controlador para la paginacion
        numElementosPorPagina = page.getSize();
        // Obteniendo el total de paginas
        totalPaginas = page.getTotalPages();

        // Pagina actual
        paginaActual = page.getNumber() + 1;

        // Realizamos el calculo, desde y hasta para dibujar el paginador en nuestra vista
        int desde, hasta;
        if(totalPaginas <= numElementosPorPagina){
            desde = 1;
            hasta = totalPaginas;
        } else {
            if (paginaActual <= numElementosPorPagina/2){
                desde = 1;
                hasta = numElementosPorPagina;
            } else if(paginaActual >= totalPaginas - numElementosPorPagina/2){
                desde = totalPaginas - numElementosPorPagina + 1;
                hasta = numElementosPorPagina;
            } else {
                desde = paginaActual - numElementosPorPagina/2;
                hasta = numElementosPorPagina;
            }
        }

        // Recorremos el hasta y empezamos a llenar la pagina
        for (int i = 0; i < hasta; i++){
            paginas.add(new PageItem(desde + i, paginaActual == desde + i));
        }
    }

    // Getters para poder acceder desde la vista

    public String getUrl() {
        return url;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    public boolean isFirst(){
        return page.isFirst();
    }

    public boolean isLast(){
        return page.isLast();
    }

    public boolean isHasNext(){
        return page.hasNext();
    }

    public boolean isHasPrevious(){
        return page.hasPrevious();
    }
}
