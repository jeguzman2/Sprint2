package com.provesi.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;


@Entity
@Table(name = "inventarios", indexes = @Index(name = "idx_inv_prod_fecha", columnList = "producto_id,updatedAt"))

public class Inventario {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idInventario;

  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Producto producto;

  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Ubicacion ubicacion;

  @Column(nullable = false)
  private Integer cantidadDisponible;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoInventario estado;

 public Long getIdInventario() {
    return idInventario;
  }

  public void setIdInventario(Long idInventario) {
    this.idInventario = idInventario;
  }

  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(Ubicacion ubicacion) {
    this.ubicacion = ubicacion;
  }

  public Integer getCantidadDisponible() {
    return cantidadDisponible;
  }

  public void setCantidadDisponible(Integer cantidadDisponible) {
    this.cantidadDisponible = cantidadDisponible;
  }
  public EstadoInventario getEstado() {
    return estado;
  }
  public void setEstado(EstadoInventario estado) {
    this.estado = estado;
  }

}
