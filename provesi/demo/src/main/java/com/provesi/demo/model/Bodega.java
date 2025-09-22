package com.provesi.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bodegas")
public class Bodega {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idBodega;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 150)
  private String ciudad;

  // Getters y setters
  public Long getId()
  {
    return idBodega;
  }

  public void setId(Long idBodega) {
    this.idBodega = idBodega;
  }
  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  public String getCiudad() {
    return ciudad;
  }
  public void setCiudad(String ciudad) {
    this.ciudad = ciudad;
  }
}



