package com.example.multitenancy.tenant.service;

import com.example.multitenancy.tenant.model.User;

/**
 * @author Xutingzhou
 */
public interface UserService {

    User getUser(long id);

    User addUser(String name);

}
