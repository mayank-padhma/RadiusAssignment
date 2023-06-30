package com.radiusagent.radiusassignment.data.remote

import com.radiusagent.radiusassignment.data.model.ApiResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ApiService {
    // get api endpoint to get data
    @GET("ad-assignment/db")
    fun getData() : Single<ApiResponse>
}