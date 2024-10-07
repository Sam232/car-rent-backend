package org.acme.commons

import jakarta.persistence.PersistenceException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.acme.commons.dto.APIResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.DateTimeException

@Provider
class GlobalExceptionHandler: ExceptionMapper<Exception> {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun toResponse(p0: Exception): Response {
        return mapExceptionToResponse(p0)
    }

    private fun mapExceptionToResponse(exception: Exception): Response {
        var status = 400
        var message = "Request failed"

        when (exception) {
            is WebApplicationException -> {
                message = exception.message?.split(" : ")?.get(0) ?: message
                logger.error("Web application exception: {}", "", exception)
            }
            is PersistenceException -> {
                status = 500
                logger.error("Database persistence exception: {}", "", exception);
            }
            is DateTimeException -> {
                message = exception.message?.split(" : ")?.get(0) ?: message
                logger.error("Date time exception: {}", "", exception);
            }
            else -> {
                status = 500
                logger.error("Unexpected server error: {}", "", exception);
            }
        }

        val apiResponse = APIResponseDto(msg = message, code = "02", data = null)
        val response = Response.status(status).entity(apiResponse).build();

        return response
    }
}