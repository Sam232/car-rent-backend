package org.acme.commons.dto

import java.time.LocalDateTime

data class FilterRentsDto (
    var rented: Boolean,
    var pageNumber: String,
    var pageSize: String,
    var startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime
)