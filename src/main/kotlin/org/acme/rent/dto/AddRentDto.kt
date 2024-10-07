package org.acme.rent.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

class AddRentDto : Serializable {
    @NotNull
    var rented: Boolean = false
}