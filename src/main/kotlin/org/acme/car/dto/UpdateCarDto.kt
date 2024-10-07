package org.acme.car.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.acme.rent.dto.UpdateRentDto

class UpdateCarDto {
    @NotNull
    var id: Long? = null

    @NotBlank
    var name: String? = null

    @NotBlank
    var model: String? = null

    @NotNull
    var rent: UpdateRentDto? = null
}