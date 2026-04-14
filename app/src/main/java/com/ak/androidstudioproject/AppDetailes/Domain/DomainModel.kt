package com.ak.androidstudioproject.AppDetailes.Domain

data class FullCardInfo (
    val url : String,
    val iconUrl : String,
    val appName : String,
    val shortDescription : String,
    val categories : List<String>,
    val screenshots : List<String>,
    val ageRating : String,
    val developer : String,
    val appSize: Long
)