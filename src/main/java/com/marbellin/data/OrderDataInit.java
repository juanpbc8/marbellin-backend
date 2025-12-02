package com.marbellin.data;

import com.marbellin.billing.entity.InvoiceEntity;
import com.marbellin.billing.entity.PaymentEntity;
import com.marbellin.billing.entity.enums.*;
import com.marbellin.catalog.entity.ProductEntity;
import com.marbellin.catalog.entity.ProductVariantEntity; // Importante
import com.marbellin.catalog.repository.ProductRepository;
import com.marbellin.customers.entity.AddressEntity;
import com.marbellin.customers.entity.CustomerEntity;
import com.marbellin.customers.repository.CustomerRepository;
import com.marbellin.orders.entity.OrderEntity;
import com.marbellin.orders.entity.OrderItemEntity;
import com.marbellin.orders.entity.enums.DeliveryType;
import com.marbellin.orders.entity.enums.OrderStatus;
import com.marbellin.orders.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Order(4) // Aumenté a 4 para asegurar que corra después de CatalogDataInit (que es 3)
@RequiredArgsConstructor
public class OrderDataInit implements CommandLineRunner {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        if (orderRepository.count() > 0) {
            System.out.println("⚠️ Órdenes ya inicializadas. Omitiendo carga...");
            return;
        }

        System.out.println("🚀 Generando 30 Órdenes Históricas para Marbellin...");

        List<CustomerEntity> customers = customerRepository.findAll();
        List<ProductEntity> products = productRepository.findAll();

        if (customers.isEmpty() || products.isEmpty()) {
            System.out.println("❌ No hay clientes o productos para generar órdenes.");
            return;
        }

        Random random = new Random();
        int totalOrdersToCreate = 30; // Límite solicitado

        for (int i = 0; i < totalOrdersToCreate; i++) {
            // 1. Elegir cliente aleatorio
            CustomerEntity randomCustomer = customers.get(random.nextInt(customers.size()));

            // 2. Definir escenario de éxito/fracaso
            int scenario = random.nextInt(10);

            // 3. Crear orden
            createAndHackOrder(randomCustomer, products, random, scenario);
        }

        System.out.println("✅ Carga completa: 30 órdenes creadas con variantes y fechas distribuidas.");
    }

    private void createAndHackOrder(CustomerEntity customer, List<ProductEntity> products, Random random, int scenario) {
        // --- 1. GENERACIÓN DE FECHA RETROACTIVA ---
        int daysAgo = random.nextInt(14); // Últimos 14 días
        int hoursAgo = random.nextInt(20) + 1;

        LocalDateTime date = LocalDateTime.now()
                .minusDays(daysAgo)
                .minusHours(hoursAgo);

        // --- 2. LÓGICA DE NEGOCIO ---
        boolean isDelivery = random.nextBoolean();
        DeliveryType deliveryType = isDelivery ? DeliveryType.A_DOMICILIO : DeliveryType.RECOJO_EN_TIENDA;

        // Si es delivery, intentamos asignar dirección. Si no tiene, forzamos recojo en tienda.
        AddressEntity address = null;
        if (isDelivery && !customer.getAddresses().isEmpty()) {
            address = customer.getAddresses().get(random.nextInt(customer.getAddresses().size()));
        } else {
            deliveryType = DeliveryType.RECOJO_EN_TIENDA;
        }

        BigDecimal shippingCost = (deliveryType == DeliveryType.A_DOMICILIO) ? new BigDecimal("15.00") : BigDecimal.ZERO;

        OrderStatus orderStatus;
        PaymentStatus paymentStatus;
        boolean generateInvoice;

        // Distribución de Estados
        if (scenario == 0 || scenario == 1) { // 20% Canceladas
            orderStatus = OrderStatus.CANCELADO;
            paymentStatus = PaymentStatus.RECHAZADO;
            generateInvoice = false;
        } else if (scenario == 2) { // 10% Pendientes
            orderStatus = OrderStatus.PENDIENTE;
            paymentStatus = PaymentStatus.PENDIENTE;
            generateInvoice = false;
        } else { // 70% Exitosas
            orderStatus = getRandomSuccessStatus(random);
            paymentStatus = PaymentStatus.CONFIRMADO;
            generateInvoice = true;
        }

        // 2.1 Construir Cabecera de Orden
        OrderEntity order = OrderEntity.builder()
                .code("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(orderStatus)
                .deliveryType(deliveryType)
                .customer(customer)
                .shippingAddress(address)
                .shippingCost(shippingCost)
                .discount(BigDecimal.ZERO)
                .build();

        // 2.2 Items (CON VARIANTES)
        List<OrderItemEntity> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        // Barajar productos para elegir al azar
        List<ProductEntity> shuffledProducts = new ArrayList<>(products);
        Collections.shuffle(shuffledProducts);

        int itemCount = random.nextInt(3) + 1; // De 1 a 3 items por orden

        for (int k = 0; k < itemCount; k++) {
            if (k >= shuffledProducts.size()) break;

            ProductEntity p = shuffledProducts.get(k);

            // Validar que el producto tenga variantes (por seguridad)
            if (p.getVariants() == null || p.getVariants().isEmpty()) continue;

            // --- SELECCIONAR VARIANTE ALEATORIA ---
            ProductVariantEntity randomVariant = p.getVariants().get(random.nextInt(p.getVariants().size()));

            int qty = (random.nextInt(2) + 1); // Cantidad 1 o 2

            OrderItemEntity item = OrderItemEntity.builder()
                    .quantity(qty)
                    .unitPrice(p.getPrice())
                    .product(p)
                    .order(order)
                    // --- SNAPSHOT DE LA VARIANTE ---
                    .selectedSize(randomVariant.getSize())
                    .selectedColor(randomVariant.getColor())
                    .variantSku(randomVariant.getSku())
                    // -------------------------------
                    .build();

            items.add(item);
            subtotal = subtotal.add(p.getPrice().multiply(BigDecimal.valueOf(qty)));
        }

        // Si por alguna razón no se agregaron items (ej. productos sin variantes), abortamos esta orden
        if (items.isEmpty()) return;

        order.setItems(items);
        order.setSubtotal(subtotal);
        order.setTotal(subtotal.add(shippingCost));

        // 2.3 Pago
        PaymentEntity payment = PaymentEntity.builder()
                .amount(order.getTotal())
                .currency(CurrencyCode.PEN)
                .method(getRandomPaymentMethod(random))
                .status(paymentStatus)
                .transactionId(UUID.randomUUID().toString())
                .order(order)
                .build();

        if (paymentStatus == PaymentStatus.CONFIRMADO) {
            payment.setPaidAt(date.plusMinutes(2));
        }
        order.setPayments(new ArrayList<>(List.of(payment)));

        // 2.4 Factura
        if (generateInvoice) {
            InvoiceEntity invoice = InvoiceEntity.builder()
                    .type(InvoiceType.BOLETA)
                    .serie("B001")
                    .number(String.format("%08d", random.nextInt(999999)))
                    .issuedAt(date.plusMinutes(5))
                    .totalAmount(order.getTotal())
                    .currency(CurrencyCode.PEN)
                    .status(InvoiceStatus.EMITIDO)
                    .hashValue(UUID.randomUUID().toString())
                    .documentUrl("https://marbellin-cdn.com/invoices/" + order.getCode() + ".pdf")
                    .order(order)
                    .build();
            order.setInvoice(invoice);
        }

        // --- 3. PERSISTENCIA Y HACK DE FECHAS (Time Travel) ---

        // A. Guardar orden (Cascade guardará items, pagos y factura)
        orderRepository.save(order);

        // B. Hackear fecha de creación de la orden
        entityManager.createNativeQuery("UPDATE orders SET created_at = :date WHERE id = :id")
                .setParameter("date", date)
                .setParameter("id", order.getId())
                .executeUpdate();

        // C. Hackear fecha de factura
        if (order.getInvoice() != null) {
            entityManager.createNativeQuery("UPDATE invoices SET created_at = :date WHERE id = :id")
                    .setParameter("date", date)
                    .setParameter("id", order.getInvoice().getId())
                    .executeUpdate();
        }

        // D. Hackear fecha de pagos
        for (PaymentEntity p : order.getPayments()) {
            entityManager.createNativeQuery("UPDATE payments SET created_at = :date WHERE id = :id")
                    .setParameter("date", date)
                    .setParameter("id", p.getId())
                    .executeUpdate();
        }
    }

    private OrderStatus getRandomSuccessStatus(Random random) {
        OrderStatus[] successStatuses = {OrderStatus.CONFIRMADO, OrderStatus.PREPARANDO, OrderStatus.ENVIADO, OrderStatus.ENTREGADO};
        return successStatuses[random.nextInt(successStatuses.length)];
    }

    private PaymentMethod getRandomPaymentMethod(Random random) {
        return PaymentMethod.values()[random.nextInt(PaymentMethod.values().length)];
    }
}
