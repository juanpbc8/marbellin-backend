package com.marbellin.auth.repository;

import com.marbellin.auth.entity.RoleEntity;
import com.marbellin.auth.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(RoleEnum roleName);
}
