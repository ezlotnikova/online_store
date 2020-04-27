package com.gmail.ezlotnikova.service.util.converter;

import com.gmail.ezlotnikova.repository.model.Order;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.model.OrderPreviewDTO;
import com.gmail.ezlotnikova.service.model.ShowOrderDTO;

public class OrderConverter {

    public static OrderPreviewDTO convertToOrderPreviewDTO(Order order) {
        OrderPreviewDTO orderDTO = new OrderPreviewDTO();
        orderDTO.setId(
                order.getId());
        orderDTO.setItemName(
                order.getItem()
                        .getName());
        orderDTO.setAmount(
                order.getAmount());
        orderDTO.setSum(
                order.getSum());
        orderDTO.setStatus(
                order.getStatus());
        return orderDTO;
    }

    public static ShowOrderDTO convertToShowOrderDTO(Order order) {
        ShowOrderDTO orderDTO = new ShowOrderDTO();
        orderDTO.setId(
                order.getId());
        orderDTO.setStatus(
                order.getStatus());
        UserDetails userDetails = order.getUserDetails();
        orderDTO.setUserId(
                userDetails.getUserId());
        orderDTO.setUserTelephone(
                userDetails.getTelephone());
        orderDTO.setItemName(
                order.getItem()
                        .getName());
        orderDTO.setAmount(
                order.getAmount());
        orderDTO.setSum(
                order.getSum());
        return orderDTO;
    }

}
