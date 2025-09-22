package com.provesi.demo.DTO;

import com.provesi.demo.model.EstadoInventario;

public class InventarioDTO {
    private Long productoId;
    private Long idUbicacion;
    private Integer cantidadDisponible;
    private EstadoInventario estado;

    public Long getProductoId() {
        return productoId;
    }
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    public Long getIdUbicacion() {
        return idUbicacion;
    }
    public void setIdUbicacion(Long idUbicacion) {
        this.idUbicacion = idUbicacion;
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
