package com.betsapp.usr.repositories;

import org.springframework.data.repository.CrudRepository;

import com.betsapp.usr.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{

    User findByUserName(String username);

}
