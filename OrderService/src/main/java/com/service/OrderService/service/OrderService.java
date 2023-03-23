package com.service.OrderService.service;

import com.service.OrderService.config.WebClientConfig;
import com.service.OrderService.dto.InventoryResponse;
import com.service.OrderService.dto.OrderEvent;
import com.service.OrderService.dto.OrderRequest;
import com.service.OrderService.model.Order;
import com.service.OrderService.model.OrderLineItems;
import com.service.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    private WebClient.Builder webClientConfig;
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderItems = orderRequest.getOrderLineItems().stream().map(orderLineItemsDto -> OrderLineItems.builder()
                .id(orderLineItemsDto.getId())
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build()).toList();

        order.setOrderLineItems(orderItems);
        log.info("after prepare order obj");

        List<String> skuCodes = order.getOrderLineItems().stream().map(orderLineItems -> orderLineItems.getSkuCode()).toList();
        // check if it exists in inventory
        InventoryResponse[] inventoryResponses = webClientConfig.build().get().uri("http://inventory-service/api/inventory",
                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        if (inventoryResponses.length > 0) {
            boolean allItemsInStock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse -> inventoryResponse.getInStock());
            if (allItemsInStock) {
                log.info("all items in stock");
                orderRepository.save(order);

                // send notification if order placed
                kafkaTemplate.send("notificationTopic", new OrderEvent(order.getOrderNumber()));

            } else  {
                throw new IllegalArgumentException("Not All products in stock");
            }
        }  else  {
            throw new IllegalArgumentException("Not All products in stock");
        }



    }
}
