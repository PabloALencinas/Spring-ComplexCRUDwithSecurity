package com.pabloagustin.springbootdatajpa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clientes")
// Es recomendable usar Serializable, proceso de convertir un objeto en una secuencia de bytes
// Para almacenarlo o transmitirlo a la memoria, o a una base de datos, json, xml o sesiones http!
// SIEMPRE implementar serializable a nuestras clases entity con JPA - Hibernate
public class Cliente implements Serializable {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY ) // Con MYSQL, H2, SQL SERVER -> IDENTITY ; Con PostgreSQL.. -> SEQUENCE
	private Long id;

	@NotEmpty
	private String nombre;
	@NotEmpty
	private String apellido;
	@NotEmpty
	@Email
	private String email;
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE) // Indica en que formato se va a guardar la fecha
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private Date createAt;

	// Mapear CLIENTES con FACTURAS -> Relacion: 1(Un) Cliente -> MUCHAS Facturas
	// Relacion en AMBOS sentidos por lo que no es necesario el JOINCOLUMN
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Carga PEREZOSA. A medida que se van invocando los metodos, NO todo a la vez
	private List<Factura> facturas;

	// Inicializamos el arrayList
	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	// Atributo para la foto del cliente
	private String foto;


	// Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	// Metodo para agregar factura para el cliente
	public void addFactura(Factura factura){
		facturas.add(factura);
	}

	@Override
	public String toString() {
		return 	nombre + " " + apellido;
	}

	private static final long serialVersionUID = 1L;

}
