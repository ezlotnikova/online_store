package com.gmail.ezlotnikova.web.controller.api;

import javax.validation.Valid;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/items")
public class ItemAPIController {

    private final ItemService itemService;

    public ItemAPIController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Page<ItemPreviewDTO> showAllItems(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        return itemService.findPaginatedAndOrderedByName(page);
    }

    @GetMapping("/{id}")
    public ItemDTO showItemById(
            @PathVariable(name = "id") Long id
    ) {
        return itemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO addItem(
            @Valid @RequestBody ItemDTO itemDTO
    ) {
        return itemService.add(itemDTO);
    }

    @DeleteMapping("/{id}")
    public ExecutionResult deleteItemById(
            @PathVariable(name = "id") Long id
    ) {
        return itemService.deleteItemById(id);
    }

}