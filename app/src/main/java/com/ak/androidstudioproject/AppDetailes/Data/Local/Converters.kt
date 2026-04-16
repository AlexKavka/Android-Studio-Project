package com.ak.androidstudioproject.AppDetailes.Data.Local

import androidx.room.TypeConverter
import com.ak.androidstudioproject.AppDetailes.Data.Remote.AgeRestriction
import com.ak.androidstudioproject.AppDetailes.Data.Remote.FileUrl
import com.ak.androidstudioproject.AppDetailes.Data.Remote.Rating
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromFileUrlList(value: String): List<FileUrl> {
        val type = object : TypeToken<List<FileUrl>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toFileUrlList(list: List<FileUrl>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromAgeRestriction(value: String): AgeRestriction {
        return gson.fromJson(value, AgeRestriction::class.java)
    }

    @TypeConverter
    fun toAgeRestriction(obj: AgeRestriction): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    fun fromRating(value: String): Rating {
        return gson.fromJson(value, Rating::class.java)
    }

    @TypeConverter
    fun toRating(obj: Rating): String {
        return gson.toJson(obj)
    }
}