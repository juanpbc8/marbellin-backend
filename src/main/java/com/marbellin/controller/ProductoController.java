package com.marbellin.controller;

import com.marbellin.dto.*;
import com.marbellin.entity.ImagenModelo;
import com.marbellin.entity.ModeloProducto;
import com.marbellin.entity.Producto;
import com.marbellin.repository.ModeloProductoRepository;
import com.marbellin.repository.ProductoRepository;
import com.marbellin.service.ProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones con productos base")
public class ProductoController {

    private final ProductoService service;
    private final ProductoRepository repo;
    private final ModeloProductoRepository modeloProductoRepository;

    public ProductoController(ProductoService service, ProductoRepository repo, ModeloProductoRepository modeloProductoRepository) {
        this.service = service;
        this.repo = repo;
        this.modeloProductoRepository = modeloProductoRepository;
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return service.guardar(producto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/{id}/modelos")
    public ResponseEntity<List<ModeloProductoDTO>> obtenerModelos(@PathVariable Long id) {
        var producto = repo.findById(id).orElseThrow();

        List<ModeloProductoDTO> modelosDTO = producto.getModelos()
                .stream()
                .map(modelo -> {
                    List<ImagenModeloDTO> imagenes = modelo.getImagenes()
                            .stream()
                            .map(img -> new ImagenModeloDTO(img.getUrl(), img.getOrden()))
                            .collect(Collectors.toList());

                    List<VariacionProductoDTO> variaciones = modelo.getVariaciones().stream()
                            .map(var -> new VariacionProductoDTO(
                                    var.getColor().getNombreColor(),
                                    var.getTalla().getNombreTalla(),
                                    var.getStock()))
                            .collect(Collectors.toList());

                    return new ModeloProductoDTO(
                            modelo.getNombreModelo(),
                            modelo.getDescripcion(),
                            modelo.getPrecio(),
                            imagenes,
                            variaciones
                    );
                }).collect(Collectors.toList());

        return ResponseEntity.ok(modelosDTO);
    }

    @GetMapping("/catalogo")
    public ResponseEntity<List<ProductoCatalogoDTO>> obtenerCatalogoCompleto() {
        List<Producto> productos = repo.findAll();
        List<ProductoCatalogoDTO> respuesta = productos.stream().map(producto -> {
            // Suponemos que cada producto tiene al menos un modelo
            ModeloProducto primerModelo = producto.getModelos().get(0);

            ImagenModelo imagenPrincipal = primerModelo.getImagenes().stream()
                    .sorted(Comparator.comparingInt(ImagenModelo::getOrden))
                    .findFirst()
                    .orElse(null);

            return new ProductoCatalogoDTO(
                    producto.getIdProducto(),
                    primerModelo.getNombreModelo(),
                    primerModelo.getPrecio(),
                    imagenPrincipal != null ? imagenPrincipal.getUrl() : "",
                    producto.getCategoria().getNombreCategoria()
            );
        }).toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/modelos/catalogo")
    public ResponseEntity<List<ModeloCatalogoDTO>> obtenerTodosLosModelos() {
        List<Producto> productos = repo.findAll();

        List<ModeloCatalogoDTO> modelos = productos.stream()
                .flatMap(producto -> producto.getModelos().stream()
                        .map(modelo -> {
                            ImagenModelo imagenPrincipal = modelo.getImagenes().stream()
                                    .sorted(Comparator.comparingInt(ImagenModelo::getOrden))
                                    .findFirst()
                                    .orElse(null);

                            return new ModeloCatalogoDTO(
                                    modelo.getIdModelo(),
                                    producto.getIdProducto(),
                                    modelo.getNombreModelo(),
                                    modelo.getPrecio(),
                                    imagenPrincipal != null ? imagenPrincipal.getUrl() : "",
                                    producto.getCategoria().getNombreCategoria()
                            );
                        })
                ).toList();

        return ResponseEntity.ok(modelos);
    }

    @GetMapping("/modelos/{id}")
    public ResponseEntity<ModeloProductoDetalleDTO> obtenerDetalleModelo(@PathVariable Long id) {
        Optional<ModeloProducto> modeloOpt = modeloProductoRepository.findById(id);
        if (modeloOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ModeloProducto modelo = modeloOpt.get();

        String imagen = modelo.getImagenes().isEmpty()
                ? "/images/no-disponible.jpg"
                : modelo.getImagenes().get(0).getUrl();

        List<VariacionDTO> variaciones = modelo.getVariaciones().stream()
                .map(var -> new VariacionDTO(
                        var.getColor().getNombreColor(),
                        var.getTalla().getNombreTalla(),
                        var.getStock()))
                .toList();

        ModeloProductoDetalleDTO dto = new ModeloProductoDetalleDTO(
                modelo.getIdModelo(),
                modelo.getNombreModelo(),
                modelo.getDescripcion(),
                modelo.getPrecio(),
                imagen,
                modelo.getProducto().getCategoria().getNombreCategoria(),
                variaciones
        );

        return ResponseEntity.ok(dto);
    }

}
