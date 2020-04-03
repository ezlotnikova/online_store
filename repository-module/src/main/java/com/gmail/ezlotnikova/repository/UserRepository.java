package com.gmail.ezlotnikova.repository;

import com.gmail.ezlotnikova.repository.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends GenericRepository<Long, User> {

    Long countAdministrators();

    Page<User> findPaginatedAndOrderedByEmail(Pageable pageRequest);

    User loadUserByEmail(String email);

}