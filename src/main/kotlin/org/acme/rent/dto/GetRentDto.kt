package org.acme.rent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.io.Serializable
import java.time.LocalDateTime

class GetRentDto : Serializable {
    @NotBlank
    var id: Long? = null

    @NotNull
    var rented: Boolean = false

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdOn: LocalDateTime? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedOn: LocalDateTime? = null
}