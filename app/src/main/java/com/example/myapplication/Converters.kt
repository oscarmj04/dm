package com.example.myapplication

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
        @TypeConverter
        fun fromLocalDate(value: LocalDate?): String? = value?.toString()

        @TypeConverter
        fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

        @TypeConverter
        fun fromCategory(value: Category?): String? = value?.name

        @TypeConverter
        fun toCategory(value: String?): Category? = value?.let { Category.valueOf(it) }
    }

