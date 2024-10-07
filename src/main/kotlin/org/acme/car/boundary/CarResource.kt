package org.acme.car.boundary

import org.acme.car.dto.AddCarDto
import org.acme.car.dto.GetCarDto
import org.acme.car.dto.UpdateCarDto
import org.acme.commons.dto.APIPaginationResponseDto
import org.acme.commons.dto.APIResponseDto

interface CarResource {
    fun create (addCarDto: AddCarDto): APIResponseDto<GetCarDto>

    fun filter(
        name: String,
        pageSize: String,
        pageNumber: String,
        startDateTime: String,
        endDateTime: String,
    ): APIPaginationResponseDto<List<GetCarDto>>

    fun filterByRentStatus(
        rented: String?,
        pageSize: String,
        pageNumber: String,
        startDateTime: String,
        endDateTime: String,
    ): APIPaginationResponseDto<List<GetCarDto>>

    fun getByModel(model: String?): APIResponseDto<GetCarDto>

    fun update(updateCarDto: UpdateCarDto): APIResponseDto<GetCarDto>

    fun delete(id: String?): APIResponseDto<GetCarDto>
}