package org.acme.car.repo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.acme.rent.repo.Rent
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(
    name = "tbl_cars",
    indexes = [
        Index(name = "name_model_index", columnList = "name, model", unique = true),
        Index(name = "created_on_updated_on_index", columnList = "created_on, updated_on")
    ]
)
@NamedQueries(
    NamedQuery(name = "Car.getByModel", query = "from Car where model = ?1"),
    NamedQuery(name = "Car.filter", query = "from Car where createdOn between ?1 and ?2 order by id asc"),
    NamedQuery(name = "Car.filterByName", query = "from Car where name = ?1 and createdOn between ?2 and ?3 order by id asc"),
    NamedQuery(name = "Car.filterByRentStatus", query = "from Car where carRentData.rented = ?1 and createdOn between ?2 and ?3 order by id asc"),
)

class Car : PanacheEntityBase, Serializable {
    @Id
    @GeneratedValue
    var id: Long? = null

    @NotEmpty
    @Column(length = 50)
    var name: String = ""

    @NotEmpty
    @Column(length = 50)
    var model: String = ""

    @NotNull
    @Column(name = "created_on")
    var createdOn: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    @Column(name = "updated_on")
    var updatedOn: LocalDateTime? = null

    @NotNull
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "rent_id")
    var carRentData: Rent? = null
}