package com.pabloagustin.springbootdatajpa.service;

import com.pabloagustin.springbootdatajpa.models.Cliente;
import com.pabloagustin.springbootdatajpa.models.Factura;
import com.pabloagustin.springbootdatajpa.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);


    public void save(Cliente cliente);

    // Para editar clientes
    public Cliente findOne(Long id);

    // Para eliminar clientes
    public void delete(Long id);

    // Metodo para el service de Producto
    public List<Producto> findByNombre(String term);

    // Metodo para guardar factura
    public void saveFactura(Factura factura);

    // Buscar producto por ID
    public Producto findProductoById(Long id);

    // Buscar factura por ID
    public Factura findFacturaById(Long id);

    // Eliminar factura
    public void deleteFactura(Long id);
}

