package org.acme.car.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.acme.rent.dto.AddRentDto

class AddCarDto {
    @NotBlank
    var name: String? = null

    @NotBlank
    var model: String? = null

    @NotNull
    var rentData: AddRentDto? = null
}