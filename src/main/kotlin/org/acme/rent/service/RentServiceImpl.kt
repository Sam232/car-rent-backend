package org.acme.rent.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.acme.rent.repo.RentRepo
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationScoped
class RentServiceImpl: RentService {
    @field:Default
    @field:Inject
    private lateinit var rentRepo: RentRepo

    private val modelMapper = ModelMapper()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
}