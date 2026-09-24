package com.example.tp1.navigation

import kotlinx.serialization.Serializable

@Serializable
object AccueilRoute

@Serializable
object ListeRoute

@Serializable
data class DetailRoute(val id: Int)

@Serializable
object CreerRoute