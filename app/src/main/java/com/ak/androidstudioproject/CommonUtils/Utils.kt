package com.ak.androidstudioproject.CommonUtils

fun getCategoryText(category: List<String>): String  {
    val translated : List<String> = category.map { single ->
        when (single) {
            "tools" -> "Инструменты"
            "business" -> "Бизнес"
            "communication" -> "Связь"
            "education" -> "Образование"
            "entertainment" -> "Развлечения"
            "finance" -> "Финансы"
            "health_fitness" -> "Здоровье и фитнес"
            "lifestyle" -> "Стиль жизни"
            "medical" -> "Медицина"
            "music_audio" -> "Музыка и аудио"
            "navigation" -> "Навигация"
            "news_magazines" -> "Новости и журналы"
            "photography" -> "Фотография"
            "productivity" -> "Продуктивность"
            "shopping" -> "Покупки"
            "social" -> "Социальные сети"
            "sports" -> "Спорт"
            "travel" -> "Путешествия"
            "video_players" -> "Видео и плееры"
            "weather" -> "Погода"
            "books_reference" -> "Книги и справочники"
            "dating" -> "Знакомства"
            "events" -> "События"
            "food_drink" -> "Еда и напитки"
            "house_home" -> "Дом и интерьер"
            "kids" -> "Детям"
            "parenting" -> "Родителям"
            "personalization" -> "Персонализация"
            "auto_vehicles" -> "Авто и транспорт"
            "beauty" -> "Красота"
            "comics" -> "Комиксы"
            "art_design" -> "Искусство и дизайн"
            "library_demo" -> "Демо"
            "game" -> "Игры"
            else -> single
        }
    }
    return translated.joinToString(", ")
}