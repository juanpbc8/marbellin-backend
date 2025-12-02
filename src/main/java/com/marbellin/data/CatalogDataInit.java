package com.marbellin.data;

import com.marbellin.catalog.dto.CategoryCreateDto;
import com.marbellin.catalog.dto.ProductCreateDto;
import com.marbellin.catalog.dto.ProductVariantCreateDto;
import com.marbellin.catalog.service.CategoryService;
import com.marbellin.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Order(3)
@RequiredArgsConstructor
public class CatalogDataInit implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;

    // Listas maestras para generación aleatoria
    private static final List<String> ALL_COLORS = List.of(
            "Rojo", "Negro", "Blanco", "Vino", "Nude",
            "Rosa Palo", "Azul Noche", "Esmeralda", "Perla"
    );
    private static final List<String> ALL_SIZES = List.of("S", "M", "L");

    @Override
    public void run(String... args) {
        System.out.println("👙 Inicializando catálogo de Marbellin (Con Variantes)...");

        try {
            // 1. CREAR CATEGORÍAS
            Long bikiniId = createCategory("Bikini");
            Long cacheteroId = createCategory("Cachetero");
            Long semiId = createCategory("Semi Hilo");
            Long topsitoId = createCategory("Topsito");

            // 2. CREAR PRODUCTOS (BIKINIS)
            createProduct("Bikini Animal Print", "Bikini con estampado animal print, ideal para destacar en la playa con estilo y confianza.",
                    bikiniId, "/uploads/products/bikinis/bikini-animal-print1.webp");
            createProduct("Bikini Blonda", "Bikini con detalles de blonda delicada, combinación perfecta de sensualidad y elegancia.",
                    bikiniId, "/uploads/products/bikinis/bikini-blonda.webp");
            createProduct("Bikini Blondita Completa", "Conjunto completo de bikini con diseño suave y femenino, para un look delicado.",
                    bikiniId, "/uploads/products/bikinis/bikini-blondita-completa.webp");
            createProduct("Bikini Clásico", "Diseño clásico y atemporal, ideal para quienes prefieren un estilo tradicional.",
                    bikiniId, "/uploads/products/bikinis/bikini-clasico.webp");
            createProduct("Bikini Estrella", "Bikini con detalles de estrella que aportan un toque juvenil y divertido.",
                    bikiniId, "/uploads/products/bikinis/bikini-estrella.webp");
            createProduct("Bikini Pretina Ancha", "Cómodo bikini con pretina ancha que brinda mejor ajuste y soporte.",
                    bikiniId, "/uploads/products/bikinis/bikini-pretina-ancha.webp");

            // 3. CREAR PRODUCTOS (CACHETEROS)
            createProduct("Cachetero Blonda con Logo", "Cachetero con elegantes acabados en blonda y logo decorativo.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-blonda-con-logo.webp");
            createProduct("Cachetero Blonda", "Cachetero clásico de blonda, suave y cómodo para el uso diario.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-blonda.webp");
            createProduct("Cachetero Corazón Estampado", "Cachetero con estampado de corazones, ideal para un look tierno y coqueto.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-corazon-estampado-1.webp");
            createProduct("Cachetero Corazón", "Cachetero con detalles románticos en forma de corazón, perfecto para ocasiones especiales.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-corazon-1.webp");
            createProduct("Cachetero Dije Estampado", "Cachetero con pequeño dije decorativo y estampados modernos.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-dije-estampado-1.webp");
            createProduct("Cachetero Dije", "Cachetero elegante con dije en el frente, para un detalle extra de estilo.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-dije-1.webp");
            createProduct("Cachetero Encaje Atrás", "Cachetero con diseño de encaje en la parte posterior, sexy y sofisticado.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-encaje-atras-1.webp");
            createProduct("Cachetero Señorial Floreado", "Diseño floreado con estilo señorial, mezcla de tradición y elegancia.",
                    cacheteroId, "/uploads/products/cacheteros/cachetero-señorial-floreado.webp");

            // 4. CREAR PRODUCTOS (SEMI HILO)
            createProduct("Semi Hilo Clásico", "Diseño clásico de semi hilo, para quienes buscan comodidad y sensualidad.",
                    semiId, "/uploads/products/semi/semi-hilo-clasico.webp");
            createProduct("Semi Hilo Dije", "Semi hilo decorado con dije, moderno y atractivo para cualquier ocasión.",
                    semiId, "/uploads/products/semi/semi-hilo-dije.webp");
            createProduct("Semi Hilo Pretina Ancha", "Semi hilo con pretina ancha que proporciona ajuste ideal y confort.",
                    semiId, "/uploads/products/semi/semi-hilo-pretina-ancha.webp");
            createProduct("Semi Hilo Señorial Juvenil", "Estilo que combina elegancia señorial con frescura juvenil.",
                    semiId, "/uploads/products/semi/semi-señorial-juvenil-1.webp");

            // 5. CREAR PRODUCTOS (TOPSITOS)
            createProduct("Topsito Clásico", "Topsito de corte clásico, esencial para cualquier guardarropa íntimo.",
                    topsitoId, "/uploads/products/topsitos/topsito-clasico.webp");
            createProduct("Topsito con Tirante", "Topsito cómodo con tirantes ajustables, ideal para uso diario.",
                    topsitoId, "/uploads/products/topsitos/topsito-con-tirante.webp");
            createProduct("Topsito Olímpico", "Bikini estilo deportivo tipo topsito olímpico, brinda soporte y estilo.",
                    topsitoId, "/uploads/products/topsitos/topsito-olimpico.webp");

            System.out.println("✅ Catálogo Marbellin cargado con éxito.");

        } catch (Exception e) {
            System.err.println("⚠️ Error al inicializar datos: " + e.getMessage());
        }
    }

    private Long createCategory(String name) {
        CategoryCreateDto dto = new CategoryCreateDto(name, null);
        return categoryService.create(dto).id();
    }

    private void createProduct(String name, String desc, Long catId, String imgPath) {
        // 1. Generar Precio Aleatorio (35 - 50)
        int randomPrice = ThreadLocalRandom.current().nextInt(35, 51);
        BigDecimal price = BigDecimal.valueOf(randomPrice);

        // 2. Generar Variantes (3 Colores x 3 Tallas)
        List<ProductVariantCreateDto> variants = generateRandomVariants(name);

        // 3. Crear DTO (Ajustado a la estructura correcta: 7 campos)
        ProductCreateDto dto = new ProductCreateDto(
                name,
                desc,
                price,
                imgPath,
                "ACTIVO",
                catId,
                variants // <--- Lista de variantes
        );

        // 4. Guardar
        productService.create(dto);
    }

    private List<ProductVariantCreateDto> generateRandomVariants(String productName) {
        List<ProductVariantCreateDto> variants = new ArrayList<>();

        // Seleccionar 3 colores aleatorios de la lista maestra
        List<String> shuffledColors = new ArrayList<>(ALL_COLORS);
        Collections.shuffle(shuffledColors);
        List<String> selectedColors = shuffledColors.subList(0, 3);

        // Generar base del SKU (Ej: "Bikini Animal" -> "BIK-ANI")
        String skuBase = generateSkuBase(productName);

        for (String color : selectedColors) {
            for (String size : ALL_SIZES) {
                // Generar SKU único para la variante: BIK-ANI-ROJ-S
                String skuColor = color.substring(0, 3).toUpperCase();
                String variantSku = String.format("%s-%s-%s", skuBase, skuColor, size);

                // Stock aleatorio entre 10 y 50
                int stock = ThreadLocalRandom.current().nextInt(10, 51);

                variants.add(new ProductVariantCreateDto(
                        variantSku,
                        size,
                        color,
                        stock
                ));
            }
        }
        return variants;
    }

    private String generateSkuBase(String name) {
        String[] parts = name.split(" ");
        StringBuilder sb = new StringBuilder();
        if (parts.length > 0) sb.append(parts[0], 0, Math.min(3, parts[0].length()));
        if (parts.length > 1) sb.append("-").append(parts[1], 0, Math.min(3, parts[1].length()));
        else sb.append("-001");

        sb.append("-").append(ThreadLocalRandom.current().nextInt(100, 999));

        return sb.toString().toUpperCase();
    }
}
