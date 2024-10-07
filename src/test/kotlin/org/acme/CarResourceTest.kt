package org.acme

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.acme.car.dto.AddCarDto
import org.acme.car.dto.UpdateCarDto
import org.acme.commons.utils.Utils
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

@QuarkusTest
@Disabled
class CarResourceTest {
    private val baseUrl = "/api/v1"
    private val podamFactoryImpl: PodamFactoryImpl = PodamFactoryImpl()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun testCreateCarResource() {
        val requestBody = podamFactoryImpl.manufacturePojoWithFullData(AddCarDto::class.java)
        val jsonRequest = Utils.convertObjectToString<AddCarDto>(requestBody)
        logger.info("Sending create car request with body :: $jsonRequest")

        given()
            .body(requestBody).contentType(ContentType.JSON)
            .`when`().post("$baseUrl/cars")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }

    @Test
    fun testAllCarsResource() {
        logger.info("Sending request to get all cars")

        val queryParams: HashMap<String, String> = HashMap()
        queryParams["startDateTime"] = "2024-02-08 00:00:00"
        queryParams["endDateTime"] = "2024-02-08 23:59:59"
        queryParams["pageSize"] = "5"
        queryParams["pageNumber"] = "2"

        given()
            .`when`().params(queryParams)
            .get("$baseUrl/cars/list")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }

    @Test
    fun testGetCarsByNameResource() {
        val name = "WYGVKXF9TF"
        logger.info("Sending request to get car with name :: $name")

        val queryParams: HashMap<String, String> = HashMap()
        queryParams["name"] = name
        queryParams["startDateTime"] = "2024-02-02 00:00:00"
        queryParams["endDateTime"] = "2024-02-08 23:59:59"
        queryParams["pageSize"] = "5"
        queryParams["pageNumber"] = "2"

        given()
            .`when`().params(queryParams)
            .get("$baseUrl/cars/list")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }

    @Test
    fun testGetCarsNotRentedResource() {
        val rented = "false"
        logger.info("Sending request to get cars by rented status :: $rented")

        val queryParams: HashMap<String, String> = HashMap()
        queryParams["rented"] = rented
        queryParams["startDateTime"] = "2024-02-02 00:00:00"
        queryParams["endDateTime"] = "2024-02-08 23:59:59"
        queryParams["pageSize"] = "5"
        queryParams["pageNumber"] = "1"

        given()
            .`when`().params(queryParams)
            .get("$baseUrl/cars/list/filter-by-rented")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }

    @Test
    fun testGetCarsRentedResource() {
        val rented = "true"
        logger.info("Sending request to get cars by rented status :: $rented")

        val queryParams: HashMap<String, String> = HashMap()
        queryParams["rented"] = rented
        queryParams["startDateTime"] = "2024-02-02 00:00:00"
        queryParams["endDateTime"] = "2024-02-08 23:59:59"
        queryParams["pageSize"] = "5"
        queryParams["pageNumber"] = "1"

        given()
            .`when`().params(queryParams)
            .get("$baseUrl/cars/list/filter-by-rented")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }

    @Test
    fun testGetCarByModelResource() {
        val model = "_ydP9xUcH4"
        logger.info("Sending request to get car with model :: $model")

        given()
            .`when`().param("model", model)
            .get("$baseUrl/cars")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }

    @Test
    fun testUpdateCarResource() {
        val requestBody = podamFactoryImpl.manufacturePojoWithFullData(UpdateCarDto::class.java)
        requestBody.id = 1001
        val jsonRequest = Utils.convertObjectToString<UpdateCarDto>(requestBody)
        logger.info("Sending update car request with body :: $jsonRequest")

        given()
            .body(requestBody).contentType(ContentType.JSON)
            .`when`().put("$baseUrl/cars")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }

    @Test
    fun testDeleteCarResource() {
        val id = 3651
        logger.info("Sending request to delete car with id :: $id")

        given().contentType(ContentType.JSON)
            .`when`().delete("$baseUrl/cars/$id")
            .then()
            .log().all()
            .time(Matchers.lessThan(5000L))
            .statusCode(200)
            .assertThat()
            .body("code", CoreMatchers.equalTo("00"))
    }
}