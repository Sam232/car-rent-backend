package org.acme.car.service

import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import jakarta.ws.rs.WebApplicationException
import org.acme.car.dto.AddCarDto
import org.acme.car.dto.GetCarDto
import org.acme.car.dto.UpdateCarDto
import org.acme.car.repo.Car
import org.acme.car.repo.CarRepo
import org.acme.commons.dto.APIPaginationResponseDto
import org.acme.commons.dto.APIResponseDto
import org.acme.commons.dto.FilterCarsDto
import org.acme.commons.dto.FilterRentsDto
import org.hibernate.QueryException
import org.hibernate.QueryTimeoutException
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors


@ApplicationScoped
class CarServiceImpl: CarService {
    @field:Default
    @field:Inject
    private lateinit var carRepository: CarRepo

    private val modelMapper = ModelMapper()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun create(car: AddCarDto, sessionID: String): APIResponseDto<GetCarDto> {
        val newCar = modelMapper.map(car, Car::class.java)
                ?: throw Exception("$sessionID - Failed to map dto to Car entity")

        try {
            carRepository.persist(newCar)
        } catch (exp: PersistenceException) {
            throw PersistenceException("$sessionID - Failed to create new car")
        }

        val savedCar = modelMapper.map(newCar, GetCarDto::class.java)
                ?: throw Exception("$sessionID - Failed to map entity to GetCarDto")

        return APIResponseDto(code = "00", msg = "success", data = savedCar, processId = sessionID)
    }

    override fun filter(filterCarsDto: FilterCarsDto, sessionID: String): APIPaginationResponseDto<List<GetCarDto>> {
        val result: PanacheQuery<Car>?

        try {
            result = if (filterCarsDto.name.isBlank()) {
                carRepository.filter(filterCarsDto.startDateTime, filterCarsDto.endDateTime)
            } else {
                carRepository.filterByName(
                    filterCarsDto.name, filterCarsDto.startDateTime, filterCarsDto.endDateTime
                )
            }
        } catch (exp: QueryException) {
            logger.error("$sessionID - Query failed. Reason :: ${exp.message}")
            throw exp
        } catch (exp: QueryTimeoutException) {
            logger.error("$sessionID - Query took too long. Reason :: ${exp.message}")
            throw exp
        }

        val response = analyzeExpectedPaginationResult(
            result, filterCarsDto.pageNumber, filterCarsDto.pageSize, sessionID
        )

        return response
    }

    override fun filterByRentStatus(
        filterRentsDto: FilterRentsDto,
        sessionID: String
    ): APIPaginationResponseDto<List<GetCarDto>> {
        val result: PanacheQuery<Car>?

        try {
            result = carRepository.filterByRentStatus(
                filterRentsDto.rented, filterRentsDto.startDateTime, filterRentsDto.endDateTime
            )
        } catch (exp: QueryException) {
            logger.error("$sessionID - Query failed. Reason :: ${exp.message}")
            throw exp
        } catch (exp: QueryTimeoutException) {
            logger.error("$sessionID - Query took too long. Reason :: ${exp.message}")
            throw exp
        }

        val response = analyzeExpectedPaginationResult(result, filterRentsDto.pageNumber, filterRentsDto.pageSize, sessionID)

        return response
    }

    override fun getByModel(model: String, sessionID: String): APIResponseDto<GetCarDto> {
        val car: Car?

        try {
            car = carRepository.findByModel(model)
        } catch (exp: QueryException) {
            logger.error("$sessionID - Query failed. Reason :: ${exp.message}")
            throw exp
        } catch (exp: QueryTimeoutException) {
            logger.error("$sessionID - Query took too long. Reason :: ${exp.message}")
            throw exp
        }

        val singleCar = modelMapper.map(car, GetCarDto::class.java) ?: throw Exception("$sessionID - Failed to map entity to GetCarDto")

        val response = APIResponseDto(code = "00", msg = "success", data = singleCar, processId = sessionID)

        return response
    }

    private fun analyzeExpectedPaginationResult(
        result: PanacheQuery<Car>,
        pageNumber: String,
        pageSize: String,
        sessionID: String
    ): APIPaginationResponseDto<List<GetCarDto>> {
        val cars: List<Car> = result.page(pageNumber.toInt()-1, pageSize.toInt())
            .stream().collect(Collectors.toList())
        val totalPages = result.pageCount()

        val listOfCars: MutableList<GetCarDto> = mutableListOf()

        if (cars.isNotEmpty()) {
            cars.stream().forEach { car ->
                val singleCar: GetCarDto = modelMapper.map(car, GetCarDto::class.java)
                    ?: throw Exception("$sessionID - Failed to map entity to GetCarDto")
                listOfCars.add(singleCar)
            }
        }

        val response: APIPaginationResponseDto<List<GetCarDto>> = if (listOfCars.isNotEmpty()) {
            APIPaginationResponseDto(
                msg = "success", code = "00", totalPages = totalPages, pageSize = pageSize.toInt(),
                data = listOfCars, processId = sessionID
            )
        } else {
            APIPaginationResponseDto(msg = "No cars found", code = "01", processId = sessionID)
        }
        return response
    }

    @Transactional
    override fun update(updatedCar: UpdateCarDto, sessionID: String): APIResponseDto<GetCarDto> {
        val car: Car?

        try {
            car = carRepository.findById(updatedCar.id!!)
        } catch (exp: QueryException) {
            logger.error("$sessionID - Query failed. Reason :: ${exp.message}")
            throw exp
        } catch (exp: QueryTimeoutException) {
            logger.error("$sessionID - Query took too long. Reason :: ${exp.message}")
            throw exp
        }

        car == null && throw WebApplicationException("$sessionID - Car to update was not found.")

        car!!.name = updatedCar.name!!
        car.model = updatedCar.model!!
        car.carRentData!!.rented = updatedCar.rent!!.rented

        try {
            carRepository.persistAndFlush(car)
        } catch (exp: PersistenceException) {
            throw PersistenceException("$sessionID - Failed to update car. Reason :: ${exp.message}")
        }

        val carUpdated = modelMapper.map(car, GetCarDto::class.java) ?:
            throw Exception("$sessionID - Failed to map entity to GetCarDto")

        val response = APIResponseDto(code = "00", msg = "success", data = carUpdated, processId = sessionID)

        return response
    }

    @Transactional
    override fun delete(id: Long, sessionID: String): APIResponseDto<GetCarDto> {
        val car: Car?

        try {
            car = carRepository.findById(id)
        } catch (exp: QueryException) {
            logger.error("$sessionID - Query failed. Reason :: ${exp.message}")
            throw exp
        } catch (exp: QueryTimeoutException) {
            logger.error("$sessionID - Query took too long. Reason :: ${exp.message}")
            throw exp
        }

        car == null && throw WebApplicationException("$sessionID - Car to delete was not found.")

        try {
            carRepository.deleteById(id)
        } catch (exp: PersistenceException) {
            throw PersistenceException("$sessionID - Failed to delete car. Reason :: ${exp.message}")
        }

        val deletedCar = modelMapper.map(car, GetCarDto::class.java) ?:
            throw Exception("$sessionID - Failed to map entity to GetCarDto")

        val response = APIResponseDto(code = "00", msg = "success", data = deletedCar, processId = sessionID)

        return response
    }
}