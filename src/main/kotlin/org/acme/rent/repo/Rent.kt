package org.acme.rent.repo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.acme.car.repo.Car
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(
    name = "tbl_rents",
    indexes = [
        Index(name = "rented_index", columnList = "rented"),
        Index(name = "created_on_updated_on_index", columnList = "created_on, updated_on")
    ]
)
@NamedQueries(
    NamedQuery(name = "Rent.filterByRentStatus", query = "from Rent where rented = ?1 and createdOn between ?2 and ?3")
)

class Rent : PanacheEntityBase, Serializable {
    @Id
    @GeneratedValue
    var id: Long? = null

    @OneToOne(mappedBy = "carRentData", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val carRentData: Car? = null

    @NotNull
    @Column(name = "rented", length = 6)
    var rented: Boolean = false

    @NotNull
    @Column(name = "created_on")
    var createdOn: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    @Column(name = "updated_on")
    var updatedOn: LocalDateTime? = null
}