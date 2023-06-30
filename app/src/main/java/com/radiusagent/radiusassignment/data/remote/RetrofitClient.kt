package com.radiusagent.radiusassignment.data.remote

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // returns retrofit instance
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/iranjith4/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}