package com.radiusagent.radiusassignment.data.remote

import com.radiusagent.radiusassignment.data.model.ApiResponse
import io.reactivex.rxjava3.core.Single

class ApiManager {
    // call the api with apiservice class
    fun getData() : Single<ApiResponse> {
        val service = RetrofitClient.retrofit.create(ApiService::class.java)
        return service.getData()
    }

}