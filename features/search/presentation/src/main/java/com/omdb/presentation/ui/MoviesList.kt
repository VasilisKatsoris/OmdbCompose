package com.omdb.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.omdb.core.framework.AppConstants
import com.omdb.domain.Movie

@Composable
internal fun MoviesList(
    modifier: Modifier,
    pagingData: LazyPagingItems<Movie>,
    sharedElementModifierProvider: SharedElementModifierProvider,
    openMovieDetails: (Movie) -> Unit
){
    LazyColumn(
        modifier = modifier
    ) {
        items(pagingData.itemCount) { index ->
            val movie = pagingData[index] ?: return@items
            val startDelay = index * AppConstants.SEARCH_RESULT_ENTRY_STEP_DELAY

            MovieItem(
                sharedElementModifierProvider = sharedElementModifierProvider,
                modifier = Modifier.conditional(!LocalInspectionMode.current) {
                    animateItem(
                        fadeInSpec = tween(
                            AppConstants.SEARCH_RESULT_ENTRY_DURATION,
                            delayMillis = startDelay
                        ),
                        fadeOutSpec = tween(
                            AppConstants.SEARCH_RESULT_ENTRY_DURATION,
                            delayMillis = startDelay
                        ),
                        placementSpec = tween(
                            AppConstants.SEARCH_RESULT_ENTRY_DURATION,
                            delayMillis = startDelay
                        ),
                    )
                },
                onClick = openMovieDetails,
                movie = movie
            )

            if (index != pagingData.itemCount - 1) {
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}