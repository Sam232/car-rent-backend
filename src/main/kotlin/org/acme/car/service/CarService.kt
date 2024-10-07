package org.acme.car.service

import org.acme.car.dto.AddCarDto
import org.acme.car.dto.GetCarDto
import org.acme.car.dto.UpdateCarDto
import org.acme.commons.dto.APIPaginationResponseDto
import org.acme.commons.dto.APIResponseDto
import org.acme.commons.dto.FilterCarsDto
import org.acme.commons.dto.FilterRentsDto

interface CarService {
    fun create(car: AddCarDto, sessionID: String): APIResponseDto<GetCarDto>

    fun filter(filterCarsDto: FilterCarsDto, sessionID: String): APIPaginationResponseDto<List<GetCarDto>>

    fun filterByRentStatus(filterRentsDto: FilterRentsDto, sessionID: String): APIPaginationResponseDto<List<GetCarDto>>

    fun getByModel(model: String, sessionID: String): APIResponseDto<GetCarDto>

    fun update(updatedCar: UpdateCarDto, sessionID: String): APIResponseDto<GetCarDto>

    fun delete(id: Long, sessionID: String): APIResponseDto<GetCarDto>
}