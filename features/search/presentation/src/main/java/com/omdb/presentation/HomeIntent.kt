package com.omdb.presentation

sealed class HomeIntent {
    data class SearchTextTyped(val text: String) : HomeIntent()
}