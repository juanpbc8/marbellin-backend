# GitHub Copilot Instructions - Marbellin Backend

## 📋 Descripción del Proyecto

**Marbellin Backend** es una API REST completa desarrollada con Spring Boot
3.5.7 y Java 21 para la gestión de un ecommerce de lencería. El sistema maneja
catálogo de productos, gestión de clientes, órdenes, inventarios, facturación y
autenticación.

---

## 🏗️ Arquitectura y Tecnologías

### Stack Tecnológico

- **Framework**: Spring Boot 3.5.7
- **Java**: 21
- **Base de Datos**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Documentación**: SpringDoc OpenAPI (Swagger)
- **Validación**: Bean Validation
- **Mapeo**: MapStruct 1.6.3
- **Seguridad**: Spring Security
- **Utilidades**: Lombok
- **Testing**: Spring Boot Test

### Arquitectura de Módulos

El proyecto sigue una arquitectura modular por dominios:

```
src/main/java/com/marbellin/
├── common/           # Componentes compartidos
├── iam/             # Gestión de identidad y acceso
├── customers/       # Gestión de clientes
├── catalog/         # Catálogo de productos
├── attributes/      # Atributos de productos
├── variants/        # Variantes de productos
├── inventory/       # Gestión de inventario
├── orders/          # Gestión de órdenes
├── billing/         # Facturación y pagos
└── data/            # Inicialización de datos
```

---

## 🎯 Convenciones de Código

### 1. Estructura de Paquetes por Módulo

Cada módulo sigue la estructura:

```
moduleName/
├── controller/      # Endpoints REST
├── dto/            # Data Transfer Objects
│   ├── admin/      # DTOs para administración
│   ├── web/        # DTOs para cliente web
│   └── shared/     # DTOs compartidos
├── entity/         # Entidades JPA
├── mapper/         # MapStruct mappers
├── repository/     # Repositorios Spring Data
└── service/        # Lógica de negocio
    └── impl/       # Implementaciones
```

### 2. Nomenclatura de Entidades

- **Entidades**: `EntityName + Entity` (ej: `CustomerEntity`, `ProductEntity`)
- **Repositorios**: `EntityName + Repository` (ej: `CustomerRepository`)
- **Servicios**: `EntityName + Service` (ej: `CustomerService`)
- **Controladores**: `Context + EntityName + Controller` (ej:
  `AdminCustomerController`, `PublicCustomerController`)

### 3. DTOs por Contexto

- **Admin DTOs**: Para operaciones administrativas
- **Web DTOs**: Para interfaz pública del cliente
- **Shared DTOs**: Para datos compartidos entre contextos

### 4. Mappers con MapStruct

- Usar `@Mapper(componentModel = "spring")`
- Métodos de mapeo específicos por contexto:
    - `toEntity()`, `toAdminResponse()`, `toWebResponse()`
    - `updateFromAdminRequest()`, `updateFromWebRequest()`

---

## 📊 Modelo de Datos Completo

### 🏛️ Entidades por Dominio

#### 1. **Dominio IAM (Identity Access Management)**

**UserEntity** - Usuario del sistema con autenticación

```java

@Entity
@Table(name = "users")
public class UserEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String passwordHash;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles")
    private Set<RoleEntity> roles;
}
```

**RoleEntity** - Roles del sistema

```java

@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnum roleName; // ADMIN, CUSTOMER
}
```

#### 2. **Dominio Customers (Gestión de Clientes)**

**CustomerEntity** - Perfil completo del cliente

```java

@Entity
@Table(name = "customers")
public class CustomerEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String firstName;

    @NotBlank
    @Size(max = 60)
    private String lastName;

    @Email
    @Size(max = 150)
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 9)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DocumentType documentType; // DNI, RUC, etc.

    @NotBlank
    @Size(max = 15)
    @Column(unique = true)
    private String documentNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CustomerType customerType; // NATURAL, COMPANY

    @OneToOne
    private UserEntity userAccount; // opcional

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> addresses;
}
```

**AddressEntity** - Direcciones del cliente

```java

@Entity
@Table(name = "addresses")
public class AddressEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String addressType;

    @NotBlank
    @Size(max = 50)
    private String department;

    @NotBlank
    @Size(max = 50)
    private String province;

    @NotBlank
    @Size(max = 50)
    private String district;

    @NotBlank
    @Size(max = 120)
    private String addressLine;

    @Size(max = 120)
    private String addressReference;

    @Size(max = 9)
    private String addressPhone;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CustomerEntity customer;
}
```

#### 3. **Dominio Catalog (Catálogo de Productos)**

**ProductEntity** - Producto base del catálogo

```java

@Entity
@Table(name = "products")
public class ProductEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageEntity> images;

    @ManyToMany
    @JoinTable(name = "product_categories")
    private List<CategoryEntity> categories;
}
```

**CategoryEntity** - Categorías jerárquicas

```java

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    private String name;

    @ManyToOne
    private CategoryEntity parentCategory;

    @ManyToMany(mappedBy = "categories")
    private List<ProductEntity> products;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> subCategories;
}
```

**ProductImageEntity** - Imágenes de productos

```java

@Entity
@Table(name = "product_images")
public class ProductImageEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String url;

    @NotNull
    @Positive
    private Byte position;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProductEntity product;
}
```

#### 4. **Dominio Attributes (Atributos de Productos)**

**AttributeEntity** - Atributos como Color, Talla

```java

@Entity
@Table(name = "attributes")
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeValueEntity> values;
}
```

**AttributeValueEntity** - Valores específicos de atributos

```java

@Entity
@Table(name = "attribute_values")
public class AttributeValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String attributeValueName;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AttributeEntity attribute;
}
```

#### 5. **Dominio Variants (Variantes de Productos)**

**VariantEntity** - Variante comprable específica

```java

@Entity
@Table(name = "variants")
public class VariantEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(unique = true)
    private String sku;

    @NotNull
    @Digits(integer = 8, fraction = 2)
    @PositiveOrZero
    private BigDecimal price;

    @Digits(integer = 8, fraction = 2)
    @PositiveOrZero
    private BigDecimal compareAtPrice;

    @ManyToOne(optional = false)
    private ProductEntity product;

    @ManyToMany
    @JoinTable(name = "variant_attribute_values")
    private Set<AttributeValueEntity> attributeValues;

    @OneToOne(mappedBy = "variant")
    private InventoryEntity inventory;
}
```

#### 6. **Dominio Inventory (Gestión de Inventario)**

**InventoryEntity** - Control de stock por variante

```java

@Entity
@Table(name = "inventory")
public class InventoryEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    @Column(name = "qty_stock")
    private int quantityOnHand;

    @PositiveOrZero
    @Column(name = "qty_reserved")
    private int reservedQuantity;

    @Positive
    private int reorderPoint;

    @Positive
    private int safetyStock;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private VariantEntity variant;
}
```

#### 7. **Dominio Orders (Gestión de Órdenes)**

**OrderEntity** - Orden de compra principal

```java

@Entity
@Table(name = "orders")
public class OrderEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(unique = true)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, CONFIRMED, PREPARING, SHIPPED, DELIVERED, CANCELED

    @NotNull
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // STORE_PICKUP, HOME_DELIVERY

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private AddressEntity shippingAddress; // nullable para STORE_PICKUP

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderDetailEntity detail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;
}
```

**OrderDetailEntity** - Detalle financiero de la orden

```java

@Entity
@Table(name = "order_details")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private BigDecimal subtotal;

    @NotNull
    @PositiveOrZero
    private BigDecimal shippingCost;

    @NotNull
    @PositiveOrZero
    private BigDecimal discount;

    @NotNull
    @Positive
    private BigDecimal total;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private OrderEntity order;
}
```

**OrderItemEntity** - Ítem individual de la orden

```java

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private Short quantity;

    @NotNull
    @Positive
    private BigDecimal unitPrice;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrderEntity order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private VariantEntity variant;
}
```

#### 8. **Dominio Billing (Facturación y Pagos)**

**Payment** - Registro de pagos

```java

@Entity
@Table(name = "payments")
public class Payment extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency; // PEN, USD, etc.

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // CASH, CARD, TRANSFER, etc.

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, COMPLETED, FAILED, REFUNDED

    @Size(max = 100)
    private String transactionId; // gateway ID

    @PastOrPresent
    private OffsetDateTime paidAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrderEntity order;
}
```

**Invoice** - Comprobantes electrónicos

```java

@Entity
@Table(name = "invoices")
public class Invoice extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceType type; // BOLETA, FACTURA, NOTA_CREDITO

    @NotBlank
    @Size(max = 10)
    private String serie;

    @NotBlank
    @Size(max = 20)
    private String number;

    @PastOrPresent
    private OffsetDateTime issuedAt;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 10, fraction = 2)
    private BigDecimal totalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status; // DRAFT, ISSUED, VOID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrderEntity order;
}
```

### 🏗️ Entidad Base Auditable

**AuditableEntity** - Clase padre para auditoría automática

```java

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}
```

### 📋 Enumeraciones Principales

```java
// IAM
public enum RoleEnum {
    ADMIN, CUSTOMER
}

// Customers  
public enum DocumentType {
    DNI, RUC, PASSPORT
}

public enum CustomerType {
    NATURAL, COMPANY
}

// Orders
public enum OrderStatus {
    PENDING, CONFIRMED, PREPARING, SHIPPED, DELIVERED, CANCELED
}

public enum DeliveryType {
    STORE_PICKUP, HOME_DELIVERY
}

// Billing
public enum PaymentMethod {
    CASH, CARD, TRANSFER, YAPE, PLIN
}

public enum PaymentStatus {
    PENDING, COMPLETED, FAILED, REFUNDED
}

public enum InvoiceType {
    BOLETA, FACTURA, NOTA_CREDITO
}

public enum InvoiceStatus {
    DRAFT, ISSUED, VOID
}

public enum CurrencyCode {
    PEN, USD, EUR
}
```

---

## 🔒 Seguridad

### Configuración de Seguridad

- **Autenticación**: HTTP Basic (desarrollo), preparado para JWT
- **Autorización**: Basada en roles (ADMIN, CUSTOMER)
- **Endpoints públicos**: `/api/public/**`, `/api/auth/**`
- **Endpoints admin**: `/api/admin/**`
- **CORS**: Configurado para desarrollo frontend

### Roles del Sistema

- **ADMIN**: Acceso completo al sistema
- **CUSTOMER**: Acceso a funciones de cliente

---

## 🌐 Estructura de API

### Patrones de Endpoints

#### Para Administración

```
/api/admin/{entity}
├── GET    /               # Listar con paginación
├── GET    /{id}           # Obtener por ID
├── POST   /               # Crear nuevo
├── PUT    /{id}           # Actualizar completo
├── PATCH  /{id}           # Actualización parcial
├── DELETE /{id}           # Eliminar
└── GET    /search?q=...   # Buscar
```

#### Para Cliente Web

```
/api/public/{entity}
├── GET    /               # Listar público
├── GET    /{id}           # Detalle público
└── POST   /register       # Registro de cliente
```

#### Para Cliente Autenticado

```
/api/customer
├── GET    /profile        # Perfil actual
├── PUT    /profile        # Actualizar perfil
├── PATCH  /profile        # Actualización parcial
└── POST   /profile        # Crear/actualizar perfil
```

---

## 📝 Buenas Prácticas de Desarrollo

### 1. Manejo de Excepciones

```java
// Excepciones personalizadas
ResourceNotFoundException // 404
        ConflictException // 409
GlobalExceptionHandler // manejo centralizado
```

### 2. Validaciones

```java
// En entidades y DTOs
@NotBlank
@NotNull
@Email
@Size
@Positive
@Valid // en métodos de controladores
```

### 3. Transacciones

```java
// En servicios
@Transactional // en métodos de escritura
@Transactional(readOnly = true) // en métodos de solo lectura
```

### 4. Paginación

```java
// En repositorios y servicios
Page<Entity> findAll(Pageable pageable);

List<Dto> toResponseList(Page<Entity> page);
```

### 5. Inicialización de Datos

```java
// Clases en package data
@Component
@PostConstruct
public void initData() {
    // datos de prueba y configuración inicial
}
```

---

## 🚀 Configuración de Desarrollo

### Base de Datos

```properties
# PostgreSQL local
spring.datasource.url=jdbc:postgresql://localhost:5432/marbellin
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=create-drop
```

### Perfiles de Aplicación

- **Desarrollo**: `create-drop` con datos de prueba
- **Producción**: `validate` con migraciones controladas

---

## 📁 Gestión de Archivos

### Subida de Imágenes

- **Directorio**: `uploads/products/`
- **Categorías**: `bikinis/`, `cacheteros/`, `semi/`, `topsitos/`
- **Controlador**: `UploadController` para gestión de archivos

---

## 🧪 Testing

### Estructura de Tests

```
// Tests unitarios por módulo
src/test/java/com/marbellin/
        ├── {module}/service/       # Tests de servicios
        ├── {module}/repository/    # Tests de repositorios
        └── {module}/controller/    # Tests de controladores
```

---

## 📚 Documentación API

### Swagger/OpenAPI

- **URL**: `http://localhost:8080/swagger-ui.html`
- **Configuración**: `SwaggerConfig`
- **Anotaciones**: `@Operation`, `@ApiResponse`, `@Tag`

---

## 🔄 Ciclo de Desarrollo

### 1. Para Nuevas Funcionalidades

1. Crear entidad en `entity/`
2. Definir repositorio en `repository/`
3. Crear DTOs por contexto en `dto/`
4. Implementar mapper en `mapper/`
5. Desarrollar servicio en `service/`
6. Crear controladores por contexto
7. Agregar tests correspondientes

### 2. Para Modificaciones

1. Actualizar entidad si es necesario
2. Modificar DTOs afectados
3. Actualizar mappers
4. Ajustar lógica de servicio
5. Validar controladores
6. Ejecutar tests

### 3. Para Nuevos Endpoints

1. Definir en interfaz de servicio
2. Implementar en service impl
3. Crear endpoint en controlador correspondiente
4. Documentar con Swagger
5. Agregar tests de integración

---

## ⚠️ Consideraciones Importantes

### 1. **Contextos de API**

- Separar lógica entre admin y web
- DTOs específicos por contexto
- Validaciones diferenciadas

### 2. **Gestión de Estado**

- Entidades con estados (OrderStatus, PaymentStatus)
- Transiciones controladas en servicios

### 3. **Relaciones de Entidades**

- Lazy loading por defecto
- Cascade apropiado para eliminaciones
- Manejo de relaciones bidireccionales

### 4. **Performance**

- Paginación en todas las listas
- Proyecciones con DTOs
- Queries optimizadas en repositorios

---

## 🎯 Objetivos del Proyecto

Este backend busca:

- **Escalabilidad**: Arquitectura modular y limpia
- **Mantenibilidad**: Separación de responsabilidades clara
- **Flexibilidad**: APIs preparadas para múltiples frontends
- **Robustez**: Manejo completo de errores y validaciones
- **Productividad**: Automatización con MapStruct y Spring Boot

---

## 💡 Recomendaciones para GitHub Copilot

Al trabajar con este proyecto:

1. **Respeta la estructura modular** establecida
2. **Usa los patrones de nomenclatura** consistentes
3. **Implementa validaciones** apropiadas en DTOs y entidades
4. **Maneja excepciones** con las clases personalizadas
5. **Documenta endpoints** con anotaciones Swagger
6. **Sigue el patrón de transacciones** en servicios
7. **Mantén separación** entre contextos admin/web/public
8. **Usa MapStruct** para conversiones entre capas
9. **Implementa tests** para nueva funcionalidad
10. **Considera performance** con paginación y lazy loading

---

## 🔧 Configuraciones Adicionales Importantes

### Configuración de JPA

```properties
# Configuraciones clave en application.properties
spring.jpa.hibernate.ddl-auto=create-drop  # Solo desarrollo
spring.jpa.show-sql=true                   # Debug SQL
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.time_zone=America/Lima
```

### Configuración de Uploads

```properties
app.upload.dir.products=uploads/products
# Estructura: uploads/products/{categoria}/{archivo}
# Categorías: bikinis/, cacheteros/, semi/, topsitos/
```

### Configuración de CORS

- Permitir origins para desarrollo frontend
- Métodos: GET, POST, PUT, PATCH, DELETE
- Headers: Authorization, Content-Type

---

## 📐 Patrones de Diseño Implementados

### 1. **Repository Pattern**

```java
public interface EntityRepository extends JpaRepository<Entity, Long> {
    // Métodos de consulta personalizados
    Page<Entity> findBySearchCriteria(String criteria, Pageable pageable);

    Optional<Entity> findByUniqueField(String field);
}
```

### 2. **Service Layer Pattern**

```java

@Service
@Transactional
public class EntityServiceImpl implements EntityService {
    // Lógica de negocio
    // Manejo de transacciones
    // Validaciones de dominio
}
```

### 3. **DTO Pattern con MapStruct**

```java

@Mapper(componentModel = "spring")
public interface EntityMapper {
    EntityAdminResponse toAdminResponse(Entity entity);

    EntityWebResponse toWebResponse(Entity entity);

    Entity toEntity(EntityCreateRequest request);

    void updateFromRequest(EntityUpdateRequest request, @MappingTarget Entity entity);
}
```

### 4. **Controller Pattern**

```java

@RestController
@RequestMapping("/api/{context}/{entity}")
@RequiredArgsConstructor
@Tag(name = "Entity Management")
public class ContextEntityController {
    // Endpoints RESTful estándar
    // Documentación Swagger
    // Validación de entrada
}
```

---

## 🎨 Convenciones de Naming Específicas

### Entidades y Tablas

- **Entidades**: `EntityNameEntity` (ej: `CustomerEntity`)
- **Tablas**: `snake_case` plural (ej: `customers`, `order_items`)
- **Columnas**: `snake_case` (ej: `first_name`, `created_at`)

### DTOs por Contexto

```java
// Admin context
CustomerAdminRequest
        CustomerAdminResponse
CustomerAdminPatchRequest

// Web context  
        CustomerWebRequest
CustomerWebResponse
        CustomerWebPatchRequest

// Shared context
CustomerSharedDto
        AddressDto
```

### Servicios e Interfaces

```java
// Interface
public interface CustomerService {
    CustomerAdminResponse createCustomer(CustomerAdminRequest request);
}

// Implementación
@Service
public class CustomerServiceImpl implements CustomerService {
    // Implementación
}
```

---

## 🔄 Flujos de Datos Importantes

### 1. **Flujo de Creación de Orden**

```
1. Cliente → OrderCreateRequest
2. Validar Customer existente
3. Validar VariantEntity disponibles y stock
4. Reservar inventario (reservedQuantity)
5. Crear OrderEntity con OrderItems
6. Generar OrderDetailEntity con cálculos
7. Retornar OrderResponse
```

### 2. **Flujo de Pago**

```
1. OrderEntity PENDING → Payment PENDING
2. Procesar pago con gateway
3. Payment COMPLETED → OrderEntity CONFIRMED
4. Crear Invoice (BOLETA/FACTURA)
5. Actualizar stock final (quantityOnHand - reservedQuantity)
```

### 3. **Flujo de Atributos y Variantes**

```
1. AttributeEntity (ej: Color, Talla)
2. AttributeValueEntity (ej: Rojo, M)
3. VariantEntity vincula Product + AttributeValues
4. InventoryEntity vincula 1:1 con VariantEntity
```

---

## 🛡️ Validaciones y Constraints

### Validaciones de Negocio

```java
// En entidades
@NotBlank,@NotNull,@Email,@Size(max = N)
@Positive,@PositiveOrZero,@Digits(integer = X, fraction = Y)
@PastOrPresent,
@Future

// En DTOs de request
@Valid // para objetos anidados
@Pattern // para formatos específicos

// Constraints de BD
@Column(unique = true, nullable = false, length = N)
@JoinColumn(nullable = false) // para relaciones requeridas
```

### Manejo de Unique Constraints

```java
// En servicios, capturar DataIntegrityViolationException
// Convertir a ConflictException para el cliente
try{
        repository.save(entity);
}catch(
DataIntegrityViolationException e){
        throw new

ConflictException("Email ya registrado");
}
```

---

## 🧩 Extensiones y Personalizaciones

### Custom Queries en Repositorios

```java

@Query("SELECT p FROM ProductEntity p JOIN p.categories c WHERE c.name = :categoryName")
Page<ProductEntity> findByCategory(@Param("categoryName") String categoryName, Pageable pageable);

@Query(value = "SELECT * FROM variants v WHERE v.sku LIKE %?1%", nativeQuery = true)
List<VariantEntity> searchBySku(String sku);
```

### Configuraciones de Seguridad Específicas

```java
// En SecurityConfig
.requestMatchers("/api/admin/**").

hasRole("ADMIN")
.

requestMatchers("/api/customer/**").

hasRole("CUSTOMER")
.

requestMatchers("/api/public/**").

permitAll()
.

requestMatchers("/api/auth/**").

permitAll()
```

### Data Initialization

```java
// Clases en package data con @Component
@PostConstruct
public void initData() {
    // Crear datos de prueba
    // Roles, categorías, atributos básicos
}
```

---

## 🎯 Objetivos de Calidad del Código

### Performance

- **Lazy Loading** por defecto en @ManyToOne y @OneToOne
- **Paginación** obligatoria en endpoints de listado
- **Proyecciones DTO** para evitar N+1 queries
- **@Transactional** apropiado según operación

### Mantenibilidad

- **Separación clara** entre capas (Controller → Service → Repository)
- **DTOs específicos** por contexto (admin/web/shared)
- **Mappers automáticos** con MapStruct
- **Documentación Swagger** en todos los endpoints

### Robustez

- **Validaciones** en múltiples capas
- **Manejo centralizado** de excepciones
- **Testing** unitario e integración
- **Constraints de BD** respaldando validaciones

---

## 🚀 Próximas Extensiones Planificadas

### Funcionalidades Adicionales

- **Carrito de compras** temporal (SessionCart)
- **Dashboard analytics** para admin
- **Integración con pasarelas** de pago peruanas

---

## 🔧 Dominio Common - Componentes Compartidos

### 🛡️ **Security**

- **CustomUserDetailsService**: Servicio de autenticación personalizado
- **CustomUserDetails**: Implementación de UserDetails para Spring Security
- **SecurityConfig**: Configuración centralizada de seguridad

### 📂 **Storage**

- **FileStorageService**: Interface para gestión de archivos
- **LocalFileStorageService**: Implementación local para almacenamiento
- **UploadController**: Endpoint para subida de imágenes de productos

### ⚠️ **Exception Handling**

```java
// Jerarquía de excepciones
-ResourceNotFoundException(404)
-

ConflictException(409)  
-

GlobalExceptionHandler(@RestControllerAdvice)
-

ApiError(estructura de respuesta de error)
```

### 🔧 **Configuration**

- **SwaggerConfig**: Configuración de documentación OpenAPI
- **CorsConfig**: Configuración CORS para desarrollo
- **WebConfig**: Configuraciones web adicionales

---

## 📋 Patrones de Repositorios por Dominio

### Repositorios Implementados

```java
// Todos extienden JpaRepository<Entity, Long>

// IAM
UserRepository,
        RoleRepository

// Customers  
                CustomerRepository, AddressRepository

// Catalog
ProductRepository,CategoryRepository,
        ProductImageRepository

// Attributes
                AttributeRepository, AttributeValueRepository

// Variants & Inventory
VariantRepository,
        InventoryRepository

// Orders
                OrderRepository, OrderDetailRepository, OrderItemRepository

// Billing
PaymentRepository,InvoiceRepository
```

### Métodos Comunes en Repositorios

```java
// Búsquedas por campos únicos
Optional<Entity> findByUniqueField(String field);

boolean existsByUniqueField(String field);

// Validaciones para updates (evitar duplicados)
boolean existsByFieldAndIdNot(String field, Long id);

// Búsquedas con paginación
Page<Entity> findBySearchCriteria(String criteria, Pageable pageable);

// Custom queries con @Query
@Query("SELECT e FROM Entity e WHERE e.field = :param")
List<Entity> findCustomMethod(@Param("param") String param);
```

---

## 🗺️ Mappers MapStruct por Dominio

### Estructura de Mappers

```java

@Mapper(
        componentModel = "spring",
        uses = {DependentMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EntityMapper {
    // Conversiones básicas
    Entity toEntity(EntityCreateRequest request);

    EntityAdminResponse toAdminResponse(Entity entity);

    EntityWebResponse toWebResponse(Entity entity);

    // Updates con @MappingTarget
    void updateFromAdminRequest(EntityAdminRequest request, @MappingTarget Entity entity);

    void updateFromWebRequest(EntityWebRequest request, @MappingTarget Entity entity);

    // Listas
    List<EntityResponse> toResponseList(List<Entity> entities);

    Page<EntityResponse> toResponsePage(Page<Entity> entities);
}
```

### Mappers Implementados

- **CustomerMapper** (uses AddressMapper)
- **AddressMapper**
- **ProductMapper** (uses CategoryMapper, ProductImageMapper)
- **CategoryMapper**
- **ProductImageMapper**
- **AttributeMapper** (uses AttributeValueMapper)
- **AttributeValueMapper**

---

## 🌊 Flujos de Negocio Detallados

### 1. **Registro de Cliente**

```
1. AuthController.register() → UserEntity CUSTOMER
2. PublicCustomerController.register() → CustomerEntity
3. Vincular UserEntity.id con CustomerEntity.userAccount
4. Validar unicidad de email y documentNumber
5. Hashear contraseña con BCrypt
```

### 2. **Gestión de Productos y Variantes**

```
1. Crear ProductEntity con categorías
2. Subir imágenes → ProductImageEntity
3. Configurar AttributeEntity (Color, Talla)
4. Crear AttributeValueEntity (Rojo, M, L)
5. Generar VariantEntity (Product + AttributeValues)
6. Inicializar InventoryEntity para cada variante
```

### 3. **Proceso de Compra Completo**

```
1. Cliente navega catálogo público
2. Selecciona variantes → valida stock disponible
3. Crea OrderEntity PENDING
4. Reserva inventory (reservedQuantity++)
5. Procesa Payment → OrderEntity CONFIRMED
6. Genera Invoice electrónica
7. Actualiza stock final (quantityOnHand--)
8. Transición a PREPARING → SHIPPED → DELIVERED
```

---

## 🔐 Seguridad y Autorización Detallada

### Configuración de Endpoints

```java
// Públicos (sin autenticación)
/api/public/** - Catálogo, productos, categorías
 /api/auth/** - Registro, login
 /swagger-ui/** - Documentación

 // Admin (rol ADMIN)
 /api/admin/** - Gestión completa del sistema

 // Customer (rol CUSTOMER) 
 /api/customer/** - Perfil, órdenes propias

 // Upload (authenticated)
 /api/upload/** - Subida de archivos
```

### UserDetails Personalizado

```java
CustomUserDetails implements

UserDetails {
    -UserEntity user
            - Collection < GrantedAuthority > authorities(from roles)
            - boolean accountNonExpired = true
            - boolean credentialsNonExpired = true
            - boolean enabled = user.enabled
}
```

---

## 📊 Gestión de Estados y Transiciones

### Estados de Orden (OrderStatus)

```java
PENDING →CONFIRMED →PREPARING →SHIPPED →DELIVERED
              ↓

CANCELED(desde cualquier estado pre-envío)
```

### Estados de Pago (PaymentStatus)

```java
PENDING →COMPLETED
    ↓         ↓
FAILED REFUNDED
```

### Estados de Factura (InvoiceStatus)

```java
DRAFT →ISSUED →VOID
```

---

## 🎨 Convenciones de DTOs Específicas

### Nomenclatura por Contexto

```java
// Admin Context (gestión completa)
{Entity}AdminRequest -Crear/
actualizar completo
{Entity}AdminResponse -
Respuesta completa
con relaciones
{Entity}AdminPatchRequest -
Actualización parcial

// Web Context (cliente público)  
{Entity}WebRequest -
Datos básicos
para cliente
{Entity}WebResponse -
Vista pública
sin datos

sensibles {
    Entity
}

WebPatchRequest -
Actualización limitada

// Shared Context
{Entity}Dto -
DTOs compartidos
entre contextos
{Entity}Summary -
Versiones resumidas
para listas
```

### Validaciones por Contexto

```java
// AdminRequest - Validaciones completas
@NotNull,@NotBlank,@Email,@Size,@Positive,@Valid

// WebRequest - Validaciones básicas del cliente
@NotBlank,@Email,@Size,@Pattern

// PatchRequest - Validaciones opcionales
// Campos nullable para updates parciales
```

---

## 🎀 Contexto del Negocio - Lencería Marbellin

### 📦 **Categorías de Productos**

```java
// Categorías principales implementadas
-Bikinis:
Bikini completos
de dos
piezas
-Cacheteros:
Ropa interior
femenina básica
-Semi:
Productos semi-
íntimos estilo
semi-hilo
-Topsitos:
Tops y
sostenes deportivos/casuales
```

### 🎨 **Atributos Específicos del Negocio**

```java
// Attributes implementados para lencería
Color:Rojo,Negro,Blanco,Rosa,Azul,etc.
        Talla:XS,S,M,L,XL,XXL
Material:Algodón,Encaje,Microfibra,etc.
        Estilo:Clásico,Deportivo,Sexy,Casual
```

### 📸 **Gestión de Imágenes por Categoría**

```
uploads/products/
├── bikinis/          # Imágenes de bikinis
├── cacheteros/       # Imágenes de cacheteros  
├── semi/             # Imágenes de semi-hilos
└── topsitos/         # Imágenes de topsitos
```

### 💰 **Contexto de Precios Peruanos**

```java
// Moneda principal: PEN (Soles Peruanos)
// Precios típicos en el mercado:
Cacheteros:S/8.00-S/25.00
Bikinis:S/35.00-S/80.00
Semi:S/12.00-S/30.00
Topsitos:S/18.00-S/45.00
```

---

## 🔍 Análisis Final y Contextos Faltantes

### ✅ **Contextos Completados**

1. **Modelo de Datos Completo** - Todas las entidades con relaciones detalladas
2. **Arquitectura Modular** - Separación clara por dominios de negocio
3. **Patrones de API** - Endpoints admin/public/customer bien definidos
4. **Seguridad** - Configuración Spring Security con roles
5. **Mappers y DTOs** - Convenciones MapStruct por contexto
6. **Repositorios** - Patrones JPA con consultas personalizadas
7. **Validaciones** - Bean Validation en múltiples capas
8. **Excepciones** - Manejo centralizado con tipos específicos
9. **Configuración** - Properties, Docker, perfiles de desarrollo
10. **Testing** - Estructura de tests unitarios e integración

### 🚧 **Contextos que Podrían Mejorarse**

#### 1. **Módulos Pendientes de Implementación**

```java
// Módulos que podrían agregarse en el futuro
-shopping-cart/     #
Carrito temporal
de compras
-wishlist/          #
Lista de
deseos
-promotions/        #
Sistema de
descuentos
-notifications/     #
Notificaciones email/SMS
-analytics/         #
Dashboard y
métricas
-reviews/           #
Reseñas y
calificaciones
-
Integración con
pasarelas de
pago peruanas
```

#### 2. **Configuraciones de Producción**

```properties
# Configuraciones que faltan para producción
spring.jpa.hibernate.ddl-auto=validate
spring.datasource.url=${DATABASE_URL}
logging.level.com.marbellin=INFO
management.endpoints.web.exposure.include=health,metrics
```

#### 3. **Aspectos de Integración**

```java
// Servicios externos por integrar
-PassarellaPago(Culqi, Mercado Pago, PayPal)
-

ServicioEnvio(Olva Courier, Shalom, etc .)
-

FacturacionElectronica(SUNAT Perú)
-

NotificacionesEmail(SendGrid, SES)
-

AlmacenamientoCloud(AWS S3, Google Cloud)
```

#### 4. **Optimizaciones de Performance**

```java
// Mejoras de rendimiento futuras
@Cache
en consultas
frecuentes
@Async
en operaciones
pesadas
Connection pooling
avanzado
Query optimization
con índices
CDN para
imágenes de
productos
```

---

## 📚 Guías Específicas para GitHub Copilot

### 🎯 **Para Desarrollo de Nuevas Funcionalidades**

1. **Siempre seguir el patrón modular**: Un módulo por dominio con su estructura
   completa
2. **DTOs por contexto**: Admin, Web y Shared según la audiencia del endpoint
3. **Validaciones apropiadas**: Bean Validation en DTOs, constraints en BD
4. **Lazy loading**: Preferir LAZY en relaciones @ManyToOne y @OneToOne
5. **Transacciones**: @Transactional en métodos de escritura, SUPPORTS en
   lectura

### 🛠️ **Para Mantenimiento y Mejoras**

1. **Backward compatibility**: No romper contratos de API existentes
2. **Performance first**: Siempre usar paginación en listados
3. **Security by default**: Validar permisos en todos los endpoints
4. **Documentation**: Swagger en endpoints nuevos, actualizar en modificados
5. **Testing**: Tests unitarios obligatorios para lógica de negocio

### 📊 **Para Consultas y Reportes**

1. **Proyecciones**: Usar DTOs para evitar cargar entidades completas
2. **Custom queries**: Preferir @Query sobre métodos dinámicos complejos
3. **Agregaciones**: Usar nativeQuery para consultas complejas de agregación
4. **Índices**: Considerar índices en campos de búsqueda frecuente

### 🔐 **Para Aspectos de Seguridad**

1. **Input validation**: Validar y sanitizar toda entrada del usuario
2. **Authorization**: Verificar permisos a nivel de método y entidad
3. **Sensitive data**: No exponer datos sensibles en logs o respuestas
4. **Password handling**: Usar BCrypt, nunca almacenar texto plano

---

## 🌟 Conclusión

Este proyecto **Marbellin Backend** representa una **arquitectura robusta y
escalable** para un ecommerce especializado en lencería femenina. La separación
modular, los patrones establecidos y las convenciones detalladas garantizan:

- **🎯 Consistencia** en el desarrollo de nuevas funcionalidades
- **🔧 Mantenibilidad** a largo plazo del código base
- **📈 Escalabilidad** para crecimiento del negocio
- **🛡️ Robustez** en la gestión de errores y validaciones
- **⚡ Performance** optimizada para operaciones críticas

**GitHub Copilot** debe usar estas instrucciones como **contexto fundamental**
para generar código que mantenga la calidad y consistencia arquitectónica
establecida en el proyecto.
