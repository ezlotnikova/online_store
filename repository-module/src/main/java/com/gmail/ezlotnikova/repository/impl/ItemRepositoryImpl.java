package com.gmail.ezlotnikova.repository.impl;

import java.lang.invoke.MethodHandles;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.ezlotnikova.repository.ItemRepository;
import com.gmail.ezlotnikova.repository.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.gmail.ezlotnikova.repository.util.PaginationUtil.getQueryStartPosition;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    @SuppressWarnings("unchecked")
    public Page<Item> findPaginatedAndOrderedByName(Pageable pageRequest) {
        int startPosition = getQueryStartPosition(
                pageRequest.getPageNumber(), pageRequest.getPageSize());
        int maxResult = pageRequest.getPageSize();
        Long count = getTotalCount();
        String hql = "FROM Item as i WHERE i.isAvailable = 1 ORDER BY i.name";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return new PageImpl<Item>(query.getResultList(), pageRequest, count);
    }

    @Override
    public Item findByUuid(String uuid) {
        String hql = "FROM Item as i WHERE i.uniqueNumber =:uuid";
        Query query = entityManager.createQuery(hql);
        query.setParameter("uuid", uuid);
        try {
            return (Item) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}