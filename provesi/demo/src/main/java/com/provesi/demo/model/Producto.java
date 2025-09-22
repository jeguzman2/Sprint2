package com.provesi.demo.model;
import jakarta.persistence.*;

@Entity
@Table(name = "productos", indexes = {
@Index(name = "producto_codigo", columnList = "codigo", unique = true)
})

public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    

    @Column(nullable = false, length = 150)
    private Float peso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 150)
    private UnidadPeso unidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProducto estado;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String descripcion;

    @Column(nullable = false, length = 150)
    private String dimensiones;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public Float getPeso() {
        return peso;
    }
    public void setPeso(Float peso) {
        this.peso = peso;
    }
    public UnidadPeso getUnidad() {
        return unidad;
    }
    public void setUnidad(UnidadPeso unidad) {
        this.unidad = unidad;
    }
    public EstadoProducto getEstado() {
        return estado;
    }
    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDimensiones() {
        return dimensiones;
    }
    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }



}
