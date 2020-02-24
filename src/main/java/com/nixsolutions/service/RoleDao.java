package com.nixsolutions.service;

import com.nixsolutions.domain.Role;

public interface RoleDao {
    void create(Role role);

    void update(Role role);

    void remove(Role role);

    Role findByName(String name);
}
