package com.provesi.demo.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "orden_alistamiento")
public class OrdenAlistamiento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idAlistamiento;

  @Column(nullable = false)
  private Instant fechaCreacion;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoAlistamiento estado;  // Ejemplo: "Alistamiento", "Verificación"

  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Usuario operario;

    public Long getIdAlistamiento() {
        return idAlistamiento;
    }
    public void setIdAlistamiento(Long idAlistamiento) {
        this.idAlistamiento = idAlistamiento;
    }
    public Instant getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public EstadoAlistamiento getEstado() {
        return estado;
    }
    public void setEstado(EstadoAlistamiento estado) {
        this.estado = estado;
    }
    public Usuario getOperario() {
        return operario;
    }
    public void setOperario(Usuario operario) {
        this.operario = operario;
    }
    
}
