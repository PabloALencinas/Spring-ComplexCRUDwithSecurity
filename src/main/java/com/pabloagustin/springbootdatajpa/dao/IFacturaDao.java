package com.pabloagustin.springbootdatajpa.dao;

import com.pabloagustin.springbootdatajpa.models.Factura;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
}
