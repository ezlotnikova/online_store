package com.gmail.ezlotnikova.repository.impl;

import java.lang.invoke.MethodHandles;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.repository.util.PaginationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final PaginationUtil paginationUtil;

    public UserRepositoryImpl(PaginationUtil paginationUtil) {
        this.paginationUtil = paginationUtil;
    }

    @Override
    public Long countAdministrators() {
        String hql = "SELECT COUNT(u.id) FROM User as u " +
                "WHERE u.role LIKE : administratorRole";
        Query query = entityManager.createQuery(hql);
        query.setParameter("administratorRole", UserRoleEnum.ADMINISTRATOR);
        return (Long) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<User> findPaginatedAndOrderedByEmail(Pageable pageRequest) {
        int startPosition = paginationUtil.getQueryStartPosition(
                pageRequest.getPageNumber(), pageRequest.getPageSize());
        int maxResult = pageRequest.getPageSize();
        Long count = countTotal();
        String hql = "FROM User as u ORDER BY u.email";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return new PageImpl<User>(query.getResultList(), pageRequest, count);
    }

    @Override
    public User loadUserByEmail(String email) {
        String hql = "FROM User as U WHERE U.email =:email";
        Query query = entityManager.createQuery(hql);
        query.setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}