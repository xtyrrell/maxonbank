package com.xtyrrell.maxonbank.web

import com.xtyrrell.maxonbank.query.LineItem
import com.xtyrrell.maxonbank.query.OrderRepository
import com.xtyrrell.maxonbank.query.OrderEntity

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * This is just here to reference as an example of using Kotlin Spring associations.
 */

@RequestMapping("/orders")
@RestController
class OrdersController(
    private val orderRepository: OrderRepository
) {
    @GetMapping("test")
    fun test(): List<OrderEntity> {
        orderRepository.deleteAll()

        val order = OrderEntity("Timothy", "Tester")

        val item = LineItem(order)
        order.addLineItem(item)

        orderRepository.save(order)

        return orderRepository.findAll()
    }

    @GetMapping("/add") fun add(): OrderEntity {
        val order = orderRepository.findAll().first()
        order.addLineItem(LineItem(order))

        orderRepository.save(order)

        return order
    }

    @GetMapping fun index() = orderRepository.findAll()
}