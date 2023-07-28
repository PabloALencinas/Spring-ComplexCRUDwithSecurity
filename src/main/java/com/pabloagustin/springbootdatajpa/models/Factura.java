package com.pabloagustin.springbootdatajpa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String descripcion;
    private String observacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;
    @PrePersist // Justo antes de persistir la FACTURA, le asigna la fecha de creacion
    public void prePersist(){
        createAt = new Date();
    }

    // Mapear FACTURA con CLIENTES -> Relacion: MUCHAS FACTURAS -> 1(Un) Cliente
    @ManyToOne(fetch = FetchType.LAZY) // Carga PEREZOSA. A medida que se van invocando los metodos, NO todo a la vez
    private Cliente cliente;

    // Mapear FACTURA con ITEMFACTURA -> Relacion: 1(Una) FACTURA -> MUCHOS itemfactura
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id") // Llave FORANEA en la tabla factura_items. La relacion UNIDIRECCIONAL
    private List<ItemFactura> items;

    // Constructores

    public Factura() {
        this.items = new ArrayList<ItemFactura>();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    // METODOS
    public void addItemFactura(ItemFactura item){
        this.items.add(item);
    }

    public Double getTotal(){
        Double total = 0.0;
        int size = items.size();
        for (int i = 0; i < size; i++){
            total += items.get(i).calcularImporte();
        }
        return total;
    }

    // Atributo estatico para el serializable UID
    private static final long serialVersionUID = 1L;
}
