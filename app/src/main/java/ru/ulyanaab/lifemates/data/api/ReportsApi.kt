package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.ulyanaab.lifemates.data.dto.request.ReportRequestDto

interface ReportsApi {

    @POST("reports")
    fun report(
        @Body request: ReportRequestDto
    ): Call<Unit>
}
