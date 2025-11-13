package com.provesi.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "usuarios")
public class Usuario {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idUsuario;

  @Column(nullable = false, length = 120)
  private String nombre;

  @Column(nullable = false, unique = true, length = 150)
  private String email;

  @Column(nullable = false, length = 20)
  private EstadoUsuario estado;  // Ej. Activo/Inactivo

  @Column(nullable = false, length = 20)
  private String rol;

    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public EstadoUsuario getEstado() {
        return estado;
    }
    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    
}
