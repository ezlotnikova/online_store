package com.gmail.ezlotnikova.service.util.converter;

import java.util.UUID;

import com.gmail.ezlotnikova.repository.model.Item;
import com.gmail.ezlotnikova.repository.model.ItemDetails;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;

public class ItemConverter {

    public static Item convertToDatabaseObject(ItemDTO itemDTO){
        Item item = new Item();
        item.setUniqueNumber(UUID.randomUUID().toString());
        item.setName(
                itemDTO.getName());
        item.setPrice(
                itemDTO.getPrice());
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setDescription(
                itemDTO.getDescription());
        itemDetails.setItem(item);
        item.setItemDetails(itemDetails);
        return item;
    }

    public static ItemPreviewDTO convertToItemPreviewDTO(Item item) {
        ItemPreviewDTO itemPreview = new ItemPreviewDTO();
        itemPreview.setId(
                item.getId());
        itemPreview.setUniqueNumber(
                item.getUniqueNumber());
        itemPreview.setName(
                item.getName());
        itemPreview.setPrice(
                item.getPrice());
        return itemPreview;
    }

    public static ItemDTO convertToItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(
                item.getId());
        itemDTO.setUniqueNumber(
                item.getUniqueNumber());
        itemDTO.setName(
                item.getName());
        itemDTO.setPrice(
                item.getPrice());
        if (item.getItemDetails() != null) {
            itemDTO.setDescription(
                    item.getItemDetails()
                            .getDescription());
        }
        return itemDTO;
    }

}