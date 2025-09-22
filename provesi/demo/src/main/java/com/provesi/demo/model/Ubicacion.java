package com.provesi.demo.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_id_bodega", nullable = false) // ← nombre real en ubicaciones
    @JsonIgnore
    private Bodega bodega;

    @Column(nullable = false, length = 150)
    private int pasillo;

    @Column(nullable = false, length = 150)
    private int estante;

    @Column(nullable = false, length = 150)
    private int nivel;

    public int getEstante() {
        return estante;
    }

    public void setEstante(int estante) {
        this.estante = estante;
    }
    public int getNivel() {
        return nivel;
    }
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getPasillo() {
        return pasillo;
    }
    public void setPasillo(int pasillo) {
        this.pasillo = pasillo;
    }
    public Bodega getBodega() {
        return bodega;
    }
    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }
    
}
