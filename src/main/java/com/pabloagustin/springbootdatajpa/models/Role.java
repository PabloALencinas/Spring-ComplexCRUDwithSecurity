package com.pabloagustin.springbootdatajpa.models;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
// Creamos en la tabla, el indice, la llave que indique que el campo 'authority' y 'user_id' son UNICOS
@Table(name = "authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "authority"})})
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    // Serializable
    private static final long serialVersionUID = 1L;
}
