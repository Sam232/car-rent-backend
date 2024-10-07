package org.acme.commons.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


object Utils {
    private val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun generateSessionID(): String {
        return UUID.randomUUID().toString()
    }

    fun <T> convertObjectToString(requestBody: T): String {
        val objectMap = ObjectMapper().registerModule(JavaTimeModule())
        return objectMap.writeValueAsString(requestBody)
    }

    fun getCurrentDate(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun parseDateTime(currDateTime: LocalDateTime, dateTimeStr: String, localTime:LocalTime): LocalDateTime? {
        val formattedDateTime: LocalDateTime? = if (dateTimeStr.isBlank()) {
            try {
                val dateTime = currDateTime.with(localTime).format(DATE_TIME_FORMAT).toString()
                logger.info("Parsed date time is $dateTime")
                LocalDateTime.parse(dateTime, DATE_TIME_FORMAT)
            } catch (exp: Exception) {
                throw Exception("Failed to pass default date time : ${exp.message}")
            }
        } else {
            try {
                LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMAT)
            } catch (exp: DateTimeException) {
                throw DateTimeException("Invalid date time passed : ${exp.message}")
            }
        }

        return formattedDateTime
    }
}
