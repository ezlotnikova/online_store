package com.gmail.ezlotnikova.service.impl;

import com.gmail.ezlotnikova.repository.ItemRepository;
import com.gmail.ezlotnikova.repository.model.Item;
import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;
import com.gmail.ezlotnikova.service.util.converter.ItemConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.service.constant.PaginationConstant.ITEMS_BY_PAGE;
import static com.gmail.ezlotnikova.service.util.converter.ItemConverter.convertToDatabaseObject;
import static com.gmail.ezlotnikova.service.util.converter.ItemConverter.convertToItemDTO;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ItemDTO add(ItemDTO itemDTO) {
        Item item = convertToDatabaseObject(itemDTO);
        Item addedItem = itemRepository.persist(item);
        return convertToItemDTO(addedItem);
    }

    @Override
    @Transactional
    @Nullable
    public ItemDTO findById(Long id) {
        Item item = itemRepository.findById(id);
        if (item != null) {
            return convertToItemDTO(item);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    @Nullable
    public ItemDTO findByUuid(String uuid) {
        Item item = itemRepository.findByUuid(uuid);
        if (item != null) {
            return convertToItemDTO(item);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Page<ItemPreviewDTO> findPaginatedAndOrderedByName(int pageNumber) {
        /* page numeration in UI starts from 1, but in Pageable and Page objects it starts from zero,
        so parameter passed to PageRequest constructor is "pageNumber - 1" */
        Pageable pageRequest = PageRequest.of(pageNumber - 1, ITEMS_BY_PAGE);
        return itemRepository.findPaginatedAndOrderedByName(pageRequest)
                .map(ItemConverter::convertToItemPreviewDTO);
    }

    @Override
    @Transactional
    public ItemDTO duplicateItem(Long id) {
        Item item = itemRepository.findById(id);
        ItemDTO itemDTO = convertToItemDTO(item);
        ItemDTO duplicateDTO = getDuplicate(itemDTO);
        return add(duplicateDTO);
    }

    @Override
    @Transactional
    public ExecutionResult deleteItemById(Long id) {
        Item item = itemRepository.findById(id);
        if (item != null) {
            itemRepository.remove(item);
            return ExecutionResult.ok();
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No item found with id provided");
        }
    }

    private ItemDTO getDuplicate(ItemDTO item) {
        ItemDTO duplicate = new ItemDTO();
        duplicate.setName(
                item.getName() + "-1");   // implement counter
        duplicate.setPrice(
                item.getPrice());
        duplicate.setDescription(
                item.getDescription());
        return duplicate;
    }

}