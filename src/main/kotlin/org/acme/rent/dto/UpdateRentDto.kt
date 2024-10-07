package org.acme.rent.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

class UpdateRentDto : Serializable {
    @NotNull
    var rented: Boolean = false
}