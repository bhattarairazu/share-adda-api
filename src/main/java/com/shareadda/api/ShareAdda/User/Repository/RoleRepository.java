package com.shareadda.api.ShareAdda.User.Repository;

import com.shareadda.api.ShareAdda.User.Domain.ERole;
import com.shareadda.api.ShareAdda.User.Domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByName(ERole name);
}
