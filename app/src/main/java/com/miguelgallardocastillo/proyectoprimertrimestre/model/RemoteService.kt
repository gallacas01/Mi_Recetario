package com.miguelgallardocastillo.proyectoprimertrimestre.model

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService{

    @GET("recipes/v2?type=public&random=true&q=random")
    suspend fun RandomRecetas(@Query("app_id") app_id: String,@Query("app_key") app_key: String) : RemoteResult

    @GET("recipes/v2?type=public&random=true")
    suspend fun CustomRecetas(@Query("q") q:String,@Query("app_id") app_id: String, @Query("app_key") app_key: String) : RemoteResult

}
