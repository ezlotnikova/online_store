package com.gmail.ezlotnikova.repository;

import com.gmail.ezlotnikova.repository.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepository extends GenericRepository<Long, Item> {

    Page<Item> findPaginatedAndOrderedByName(Pageable pageRequest);

    Item findByUuid(String uuid);

}