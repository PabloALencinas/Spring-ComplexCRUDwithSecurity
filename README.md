# CONTINUACION DEL CURSO COMPLETO DE SPRING & SPRING BOOT

# CONTINUAMOS CON EL MISMO PROYECTO PERO CON SPRING SECURITY

# Seccion 13 - Spring Security

## SCOPE: Seguridad, restringir los datos de nuestra aplicacion aplicando roles de usuarios
## Administrar las facturas de nuestros clientes (usuarios) 
## Poder tener perfiles de usuarios a traves de ROLES y de alguna forma otorgar PRIVILEGIOS
## PRIVILEGIO ADMIN: Manejo de facturas de todos los usuarios
## USUARIO STANDARD: Restringir funcionalidades
## USUARIOS ANONIMOS: Solo pueden ver el listado de clientes.
## DAR SEGURIDAD AL FIN Y AL CABO

## 1) Breve introduccion a Spring Security

    - Framework de seguridad integrado a Spring. Ofrece lo referido a Autenticacion (Login, credenciales y passwords)
      Asi como tambien, Autorizacion (ACL -> Lista de control de acceso, permisos a traves de ROLES (privilegios))

## 2) Actualizacion Spring Boot 3: Dependencia thymeleaf-extras-springsecurity6

    - Agregamos la siguiente dependencia para continuar con el desarrollo en spring 3 o superior:

        ->     <dependency>
                  <groupId>org.thymeleaf.extras</groupId>
                  <artifactId>thymeleaf-extras-springsecurity6</artifactId>
               </dependency>

## 3) Agregando las dependencias necesarias

    - DOS depedencias -> Spring Starter Security y Spring Thymeleaf Security (ver seccion anterior)
    - Una vez instaladas las dependencias veremos en la consola a inicializar la app..

    - VEMOS ESTO:
    
    Using generated security password: 2b0a1f4c-c1f2-4ae2-832f-75c3b460c2dd

    This generated password is for development use only. Your security configuration must be updated before running
    your application in production.

    - Esto es generado por default -> user: user; password: auto-generada arriba!

## 4) ACTUALIZACION SIGUIENTE CLASE: Metodo BCryptPasswordEncoder passwordEncoder()

    Un tema que veremos en la siguiente clase sobre el método passwordEncoder(). Para evitar un posible error en las ultimas versiones de spring boot 2.6.0 en adelante:

    BeanCurrentlyInCreationException: Error creating bean with name 'springSecurityConfig': Requested bean is currently in creation: Is there an unresolvable circular reference

    En la clase SpringSecurityConfig, tienen que modificar esto por:

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    por

        @Bean
        public static BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    es decir le agregas la palabra static.

    Esto es muy importante que lo tengan en cuenta para la siguiente clase (si es que utilizan Spring Boot 2.6.0 o superior), agregar el modificador static en el método passwordEncoder(), mas info:

    https://stackoverflow.com/questions/70254555/spring-boot-2-6-0-error-creating-bean-with-name-websecurityconfig/70265714

## 5) [Importante] Spring Boot 3: Actualización clase SpringSecurityConfig

    - Actualización para las siguientes clases, usando Spring Boot 3, Spring Security 6 y usando JAVA 17 o 19.

    - En Spring Security 6, así como otros métodos de configuración para proteger las solicitudes (a saber, y ) se han eliminado de la API.antMatchers()mvcMathcers()regexMatchers()

    - Se introdujo un método sobrecargado requesMatchers() como un medio uniforme para asegurar las solicitudes. Los sabores de facilitan todas las formas de restringir las solicitudes que fueron admitidas por los métodos eliminados.requesMatchers()
    Además, el método authorizeRequests() ha quedado obsoleto y ya no debe usarse, ahora en Spring Boot 3 se usa authorizeHttpRequests().

    ESTE ES EL CODIGO ACTUALIZADO 

    - Actualización para las siguientes clases, usando Spring Boot 2.6 o 2.7.

    Quedaría de la siguiente forma:

    package com.bolsadeideas.springboot.app;
     
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;
     
    @Configuration
    public class SpringSecurityConfig {
        
        @Bean 
        public static BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
 
        @Bean
        public UserDetailsService userDetailsService()throws Exception{
                    
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User
                    .withUsername("user")
                    .password(passwordEncoder().encode("user"))
                    .roles("USER")
                    .build());
             manager.createUser(User
                        .withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN","USER")
                        .build());
            
            return manager;
        }
        
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     
            http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                    .antMatchers("/ver/**").hasAnyRole("USER")
                    .antMatchers("/uploads/**").hasAnyRole("USER")
                    .antMatchers("/form/**").hasAnyRole("ADMIN")
                    .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
                    .antMatchers("/factura/**").hasAnyRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().permitAll()
                    .and()
                    .logout().permitAll();
     
            return http.build();
        }
        
    }

## 6) Implementando la clase SpringSecurityConfig para registrar usuarios

    - En la carpeta base del proyecto, creamos dicha clase donde manejaremos el registro
    - Anotamos con @Configuration
    - VER CODIGO PARA VER ACTUALIZACION DE UNA VERSION A LA OTRA! IMPORTANTE!
    
## 7) Agregando metodo configure(HttpScurity http) para las reglas ACL en las rutas

    - Autorizacion en nuestras rutas -> SpringScurityConfig class
    - MUCHO CUIDADO ACA!
        -> Este es el codigo que implementamos al final para el SecurityFilterChain
    
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf().disable()
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
                        .formLogin();
                return http.build();
            }
    
    Si revisamos el codigo vemos que nos tira 4 'errores' pero son simplemente warning en vez de errores
    la app funciona correctamente pero ya es un tipo de error por compatibilidades de versiones.

## 8) Agregando configuracion para el formLogin y Logout

    - Esta seccion esta DEPRECADA, continuamos con la configuracion de la seccion anterior y avanzamos con el controller del Login

## 9) Creando la clase controladora LoginController
    
    - Personalizando nuestro formulario login con el controller (LoginController)
    - Mapeamos un GET request como veniamos trabajando que retorna la vista del login
    - AGREGAMOS UNA NUEVA CLASE, "Principal principal" de security para validacion de usuario
        
        Evitamos que haga doble inicio de sesion con lo siguiente
        if(principal != null){
            return "redirect:/listar";
        }
    
    - IMPORTANTE! habilitamos esta pagina en el Config del SecurityFilterChain justo despues del formLogin() -> .loginPage("/login");

## 10) Agregando la vista personalizada login.html

    - Creamos el template de la vista -> login.html
    - Agregando estilo propio al login y completado

## 11) Validando LoginController con los mensajes de error

    - En el ejemplo del curso, se muestra que en el formulario de login aparece AUTOMATICAMENTE con hidden, el csrf con el token
    - Quitar el ".csrf().disable()" FOR ENABLE esta feature.
    - ESTO ES MUY IMPORTANTE PARA CASOS DE VULNERABILIDAD, TENER MUY EN CUENTA ESTE CONCEPTO, INVESTIGA MAS A FONDO PARA MAS CLARIDAD.

    -> Continuando con la seccion

    - Manejo de Errores -> Mostrar errores y demas para mas visibilidad de estos al intentar iniciar sesion
    - Mostrando ERRORES -> Desde el controlador vamos a manejar con RequestParameter en el login

## 12) Agregando los links del login y logout en el layout para iniciar y cerrar sesion

    - Agregando el login dentro del header para iniciar y cerrar
    - Nos dirigimos al template 'layout' y vamos trabajando alli dentro del header
    - Agregamos los correspondientes tag Y DEBEMOS AGREGAR EL NAMESPACE EN EL HTML TAG PARA SPRING SECURITY
    - Ahora, agregamos las etiquetas del dropdown para el menu del usuario, para cierre de sesion

## 13) Manejo de Errores de acceso denegado AccessDeniedPage

    - Para el ROL = USER, personalizamos el template de error al querer por ejemplo: Cargar Factura, editar.. etc.
    - Dicho ROL, solo puede 'VER' los clientes y un par de features mas
    - Vamos al MvcConfig e implementamos un metodo para poder registrar el controlador de manejo de error 403
    - Creamos la vista -> error_403
    - Una vez tenemos registrado el controlador e implementado la vista, Vamos a SpringSecurityConfig y agregamos nuestra pagina de error 
    en el filter
            -> .exceptionHandling().accessDeniedPage("/error_403");

## 14) Agregando seguridad a nuestras vistas y botones del CRUD
    
    - Poder mostrar u ocultar botones segun permisos de cada usuario
    - Nos dirigimos al template 'listar' (home) y vamos modificando alli los permisos
    - Agregamos -> Para admin: sec:authorize="hasRole('ROLE_ADMIN')"
                -> Para user: sec:authorize="hasRole('ROLE_USER')"
    En los campos que cada uno puede realizar o no

    - Ahora en template 'ver' hacemos lo mismo para CREAR FACTURA alli

## 15) Implementando la clase SuccessHandler para personalizar el mensaje de exito 

    - Esto es para poder agregar personalizacion a los mensajes de exito al iniciar sesion
    - Creamos un nuevo package para el manejo de handler por parte del auth con la clase LoginSuccessHandler
    - Heredamos de SimpleUrlAuthenticationSuccessHandler y sobreescribimos el metodo que alli se muestra para el msj de exito
    - AHORA VAMOS AL SpringSecurityConfig y agregamos el metodo dentro del SecurityFilter antes del .loginPage

## 16) Obteniendo el usuario autenticado en el controlador 

    - Es una implementacion mas para poder ver el nombre del user que se autentico, simple estetica.

## 17) Obteniendo programaticamente el role (s) del usuario en el controlador

    - Dentro del clienteControler creamos un metodo hasRole para esta implementacion.

## 18) Agregar seguridad en el controlador usando anotaciones  @Secured o @PreAuthorize

    - En vez de dar seguridad a las rutas HTTP, podemos usar anotaciones en el controlador
    - Podemos agregar las siguientes anotaciones:
        -> Para metodos (dentro del clienteController) -> editar, crear etc.. agregamos @Secured("ROLE_ADMIN")
        -> Para metodos destinados para el rol simple de "USER" agregamos el @Secured("ROLE_USER")
        -> Para metodos -> listar, handler para user SIN NINGUNA AUTENTICACION DEJAMOS SIN NINGUNA ANOTACION ya que son publicos

    - Para validar roles:
        -> hasRole([role])
        -> hasAnyRole([role1],[role2])

    - VER + EN DOCUMENTACION DE SPRING!

# Y AQUI FINALIZA LA SECCION NRO 13 SOBRE UN PANTALLAZO MUY BUENO SOBRE SPRING SECURITY
# Continuamos con la siguiente seccion pero dentro del mismo proyecto

# Seccion 14 - Spring Security: Autenticacion JDBC - Bases de datos usando JDBC

## 1) BCrypt password encoder

    - Generar, encryptar algunas password que usaremos para autenticar usuarios
    - En la clase de SpringSecurityConfig
        -> Tenemos el @Bean para passwordEncoder: Importante que esta clase de componentes esten siempre en una clase
        de 'Configuration' (Podemos agregarlo al MvcConfig si asi desearamos)
    - Moveremos este @Bean al MvcConfig por simple demostracion para la seccion
    - Lo inyectamos en el security para poder usarlo

    - Nos dirigimos a la clase que maneja toda la app, la clase 'raiz' del proyecto
        -> Agregamos la generacion de las password para el ejemplo dentro del metodo sobreescrito 'run'
        -> Vemos como se generan las password encryptadas para su uso, en el terminal

## 2) Creando el esquema de tablas de base de datos
    
    - Creamos el esquema de tabla en la base de datos usando MySQLWorkbench para nuestro sistema de login, auth y autorizacion
    - Nos conectamos a nuestra DB en MYSQLWB.
    - Seleccionamos nuestra base de datos como "SET DEFAULT SCHEMA"
    - Creamos una nueva tabla 'users' con los siguientes valores
    
        CREATE TABLE `db_springboot`.`users` (
            `id` INT NOT NULL AUTO_INCREMENT,
            `username` VARCHAR(45) NOT NULL,
            `password` VARCHAR(60) NOT NULL,
            `enabled` TINYINT(1) NOT NULL,
            PRIMARY KEY (`id`),
            UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);

    - Creamos una nueva tabla 'authorities' con los siguientes valores
        -> Aqui tendremos 3 campos importantes
            - Creamos un index 'user_id_authority_id' type UNIQUE y seleccionamos id como 'index column'
            - Creamos una FOREIGN KEY 'fk_authorities_users' c/ `db_springboot`.`users` (Referenced Table)
              Con 'Foreign Key Columns' asociado a 'user_id' (column) y 'id' (Referenced Column) y COLOCAMOS
                On Update: CASCADE
                On Delete: CASCADE

    - Creamos unos registros de ejemplos
        INSERT INTO users (username, password, enabled) VALUES('pablo', '', 1);
        INSERT INTO users (username, password, enabled) VALUES('admin', '', 1);

    - LA PASSWORD ES ENCRYPTADA, EJECUTAMOS LA APP Y OBTENEMOS LAS DOS CLAVES ECRYPTADAS EN EL TERMINAL
    LAS COPIAMOS Y LE AGREGAMOS COMO VALUE A NUESTROS REGISTROS
    
        -> EN NUESTRO EJEMPLO:  
            $2a$10$nghAmrloNQQz40Xa9M7XiebypM/gjm8Z8wqMjujmA1AwrGah1X3Ny -> pablo
            $2a$10$DFPsFDjZtrGBC0ZnwKohQuGe2YZKkbruRba1YDEeIAsw1f6J4mTau -> admin

    - Ahora agregamos registros de ejemplos para los ROLES
    
        INSERT INTO authorities (user_id, authority) VALUES(1, 'ROLE_USER');
        INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_USER');
        INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_ADMIN');

## 3) Configuracion JDBC Authentication

    - Una vez terminada la seccion anterior con el registro de los users con su encrypt password y demas
    - Debemos ELIMINAR O COMENTAR el metodo dentro del SprinSecurityConfig -> userDetailsService
      Ya que este al generar en memoria esos usuarios hara conflicto con la auth por la base de datos.

    - Agregamos el metodo dentro de la configuracion 'authManager'

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
    
    - Donde, como vemos, hacemos los pasos necesarios juntos con las querys sobre nuestros usuarios y sus claves
    ecryptadas
    - y Aqui finaliza.

# AQUI FINALIZA LA SECCION 14 - Autenticacion JDBC

# CONTINUAMOS DENTRO DEL MISMO PROYECTO CON LA SIGUIENTE SECCION

# Seccion 15 - Spring Security: Autenticacion con JPA (TEMA RELEVANTE YA QUE JPA ES EL FRAMEWORK DE SP.SC MAS USADO!)

## 1) Creando las entidades necesarias Usuario y Role

    - Como implementar Spring Security con persistencia usando JPA
    - Crearemos las clases entity, MAPEADAS A TABLAS, para el USUARIO y para el ROL
    - ESTAS CLASES VAN A TENER EL MISMO COMPORTAMIENTO COMO HABIAMOS CREADOS EN LA SECCION ANTERIOR DENTRO
    DEL MYSQLWORKBENCH. ESTA IMPLEMENTACION ESTA HECHA EN BASE AL DISENO 'ORM' (Object Relational Mapping) 

## 2) Creando el repositorio JPA IUsuarioDao

    - Clase repository para el JPA, con sus metodos de consultas como findById, findAll.. etc.
    - Vamos al package DAO y creamos la interfaz -> IUsuarioRepository
    - Y creamos el metodo findByUsername

## 3) Creando la clase de servicio JpaUserDetailsService

    - Implementamos la clase SERVICE para el proceso de auth con JPA
    - En package SERVICE -> JpaUserDetailsService (NUEVA CLASE)
    - Ver codigo y comentarios de la implementacion

## 4) Configurando y registrando JpaUserDetailsService

    - Vamos al config de SpringSecurity y ver la implementacion de esta autenticacion.

# Y AQUI FINALIZA ESTA DOBLE SECCION SOBRE AUTENTICACION CON JDBC Y AUTENTICACION CON JPA

# CONTINUAMOS CON EL SIGUIENTE PROYECTO, EL CUAL, ES EL MISMO QUE ESTE PERO CON UNA NUEVA IMPLEMENTACION
# VER (springboot-data-jpa-v3features)

