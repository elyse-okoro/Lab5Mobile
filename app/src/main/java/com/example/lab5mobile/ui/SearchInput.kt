package com.example.lab5mobile.ui

/**
 * Входные данные экрана: текст поля, выбранный аэропорт и признак того,
 * что последний запрос уже прочитан из DataStore.
 */
internal data class SearchInput(
    val text: String,
    val code: String?,
    val restored: Boolean
)
