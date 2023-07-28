package com.pabloagustin.springbootdatajpa.service;

import com.pabloagustin.springbootdatajpa.dao.IClienteDao;
import com.pabloagustin.springbootdatajpa.dao.IFacturaDao;
import com.pabloagustin.springbootdatajpa.dao.IProductoDao;
import com.pabloagustin.springbootdatajpa.models.Cliente;
import com.pabloagustin.springbootdatajpa.models.Factura;
import com.pabloagustin.springbootdatajpa.models.Producto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// Es una fachada!
@Service
public class ClienteServiceImplementation implements IClienteService{

    // Tenemos un solo DAO pero podriamos tener muchos mas
    // Y aqui dentro del service accedemos a los distintos DAO's como un unico punto de acceso
    @Autowired
    private IClienteDao clienteDao;

    //  Inyectamos el service de Producto a la 'fachada'
    @Autowired
    private IProductoDao productoDao;

    // Inyectamos el componente DAO de factura para hacer el save de factura al cliente
    @Autowired
    private IFacturaDao facturaDao;

    @Override
    @Transactional
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }


    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public Cliente findOne(Long id) {
        return clienteDao.findById(id).orElse(null) ;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    public List<Producto> findByNombre(String term) {
        return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    @Transactional
    public Producto findProductoById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    public void deleteFactura(Long id) {
        facturaDao.deleteById(id);
    }
}
