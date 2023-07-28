package com.pabloagustin.springbootdatajpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.rmi.registry.Registry;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Sobreescribimos el siguiente metodo para agregar RECURSOS a nuestro proyecto
    // Para el manejo de las fotos de los clientes fuera del proyecto
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        // Registrar nuestra ruta como recurso estatico
        // Mapeamos este directorio de imagenes a una ruta URL
        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath);
    }
     */

    // Implementacion del metodo para registrar un controlador de vista
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/error_403").setViewName("error_403");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
