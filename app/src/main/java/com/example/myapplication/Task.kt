package com.example.myapplication

// @Entity(tableName = "tasks")   // Room deshabilitado temporalmente

//import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

data class Task(

    // ID remoto de CrudCrud
    @Json(name = "_id")
    var id: String? = null,

    var title: String,
    var description: String,

    // String para compatibilidad total con Retrofit/Moshi
    var dueDate: String,

    var category: Category,
    var done: Boolean
) : Serializable
