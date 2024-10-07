package org.acme.car.boundary

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.acme.car.dto.AddCarDto
import org.acme.car.dto.GetCarDto
import org.acme.car.dto.UpdateCarDto
import org.acme.car.service.CarService
import org.acme.commons.dto.APIPaginationResponseDto
import org.acme.commons.dto.APIResponseDto
import org.acme.commons.dto.FilterCarsDto
import org.acme.commons.dto.FilterRentsDto
import org.acme.commons.utils.Utils
import org.slf4j.LoggerFactory
import java.time.LocalTime
import java.util.*

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class CarResourceImpl @Inject constructor(val carService: CarService): CarResource {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @POST
    @Path("cars")
    override fun create(addCarDto: AddCarDto): APIResponseDto<GetCarDto> {
        val sessionID = Utils.generateSessionID()

        val incomingRequest = Utils.convertObjectToString(addCarDto)

        logger.info("$sessionID - Incoming request received to create car :: $incomingRequest")

        val response = carService.create(addCarDto, sessionID)

        logger.info("$sessionID - Sending response :: ${Utils.convertObjectToString(response)}")

        return response
    }

    @GET
    @Path("cars/list")
    override fun filter(
        @DefaultValue("") @QueryParam("name") name: String,
        @DefaultValue("5") @QueryParam("pageSize") pageSize: String,
        @DefaultValue("1") @QueryParam("pageNumber") pageNumber: String,
        @DefaultValue("") @QueryParam("startDateTime") startDateTime: String,
        @DefaultValue("") @QueryParam("endDateTime") endDateTime: String,
    ): APIPaginationResponseDto<List<GetCarDto>> {
        val sessionID = Utils.generateSessionID()

        logger.info("$sessionID - Incoming request to get cars with name :: $name")

        val currDateTime = Utils.getCurrentDate()
        val formattedStartDateTime = Utils.parseDateTime(currDateTime, startDateTime, LocalTime.MIN)
        val formattedEndDateTime = Utils.parseDateTime(currDateTime, endDateTime, LocalTime.MAX)

        val filterCarsDto = FilterCarsDto(name, pageNumber, pageSize, formattedStartDateTime!!, formattedEndDateTime!!)

        val response = carService.filter(filterCarsDto, sessionID)

        logger.info("$sessionID - Sending response :: ${Utils.convertObjectToString(response)}")

        return response
    }

    @GET
    @Path("cars/list/filter-by-rented")
    override fun filterByRentStatus(
        @QueryParam("rented") rented: String?,
        @DefaultValue("5") @QueryParam("pageSize") pageSize: String,
        @DefaultValue("1") @QueryParam("pageNumber") pageNumber: String,
        @DefaultValue("") @QueryParam("startDateTime") startDateTime: String,
        @DefaultValue("") @QueryParam("endDateTime") endDateTime: String,
    ): APIPaginationResponseDto<List<GetCarDto>> {
        val sessionID = Utils.generateSessionID()

        logger.info("$sessionID - Incoming request to get car rents with rented status :: $rented")

        rented.isNullOrBlank() && throw WebApplicationException("rented is required in query parameters")

        val rentStatus: Boolean

        try {
            rentStatus = rented!!.lowercase(Locale.getDefault()).toBoolean()
        } catch (exp: TypeCastException) {
            throw TypeCastException("Invalid rented value passed. Pass either 'true' or 'false'")
        }

        val currDateTime = Utils.getCurrentDate()
        val formattedStartDateTime = Utils.parseDateTime(currDateTime, startDateTime, LocalTime.MIN)
        val formattedEndDateTime = Utils.parseDateTime(currDateTime, endDateTime, LocalTime.MAX)

        val filterRentsDto = FilterRentsDto(rentStatus, pageNumber, pageSize, formattedStartDateTime!!, formattedEndDateTime!!)

        val response = carService.filterByRentStatus(filterRentsDto, sessionID)

        logger.info("$sessionID - Sending response :: ${Utils.convertObjectToString(response)}")

        return response
    }

    @GET
    @Path("cars")
    override fun getByModel(
        @QueryParam("model") model: String?
    ): APIResponseDto<GetCarDto> {
        val sessionID = Utils.generateSessionID()

        logger.info("$sessionID - Incoming request to get car by model :: $model")

        model.isNullOrBlank() && throw WebApplicationException("model is required in query parameters")

        val response = carService.getByModel(model!!, sessionID)

        logger.info("$sessionID - Sending response :: ${Utils.convertObjectToString(response)}")

        return response
    }

    @PUT
    @Path("cars")
    override fun update(updateCarDto: UpdateCarDto): APIResponseDto<GetCarDto> {
        val sessionID = Utils.generateSessionID()

        val incomingRequest = Utils.convertObjectToString(updateCarDto)

        logger.info("$sessionID - Incoming request received to update car :: $incomingRequest")

        val response = carService.update(updateCarDto, sessionID)

        logger.info("$sessionID - Sending response :: ${Utils.convertObjectToString(response)}")

        return response
    }

    @DELETE
    @Path("cars/{id}")
    override fun delete(@PathParam("id") id: String?): APIResponseDto<GetCarDto> {
        val sessionID = Utils.generateSessionID()

        logger.info("$sessionID - Incoming request received to delete car with id :: $id")

        id.isNullOrBlank() && throw WebApplicationException("car id is required in path")

        val idUpdated: Long

        try {
            idUpdated = id!!.toLong()
        } catch (exp: NumberFormatException) {
            throw WebApplicationException("Invalid car id passed")
        }

        val response = carService.delete(idUpdated, sessionID)

        logger.info("$sessionID - Sending response :: ${Utils.convertObjectToString(response)}")

        return response
    }
}