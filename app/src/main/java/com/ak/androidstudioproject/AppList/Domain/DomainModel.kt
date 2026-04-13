package com.ak.androidstudioproject.AppList.Domain

data class AppList (
    val urls : List<String>
)

data class PreCardInfo (
    val url : String,
    val iconUrl : String,
    val appName : String,
    val shortDescription : String,
    val categories: List<String>
)