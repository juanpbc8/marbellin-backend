package com.marbellin.data;

import com.marbellin.customers.entity.AddressEntity;
import com.marbellin.customers.entity.CustomerEntity;
import com.marbellin.customers.entity.enums.CustomerType;
import com.marbellin.customers.entity.enums.DocumentType;
import com.marbellin.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Component
@Order(1)
@RequiredArgsConstructor
public class CustomerDataInit implements CommandLineRunner {
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            System.out.println("⚠️ Customers ya cargados. Omitiendo inicialización...");
            return;
        }
        System.out.println("🚀 Cargando datos iniciales de Customers + Addresses...");

        List<CustomerEntity> customers = new ArrayList<>();

        // Helper para construir cliente con 1 dirección
        BiFunction<String, String, CustomerEntity> buildCustomer =
                (firstName, lastName) -> CustomerEntity.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com")
                        .phoneNumber(generarTelefono())
                        .documentType(DocumentType.DNI)
                        .documentNumber(generarDni())
                        .customerType(CustomerType.NATURAL)
                        .build();

        // Lista de nombres
        String[][] data = {
                {"Juan", "Pérez"}, {"María", "García"}, {"Luis", "Ramírez"},
                {"Ana", "Torres"}, {"Carlos", "Sánchez"}, {"Rosa", "Flores"},
                {"Miguel", "Vargas"}, {"Lucía", "Cruz"}, {"Pedro", "Díaz"},
                {"Elena", "Medina"}
        };

        for (String[] d : data) {
            CustomerEntity c = buildCustomer.apply(d[0], d[1]);
            AddressEntity a = buildAddress(c);
            c.getAddresses().add(a);
            customers.add(c);
        }

        // Dos clientes con doble dirección
        CustomerEntity c1 = customers.get(0);
        c1.getAddresses().add(buildAddress(c1));

        CustomerEntity c2 = customers.get(1);
        c2.getAddresses().add(buildAddress(c2));

        customerRepository.saveAll(customers);

        System.out.println("✅ Customers y direcciones cargados correctamente.");
    }

    // Helpers estáticos
    private String generarDni() {
        return String.valueOf((int) (Math.random() * 90000000 + 10000000));
    }

    private String generarTelefono() {
        return "9" + (int) (Math.random() * 9000000 + 1000000);
    }

    private AddressEntity buildAddress(CustomerEntity c) {
        String[][] direcciones = {
                {"Casa", "Lima", "Lima", "Miraflores", "Av. Larco 123", "Frente al parque Kennedy"},
                {"Casa", "Lima", "Lima", "Surco", "Calle Los Jazmines 456", "Cerca al óvalo Higuereta"},
                {"Departamento", "Lima", "Lima", "San Miguel", "Av. La Marina 789", "Altura Plaza San Miguel"},
                {"Casa", "Arequipa", "Arequipa", "Yanahuara", "Jr. Cusco 101", "Por la plaza de Yanahuara"},
                {"Casa", "Cusco", "Cusco", "Wanchaq", "Av. Cultura 999", "Frente a la UNSAAC"}
        };

        int r = (int) (Math.random() * direcciones.length);

        return AddressEntity.builder()
                .addressType(direcciones[r][0])
                .department(direcciones[r][1])
                .province(direcciones[r][2])
                .district(direcciones[r][3])
                .addressLine(direcciones[r][4])
                .addressReference(direcciones[r][5])
                .addressPhone(generarTelefono())
                .customer(c)
                .build();
    }
}
