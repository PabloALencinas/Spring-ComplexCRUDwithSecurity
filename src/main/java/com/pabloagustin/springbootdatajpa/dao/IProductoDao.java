package com.pabloagustin.springbootdatajpa.dao;

import com.pabloagustin.springbootdatajpa.models.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {

    // Metodo para realizar una consulta por termino 'term'
    // WhereLike donde el texto coincide con nombres del producto
    // Query con select a nivel de objeto, no de tabla
    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> findByNombre(String term);

    public List<Producto> findByNombreLikeIgnoreCase(String term);

}
