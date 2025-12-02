package com.marbellin.catalog.service;

import com.marbellin.catalog.dto.*;
import com.marbellin.catalog.entity.CategoryEntity;
import com.marbellin.catalog.entity.ProductEntity;
import com.marbellin.catalog.entity.ProductVariantEntity;
import com.marbellin.catalog.mapper.ProductMapper;
import com.marbellin.catalog.repository.CategoryRepository;
import com.marbellin.catalog.repository.ProductRepository;
import com.marbellin.catalog.repository.ProductVariantRepository;
import com.marbellin.common.exception.ConflictException;
import com.marbellin.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository; // Inyectamos el repo de variantes
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto create(ProductCreateDto dto) {
        // 1. Validar que no existan SKUs repetidos en las variantes entrantes
        if (dto.variants() != null) {
            for (ProductVariantCreateDto variantDto : dto.variants()) {
                if (productVariantRepository.existsBySku(variantDto.sku())) {
                    throw new ConflictException("Variant with SKU '" + variantDto.sku() + "' already exists");
                }
            }
        }

        // 2. Convertir DTO a Entidad (incluye la lista de variantes, pero sin el padre asignado)
        ProductEntity product = productMapper.toEntity(dto);

        // 3. Cargar y asignar Categoría
        CategoryEntity category = loadCategory(dto.categoryId());
        product.setCategory(category);

        // 4. VINCULACIÓN IMPORTANTE: Asignar el padre a cada hijo para que JPA guarde la FK
        if (product.getVariants() != null) {
            product.getVariants().forEach(variant -> variant.setProduct(product));
        }

        // 5. Guardar (Cascade ALL guardará las variantes automáticamente)
        ProductEntity saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }


    @Override
    @Transactional
    public ProductDto update(Long id, ProductUpdateDto dto) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // 1. Actualizar datos base del Padre
        productMapper.updateEntityFromDto(dto, product);

        if (dto.categoryId() != null) {
            CategoryEntity category = loadCategory(dto.categoryId());
            product.setCategory(category);
        }

        // 2. Gestionar Variantes (La parte compleja simplificada - upsert)
        if (dto.variants() != null) {
            for (ProductVariantUpdateDto vDto : dto.variants()) {
                if (vDto.id() != null) {
                    // CASO A: ACTUALIZAR EXISTENTE
                    // Buscamos la variante dentro de la lista del producto para asegurar consistencia
                    ProductVariantEntity variant = product.getVariants().stream()
                            .filter(v -> v.getId().equals(vDto.id()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Variant not found or doesn't belong to product"));

                    // Actualizamos manualmente los campos que vengan
                    if (vDto.stock() != null) variant.setStock(vDto.stock());
                    if (vDto.sku() != null) variant.setSku(vDto.sku());
                    // Talla y color usualmente no se cambian, se borra y crea otro,
                    // pero si quieres permitir corregir errores de tipeo:
                    if (vDto.size() != null) variant.setSize(vDto.size());
                    if (vDto.color() != null) variant.setColor(vDto.color());
                } else {
                    // CASO B: CREAR NUEVA (El usuario agregó una talla nueva en el form)
                    ProductVariantEntity newVariant = new ProductVariantEntity();
                    newVariant.setProduct(product);
                    newVariant.setSku(vDto.sku());
                    newVariant.setSize(vDto.size());
                    newVariant.setColor(vDto.color());
                    newVariant.setStock(vDto.stock() != null ? vDto.stock() : 0);
                    // Agregamos a la colección del padre
                    product.getVariants().add(newVariant);
                }
            }
        }

        ProductEntity updated = productRepository.save(product);
        return productMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteVariant(Long variantId) {
        if (!productVariantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException("Product variant not found with id: " + variantId);
        }
        // Hard delete: Se borra físicamente la fila de la variante
        productVariantRepository.deleteById(variantId);
    }

    @Override
    public ProductDto findById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> findAll(String search, String status, Long categoryId, Pageable pageable) {
        // El repositorio ya hace la magia del JOIN con las variantes si se busca por SKU
        Page<ProductEntity> products = productRepository.findByFilters(search, status, categoryId, pageable);
        return products.map(productMapper::toDto);
    }

    private CategoryEntity loadCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

}
