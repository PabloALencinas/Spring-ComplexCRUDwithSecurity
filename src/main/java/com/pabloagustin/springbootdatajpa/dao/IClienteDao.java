package com.pabloagustin.springbootdatajpa.dao;

import com.pabloagustin.springbootdatajpa.models.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.Optional;

// Interfaz implementada con CrudRepository by Spring JPA
// <Class, Data> -> Clase entity (Cliente) y el Dato del id (Long)
// No estamos anotando con Service o Component pero si en ClienteServiceImplementation
// y mediante la inyeccion de esta interfaz lo hacemos alli
// Ya es un componente de Spring, NO HACE FALTA AGREGAR DICHAS ANOTACIONES porque hereda de CRUDREPOSITORY!
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>,CrudRepository<Cliente, Long> {

}
