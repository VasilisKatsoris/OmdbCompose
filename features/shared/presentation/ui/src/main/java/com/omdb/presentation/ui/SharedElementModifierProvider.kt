package com.omdb.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class SharedElementModifierProvider(
    val sharedImageModifier: @Composable (movieId: String) -> Modifier = { Modifier },
    val sharedTitleModifier: @Composable (movieId: String) -> Modifier = { Modifier },
    val sharedYearModifier: @Composable (movieId: String) -> Modifier = { Modifier },
)
 