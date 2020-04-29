package com.gmail.ezlotnikova.web.controller;

import com.gmail.ezlotnikova.repository.model.сonstant.OrderStatusEnum;
import com.gmail.ezlotnikova.service.OrderService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.OrderPreviewDTO;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.ezlotnikova.service.constant.ResultTypeEnum.EXECUTION_FAILED;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.FAILURE_MESSAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ResultMessagesConstant.SUCCESS_MESSAGE;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showAllOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) {
        Page<OrderPreviewDTO> orders = orderService.findPaginatedAndOrderedByDate(page);
        model.addAttribute("orders", orders);
        return "order_previews";
    }

    @PostMapping
    public String createNewOrder(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam(value = "itemId") Long itemId,
            @RequestParam(value = "amount") Integer amount
    ) {
        Long userId = appUser.getId();
        ShowOrderDTO order = orderService.saveNewOrder(userId, itemId, amount);
        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    public String showOrderDetails(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        ShowOrderDTO order = orderService.findById(id);
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatusEnum.values());
        return "order_details";
    }

    @PostMapping("/{id}")
    public String updateOrderStatus(
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "newStatus") OrderStatusEnum newStatus,
            RedirectAttributes redirectAttributes
    ) {
        ExecutionResult result = orderService.updateOrderStatusById(id, newStatus);
        if (result.getResultType() == EXECUTION_FAILED) {
            redirectAttributes.addFlashAttribute(
                    FAILURE_MESSAGE, result.getErrorMessage());
        } else {
            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, "Status of order #" + id + " successfully changed to " + newStatus);
        }
        return "redirect:/orders";
    }

}