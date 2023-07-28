package com.pabloagustin.springbootdatajpa.dao;

import com.pabloagustin.springbootdatajpa.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    // Podemos usar @Query para la consulta personalizada
    public Usuario findByUsername(String username);

}
