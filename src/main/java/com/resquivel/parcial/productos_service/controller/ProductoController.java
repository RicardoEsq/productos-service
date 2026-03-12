package com.resquivel.parcial.productos_service.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.resquivel.parcial.productos_service.model.Producto;
import com.resquivel.parcial.productos_service.repository.ProductoRepository;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CloudWatchService cloudWatchService;

    // GET /productos - Listar todos
    @GetMapping
    public List<Producto> obtenerProductos() {
        logger.info("Consultando todos los productos.");
        return productoRepository.findAll();
    }

    // GET /productos/{id} - Buscar uno específico
    @GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<Producto> obtenerProductoPorId(@PathVariable("id") String id) {
        logger.info("Buscando producto con ID: {}", id);
        return productoRepository.findById(id)
                .map(producto -> org.springframework.http.ResponseEntity.ok(producto))
                .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }

    // POST /productos - Crear uno nuevo
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        logger.info("Creando nuevo producto: {}", producto.getNombre());
        try {
            cloudWatchService.enviarLog("Nuevo producto añadido al catálogo: " + producto.getNombre());
        } catch (Exception e) {
            logger.warn("No se pudo enviar log a CloudWatch, pero el producto se creará.");
        }
        return productoRepository.save(producto);
    }

    // PUT /productos/{id} - Actualizar datos
    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<Producto> actualizarProducto(@PathVariable("id") String id, @RequestBody Producto productoDetalles) {
        logger.info("Intentando actualizar producto ID: {}", id);
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(productoDetalles.getNombre());
            producto.setPrecio(productoDetalles.getPrecio());
            producto.setStock(productoDetalles.getStock());
            try {
                cloudWatchService.enviarLog("Producto actualizado: " + producto.getNombre());
            } catch (Exception e) {}
            return org.springframework.http.ResponseEntity.ok(productoRepository.save(producto));
        }).orElse(org.springframework.http.ResponseEntity.notFound().build());
    }

    // DELETE /productos/{id} - Eliminar del catálogo
    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable String id) {
        logger.info("Eliminando producto ID: {}", id);
        productoRepository.deleteById(id);
        cloudWatchService.enviarLog("Producto eliminado del sistema, ID: " + id);
    }
}