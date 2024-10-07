package org.acme.car.repo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDateTime

@ApplicationScoped
class CarRepo : PanacheRepositoryBase<Car, Long> {
    fun filter(startDateTime: LocalDateTime, endDateTime: LocalDateTime): PanacheQuery<Car> {
        return find("#Car.filter", startDateTime, endDateTime)
    }

    fun filterByName(name: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime): PanacheQuery<Car> {
        return find("#Car.filterByName", name, startDateTime, endDateTime)
    }

    fun filterByRentStatus(rented: Boolean, startDateTime: LocalDateTime, endDateTime: LocalDateTime): PanacheQuery<Car> {
        return find("#Car.filterByRentStatus", rented, startDateTime, endDateTime)
    }

    fun findByModel(model: String): Car? {
        return find("#Car.getByModel", model).firstResult()
    }
}