package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    val tagID: String,
    val tagName: String,
    val tagType: String
){
    private val languageNameMap = mapOf(
        "ar" to "Arabic", "bn" to "Bengali", "cn" to "Chinese (Simplified)",
        "cs" to "Czech", "da" to "Danish", "de" to "German", "el" to "Greek",
        "en" to "English", "es" to "Spanish", "et" to "Estonian", "fa" to "Persian",
        "fi" to "Finnish", "fr" to "French", "ga" to "Irish", "he" to "Hebrew",
        "hi" to "Hindi", "hu" to "Hungarian", "hy" to "Armenian", "id" to "Indonesian",
        "is" to "Icelandic", "it" to "Italian", "ja" to "Japanese", "ka" to "Georgian",
        "ko" to "Korean", "lt" to "Lithuanian", "mk" to "Macedonian", "nl" to "Dutch",
        "no" to "Norwegian", "pl" to "Polish", "pt" to "Portuguese", "ro" to "Romanian",
        "ru" to "Russian", "sr" to "Serbian", "sv" to "Swedish", "sw" to "Swahili",
        "ta" to "Tamil", "te" to "Telugu", "th" to "Thai", "tl" to "Tagalog",
        "tr" to "Turkish", "uk" to "Ukrainian", "ur" to "Urdu", "wo" to "Wolof",
        "xx" to "No Language", "zh" to "Chinese (Traditional)"
    )

    fun displayName(): String {
        return if (tagType == "language"){
            languageNameMap[tagID] ?: tagName
        }else{
            tagName
        }
    }
}