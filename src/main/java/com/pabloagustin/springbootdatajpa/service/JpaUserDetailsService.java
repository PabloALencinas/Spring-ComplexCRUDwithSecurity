package com.pabloagustin.springbootdatajpa.service;

import com.pabloagustin.springbootdatajpa.dao.IUsuarioRepository;
import com.pabloagustin.springbootdatajpa.models.Role;
import com.pabloagustin.springbootdatajpa.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// UserDetailsService es la implementacion de Spring Security que usaremos
@Service("jpaUserDetailsService") // Registramos como componente de Spring para poder INYECTAR
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    // Logger para debug
    private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Obtenemos usuario por username
        Usuario usuario = usuarioRepository.findByUsername(username);

        // Validacion mediante logger (debug), que sucede si es null?
        if(usuario == null){
            logger.error("Error login: no existe el usuario " + username);
            throw new UsernameNotFoundException("Username " + username + "no existe en el sistema!");
        }


        // Obtener sus roles y registrar estos roles dentro de una lista GrantedAuthoriti
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        // Por cada rol del usuario, obtenemos los roles y los registramos
        for (Role role: usuario.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        // Validacion mediante logger (debug), para rol, que sucede si es null?
        if(authorities.isEmpty()) {
            logger.error("Error en el Login: Usuario '" + username + "' no tiene roles asignados!");
            throw new UsernameNotFoundException("Error en el Login: usuario '" + username + "' no tiene roles asignados!");
        }

        // Retornamos el objeto UserDetails -> Representa un usuario autenticado
        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }
}
