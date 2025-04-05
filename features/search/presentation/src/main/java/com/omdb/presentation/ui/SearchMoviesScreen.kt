package com.omdb.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omdb.domain.Movie
import com.omdb.presentation.HomeIntent
import com.omdb.presentation.SearchMoviesState
import com.omdb.presentation.SearchMoviesViewModel
import com.omdb.shared.presentation.resources.R
import kotlinx.coroutines.flow.flow


@Composable
fun SearchMoviesScreen(
    openMovieDetails: (Movie) -> Unit,
    sharedElementModifierProvider: SharedElementModifierProvider
) {
    val viewModel = hiltViewModel<SearchMoviesViewModel>()

    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value

    SearchScreenContent(
        viewState = viewState,
        pagingData = pagingData,
        onSearchTextChanged = { viewModel.handleIntent(HomeIntent.SearchTextTyped(it)) },
        openMovieDetails = openMovieDetails,
        sharedElementModifierProvider = sharedElementModifierProvider
    )

}

@Composable
private fun SearchScreenContent(
    viewState: SearchMoviesState,
    onSearchTextChanged: (String) -> Unit,
    openMovieDetails: (Movie) -> Unit,
    pagingData: LazyPagingItems<Movie>,
    sharedElementModifierProvider: SharedElementModifierProvider,
) {

    val isLoading = pagingData.loadState.refresh is LoadState.Loading
    val showWelcomeMessage = viewState.searchText.isEmpty() && !viewState.inTypeDelayWindow
    val showNoResults = !isLoading &&
            viewState.searchText.isNotEmpty() &&
            !viewState.inTypeDelayWindow &&
            pagingData.itemCount == 0

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            SearchBar(
                isLoading = isLoading,
                modifier = Modifier.padding(horizontal = 16.dp),
                searchText = viewState.searchText,
                onValueChanged = onSearchTextChanged
            )
        }
    ) { innerPadding ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            val (lazyColumn, loadingIndicator, message) = createRefs()

            AnimatedVisibility(
                visible = showWelcomeMessage || showNoResults,
                enter = fadeIn(animationSpec = tween(delayMillis = 300)),
                exit = fadeOut(animationSpec = tween()),
                modifier = Modifier.constrainAs(message) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    text = stringResource(
                        if(showWelcomeMessage) R.string.welcome_to_movie_app
                        else R.string.No_results_found
                    )
                )
            }

            MoviesList(
                modifier = Modifier.constrainAs(lazyColumn) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(loadingIndicator.top)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .clipToBounds(),
                openMovieDetails = openMovieDetails,
                sharedElementModifierProvider = sharedElementModifierProvider,
                pagingData = pagingData,
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(loadingIndicator) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            ) {
                if (pagingData.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(20.dp).size(40.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val movies = listOf(
        Movie(
            imdbID = "1",
            title = "Avengers: Endgame",
            year = "2019",
            poster = "https://example.com/poster.jpg"
        ),
        Movie(
            imdbID = "2",
            title = "Avengers: Infinity War",
            year = "2018",
            poster = "https://example.com/poster.jpg"
        )
    )
    SearchScreenContent(
        viewState = SearchMoviesState("Avengers"),
        onSearchTextChanged = {},
        openMovieDetails = {},
        pagingData = flow { emit(PagingData.from(movies)) }.collectAsLazyPagingItems(),
        sharedElementModifierProvider = SharedElementModifierProvider()
    )
}
