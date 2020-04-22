package com.gmail.ezlotnikova.web.controller;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.ezlotnikova.service.constant.ResultTypeEnum.EXECUTED_SUCCESSFULLY;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping()
    public String showItemsByPage(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int pageNumber
    ) {
        Page<ItemPreviewDTO> items = itemService.findPaginatedAndOrderedByName(pageNumber);
        model.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/{uuid}")
    public String showArticleWithComments(
            @PathVariable(name = "uuid") String uuid,
            Model model
    ) {
        ItemDTO item = itemService.findByUuid(uuid);
        model.addAttribute("item", item);
        return "item_description";
    }

    @GetMapping("/{id}/duplicate")
    public String duplicateItem(
            @PathVariable(name = "id") Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        ItemDTO addedItem = itemService.duplicateItem(id);
        model.addAttribute("item", addedItem);
        redirectAttributes.addFlashAttribute(
                SUCCESS_MESSAGE, "Item added successfully");
        return "redirect:/items/" + addedItem.getUniqueNumber();
    }

    @GetMapping("/{id}/delete")
    public String deleteItemById(
            @PathVariable(name = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = itemService.deleteItemById(id);
        if (result.getResultType() == EXECUTED_SUCCESSFULLY) {
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "Item deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        }
        return "redirect:/items";
    }

}