package org.acme.commons.dto

data class APIResponseDto <T> (
    var msg: String? = null,
    var code: String? = null,
    var data: T? = null,
    var processId: String? = null
)

data class APIPaginationResponseDto <T> (
    var msg: String? = null,
    var code: String? = null,
    var totalPages: Int? = null,
    var pageSize: Int? = null,
    var data: T? = null,
    var processId: String? = null
)