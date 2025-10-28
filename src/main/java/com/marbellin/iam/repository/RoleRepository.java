package com.marbellin.iam.repository;

import com.marbellin.iam.entity.RoleEntity;
import com.marbellin.iam.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(RoleEnum roleName);
}
