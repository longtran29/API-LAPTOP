package com.springboot.laptop.service;

import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.enums.UserRoleEnum;

public interface UserRoleService {

    public UserRoleEntity getUserRoleByEnumName(UserRoleEnum name) throws Exception;
}
