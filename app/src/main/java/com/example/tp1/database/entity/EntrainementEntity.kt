package com.example.tp1.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tp1.data.TypeActivite
@Entity(tableName = "entrainements")
data class EntrainementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titre: String,
    val activite: TypeActivite,
    val lieu: String,
    val exterieur: Boolean,
    val intensite: Int,
    val notes: String,
    val estComplete: Boolean =false,
    val estFavori: Boolean = false
)
