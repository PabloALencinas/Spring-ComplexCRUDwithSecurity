package com.pabloagustin.springbootdatajpa.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;

    // Mapear ITEMFACTURA con PRODUCTO -> Relacion: MUCHOS item_factura -> 1(Un) Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id") // Llave foranea en la tabla 'facturas_item' es, justamente, producto_id
    private Producto producto;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // METODOS
    // Calcular importe
    public Double calcularImporte(){
        return cantidad.doubleValue() * producto.getPrecio();
    }

    // Serializable
    private static final long serialVersionUID = 1L;
}
