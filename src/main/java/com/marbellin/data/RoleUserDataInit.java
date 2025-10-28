package com.marbellin.data;

import com.marbellin.iam.entity.RoleEntity;
import com.marbellin.iam.entity.enums.RoleEnum;
import com.marbellin.iam.repository.RoleRepository;
import com.marbellin.iam.repository.UserRepository;
import com.marbellin.iam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@RequiredArgsConstructor
public class RoleUserDataInit implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void run(String... args) {
        System.out.println("🚀 Inicializando roles y usuario admin...");

        // 1️⃣ Crear roles si no existen
        for (RoleEnum roleEnum : RoleEnum.values()) {
            roleRepository.findByRoleName(roleEnum)
                    .orElseGet(() -> {
                        RoleEntity role = RoleEntity.builder()
                                .roleName(roleEnum)
                                .build();
                        return roleRepository.save(role);
                    });
        }

        // 2️⃣ Crear usuario admin por defecto
        if (userRepository.findByEmail("admin@marbellin.com").isEmpty()) {
            userService.createUser("admin@marbellin.com", "admin123", RoleEnum.ADMIN);
            System.out.println("✅ Usuario admin creado: admin@marbellin.com / admin123");
        } else {
            System.out.println("⚠️ Usuario admin ya existe, se omite creación.");
        }
    }
}
