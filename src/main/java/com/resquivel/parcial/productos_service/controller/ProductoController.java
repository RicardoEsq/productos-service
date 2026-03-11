package com.resquivel.parcial.productos_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resquivel.parcial.productos_service.model.Producto;
import com.resquivel.parcial.productos_service.repository.ProductoRepository;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    // Bitácora para registrar los eventos (esto servirá para CloudWatch)
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoRepository productoRepository;

    // Ruta GET: Para consultar todos los productos
    @GetMapping
    public List<Producto> obtenerProductos() {
        logger.info("Solicitud recibida: Obteniendo la lista de todos los productos.");
        return productoRepository.findAll();
    }

    @Autowired
    private CloudWatchService cloudWatchService;



    // Ruta POST: Para crear un producto nuevo
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        logger.info("Solicitud recibida: Creando un nuevo producto llamado {}", producto.getNombre());
        
        cloudWatchService.enviarLog("¡Éxito! Procesando nuevo producto en el catálogo.");

        return productoRepository.save(producto);
    }
}
