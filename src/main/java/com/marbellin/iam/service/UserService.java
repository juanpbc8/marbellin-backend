package com.marbellin.iam.service;

import com.marbellin.iam.entity.UserEntity;
import com.marbellin.iam.entity.enums.RoleEnum;

import java.util.Optional;

public interface UserService {
    UserEntity createUser(String email, String rawPassword, RoleEnum role);

    Optional<UserEntity> findByEmail(String email);
}
