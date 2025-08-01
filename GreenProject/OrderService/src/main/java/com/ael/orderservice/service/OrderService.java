package com.ael.orderservice.service;


import com.ael.orderservice.config.rabbitmq.model.OrderDetailRequest;
import com.ael.orderservice.model.Order;
import com.ael.orderservice.model.OrderDetail;
import com.ael.orderservice.model.OrderStatus;
import com.ael.orderservice.repository.IOrderDetailRepository;
import com.ael.orderservice.repository.IOrderRepository;
import com.ael.orderservice.repository.IOrderStatusRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class OrderService  {

    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderStatusRepository orderStatusRepository;

    // Sipariş durumları için static sabitler
    public static final String STATUS_AKTIF = "Aktif";
    public static final String STATUS_BEKLEMEDE = "Beklemede";
    public static final String STATUS_IPTAL = "İptal";
    public static final String STATUS_KARGOLANDI = "Kargolandı";


    public void createOrder(OrderDetailRequest orderDetailRequest) {
        String statusName = STATUS_AKTIF; // Burada ihtiyaca göre farklı bir sabit de kullanılabilir
        OrderStatus orderStatus = orderStatusRepository
                .findByOrderStatusName(statusName)
                .orElseThrow(() -> new RuntimeException("Order Status not Found"));

        System.out.println("Aranan sipariş durumu: [" + statusName + "]");

        Order order = Order.builder()
                .customerId(orderDetailRequest.getCustomerId())
                .basketId(orderDetailRequest.getBasketId())
                .orderAddress(orderDetailRequest.getOrderAddress())
                .orderStatus(orderStatus)
                .build();

        orderRepository.save(order);

        orderDetailRequest.getBasketItems().forEach(item -> {
            OrderDetail orderDetail = OrderDetail.builder()
                    .productId(item.getProductId())
                    .quantity(item.getProductQuantity())
                    .order(order)
                    .unitPrice(item.getProductPrice())
                    .build();

            orderDetailRepository.save(orderDetail);
        });







    }
    public List<Order> getOrderByCustomerId(Integer customerId) {
        return orderRepository.getOrderByCustomerId(customerId);
    }
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findOrderDetailByOrder_OrderId(orderId);
    }

}
