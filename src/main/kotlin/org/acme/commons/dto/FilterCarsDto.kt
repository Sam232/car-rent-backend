package org.acme.commons.dto

import java.time.LocalDateTime

data class FilterCarsDto (
    var name: String,
    var pageNumber: String,
    var pageSize: String,
    var startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime
)