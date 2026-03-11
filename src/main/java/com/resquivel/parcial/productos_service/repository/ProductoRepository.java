package com.resquivel.parcial.productos_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository; // <-- Asegúrate de que esta ruta coincida con tu paquete real
import org.springframework.stereotype.Repository;

import com.resquivel.parcial.productos_service.model.Producto;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {
    // Al heredar de MongoRepository, Spring Boot ya nos regala mágicamente
    // los métodos para guardar, buscar, actualizar y borrar sin escribir código SQL.
}
