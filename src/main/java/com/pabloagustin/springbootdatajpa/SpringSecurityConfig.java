package com.pabloagustin.springbootdatajpa;



import com.pabloagustin.springbootdatajpa.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.spring6.expression.Mvc;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig{

    // Inyectamos la dependencia del LoginSuccessHandler para el msj de success
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    // Inyectamos el mvcConfig para poder hacer uso del metodo passwordEnconder
    // Podria ser tambien
    // @Autowired
    // private BCryptPasswordEnconder passwordEncoder;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LoginSuccessHandler sucessHandler;

    @Autowired
    private DataSource dataSource;

    //    // Metodo para manera la configuracion global del mecanismo de autenticacion
    //    // Este metodo nos permite definir como los usarios se van a autenticar en la app
    //    @Autowired
    //    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
    //
    //        // Password Encoder
    //        PasswordEncoder encoder = passwordEncoder();
    //
    //        // Para crear los usuarios y encryptar sus password
    //        // Por cada usuario que registremos, se genera un evento que encrypta la password
    //        User.UserBuilder users = User.builder().passwordEncoder(encoder::encode);
    //
    //        // Creamos los usuarios en memoria
    //        builder.inMemoryAuthentication()
    //                .withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
    //                .withUser(users.username("pablo").password("12345").roles("USER"));
    //
    //    }


    // El codigo de arriba fue ACTUALIZADO al cliente para spring boot 2.6 o superior!
    // Agregando usuarios EN MEMORIA para la prueba de autenticacion para la aplicacion
//    @Bean
//    public UserDetailsService userDetailsService() throws Exception{
//
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User
//                .withUsername("user")
//                .password(passwordEncoder.encode("user"))
//                .roles("USER")
//                .build());
//        manager.createUser(User
//                .withUsername("admin")
//                .password(passwordEncoder.encode("admin"))
//                .roles("ADMIN","USER")
//                .build());
//
//        return manager;
//    }


    // Nuevo metodo para las autorizacion HTTP, dar seguridad a nuestra aplicacion
    // FILTROS! intercepta y valida los permisos para mostrar la data
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/form/**","/eliminar/**","/factura/**")
                .hasRole("ADMIN")
                .requestMatchers("/ver/**")
                .hasRole("USER")
                .requestMatchers("")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .successHandler(loginSuccessHandler)
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403");

        return http.build();
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?")
                .and().build();
    }

}
