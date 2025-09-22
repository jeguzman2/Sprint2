package com.provesi.demo.service;


import org.springframework.stereotype.Service;

import com.provesi.demo.DetailDTO.ProductoDetalleDTO;
import com.provesi.demo.model.Bodega;
import com.provesi.demo.model.EstadoProducto;
import com.provesi.demo.model.Inventario;
import com.provesi.demo.model.Producto;
import com.provesi.demo.model.Ubicacion;
import com.provesi.demo.repositorios.InventarioRepository;
import com.provesi.demo.repositorios.ProductoRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class ConsultaProductoService {

    private final ProductoRepository productoRepo;
    private final InventarioRepository inventarioRepo;

    public ConsultaProductoService(ProductoRepository productoRepo, InventarioRepository inventarioRepo) {
        this.productoRepo = productoRepo;
        this.inventarioRepo = inventarioRepo;
    }

    /**
     * Consulta la información de un producto por su código
     * Devuelve un DTO con la información más reciente de inventario
     */
    @Transactional
    public ProductoDetalleDTO consultarProductoPorId(Long id) {
        // Buscar el producto
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));

        // Buscar inventario por producto
        List<Inventario> items = inventarioRepo.findByProducto_Id(id);

        if (items.isEmpty()) {
            // No hay inventario registrado, devolvemos el producto básico
            return new ProductoDetalleDTO(
                    producto.getNombre(),
                    EstadoProducto.SIN_REGISTRO, // estado
                    null, // bodega
                    null, // pasillo
                    null, // estante
                    null, // nivel
                    0 // cantidad disponible
            );
        }

        // Tomamos el primer registro (puede ajustarse según "más reciente")
        Inventario item = items.get(0);
        Ubicacion ubicacion = item.getUbicacion();
        Bodega bodega = ubicacion.getBodega();

        // Devolvemos el DTO con la info completa
        return new ProductoDetalleDTO(

                producto.getNombre(),
                producto.getEstado(),
                bodega.getNombre(),
                String.valueOf(ubicacion.getPasillo()),
                String.valueOf(ubicacion.getEstante()),
                String.valueOf(ubicacion.getNivel()),
                item.getCantidadDisponible()
        );
    }
}
