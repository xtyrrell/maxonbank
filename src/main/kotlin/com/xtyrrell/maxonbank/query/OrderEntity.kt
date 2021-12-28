/**
 * This is just here to reference as an example of using Kotlin Spring associations.
 */

package com.xtyrrell.maxonbank.query

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
class OrderEntity(
    var firstName: String,
    var lastName: String,
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
) {
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "order")
    private val _lineItems = mutableListOf<LineItem>()

    val lineItems @JsonManagedReference get() = _lineItems.toList()

    fun addLineItem(newItem: LineItem) {
        _lineItems += newItem
    }
}

@Entity
class LineItem(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    val order: OrderEntity? = null
){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
}

interface OrderRepository : JpaRepository<OrderEntity, Long>
