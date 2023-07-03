package com.example.gostore.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val  API_BASE_URL ="https://worried-hurricane-hedge.glitch.me/"

    private  var goStoreInterface:GoStoreInterface? =null

    fun goStoreServices ():GoStoreInterface{

        var retrofit =
            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        goStoreInterface= retrofit.create(GoStoreInterface::class.java)
        return goStoreInterface as GoStoreInterface

    }

}