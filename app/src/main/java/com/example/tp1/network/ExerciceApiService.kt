package com.example.tp1.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Headers
import retrofit2.http.Query

interface ExerciceApiService {

    @GET("api/v1/exercises")
    suspend fun chercherExercices(
        @Query("search") motCle: String,
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("Accept") accept: String = "application/json",
        @Header("X-RapidAPI-Host") host: String = "movebase.p.rapidapi.com",
        @Query("lang") langue: String = "fr",
        @Query("limit") limite: Int = 20,
        @Query("offset") decalage: Int = 0
    ): RechercheExercicesDTO

    @GET("api/v1/exercises/{id}")
    suspend fun obtenirExercice(
        @Path("id") id: String,
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("Accept") accept: String = "application/json",
        @Header("X-RapidAPI-Host") host: String = "movebase.p.rapidapi.com",
        @Query("lang") langue: String = "fr"
    ): ExerciceDetailDTO
}
