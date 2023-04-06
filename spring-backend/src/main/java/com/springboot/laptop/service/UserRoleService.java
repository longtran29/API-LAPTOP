package com.springboot.laptop.service;

import com.springboot.laptop.model.UserRoleEntity;

public interface UserRoleService {

    public UserRoleEntity getUserRoleByEnumName(String name) throws Exception;
}
