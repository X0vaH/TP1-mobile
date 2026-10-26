package com.example.tp1.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ExerciceApiService {

    @Headers("X-RapidAPI-Host: movebase.p.rapidapi.com")
    @GET("api/v1/exercises")
    suspend fun chercherExercices(
        @Query("search") motCle: String,
        @Header("X-RapidAPI-Key") apiKey: String,
        @Query("lang") langue: String = "fr",
        @Query("limit") limite: Int = 20,
        @Query("offset") decalage: Int = 0
    ): RechercheExercicesDTO
}
