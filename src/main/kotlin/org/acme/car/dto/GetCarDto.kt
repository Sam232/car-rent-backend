package org.acme.car.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.acme.rent.dto.GetRentDto
import java.time.LocalDateTime

class GetCarDto {
    @NotBlank
    var id: Long? = null

    @NotBlank
    var name: String? = null

    @NotBlank
    var model: String? = null

    @NotNull
    var rent: GetRentDto? = null

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdOn: LocalDateTime? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedOn: LocalDateTime? = null
}