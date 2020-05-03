package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;
import org.springframework.data.domain.Page;

public interface ItemService {

    ItemDTO add(ItemDTO itemDTO);

    ItemDTO findById(Long id);

    ItemDTO findByUuid(String uuid);

    Page<ItemPreviewDTO> findPaginatedAndOrderedByName(int pageNumber);

    ItemDTO duplicateItem (Long id);

    ExecutionResult deleteItemById(Long id);

}