package com.omdb.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omdb.core.framework.AppConstants.Companion.DETAILS_ITEM_STEP_OFFSET
import com.omdb.domain.Movie
import com.omdb.domain.MovieDetails
import com.omdb.presentation.MovieDetailsIntent
import com.omdb.presentation.MovieDetailsState
import com.omdb.presentation.MovieDetailsViewModel
import com.omdb.shared.presentation.resources.R


@Composable
fun MovieDetailsScreen(
    movie: Movie,
    sharedElementModifierProvider: SharedElementModifierProvider
) {

    val viewModel = hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.Factory> {
        it.create(movie)
    }

    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value

    MovieDetailsScaffold(
        movie = movie,
        viewState = viewState,
        onRetryClicked = { viewModel.handleIntent(MovieDetailsIntent.Retry) },
        sharedElementModifierProvider = sharedElementModifierProvider
    )

}

@Composable
private fun MovieDetailsScaffold(
    movie: Movie,
    viewState: MovieDetailsState,
    sharedElementModifierProvider: SharedElementModifierProvider,
    onRetryClicked: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(15.dp)
        ) {

            AnimatingProgressBar(viewState is MovieDetailsState.Loading)

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {

                val (poster, title) = createRefs()

                PosterImage(
                    posterUrl = movie.poster,
                    posterHeight = 200.dp,
                    modifier = sharedElementModifierProvider.sharedImageModifier(movie.imdbID)
                        .constrainAs(poster) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        },
                    crossFade = false
                )

                Text(
                    text = movie.title ?: "-",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = sharedElementModifierProvider.sharedTitleModifier(movie.imdbID)
                        .constrainAs(title) {
                            start.linkTo(poster.end, margin = 16.dp)
                            top.linkTo(poster.top)
                            end.linkTo(parent.end, margin = 16.dp)
                            width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                        }
                )

            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    modifier = sharedElementModifierProvider.sharedYearModifier(movie.imdbID),
                    text = movie.year ?: "-",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )

                if(viewState is MovieDetailsState.Success){
                    val separator = " | "
                    val text = listOfNotNull(
                        viewState.movieDetails.rated,
                        viewState.movieDetails.runtime
                    ).filter { it.isNotBlank() }.joinToString(separator).let {
                        if (it.isNotBlank()) "$separator$it"
                        else it
                    }

                    Text(
                        modifier = Modifier.animateInFromRight(startDelay = DETAILS_ITEM_STEP_OFFSET),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        text = text
                    )
                }

            }


            when(viewState){
                is MovieDetailsState.Error -> {
                    ErrorScreen(
                        onRetryClicked = onRetryClicked,
                        message = viewState.error ?: stringResource(R.string.movie_details_error),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 30.dp)
                    )
                }
                MovieDetailsState.Loading -> { } //already handled
                is MovieDetailsState.Success -> {

                    val movieDetails = viewState.movieDetails

                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .animateInFromRight(),
                        text = movieDetails.genre ?: "-",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .animateInFromRight(startDelay = 2 * DETAILS_ITEM_STEP_OFFSET),
                    ) {

                        RatingText(
                            rating = movieDetails.imdbRating,
                            iconRes = R.drawable.imdb
                        )

                        RatingText(
                            modifier = Modifier.padding(start = 40.dp),
                            rating = movieDetails.metascore,
                            iconRes = R.drawable.metacritic
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .animateInFromRight(startDelay = 3 * DETAILS_ITEM_STEP_OFFSET),
                        text = stringResource(R.string.votes, movieDetails.imdbVotes ?: "-"),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 25.dp)
                            .animateInFromRight(startDelay = 4 * DETAILS_ITEM_STEP_OFFSET),
                        text = movieDetails.plot ?: "",
                        fontSize = 16.sp
                    )

                    CrewText(
                        modifier = Modifier
                            .padding(top = 25.dp)
                            .animateInFromRight(startDelay = 5 * DETAILS_ITEM_STEP_OFFSET),
                        movieDetails = movieDetails
                    )
                }
            }

        }
    }
}


@Composable
@Preview
fun PreviewSuccess() {
    MovieDetailsScaffold(
        movie = Movie(
            imdbID = "tt4154796",
            poster = "https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_SX300.jpg",
            title = "Avengers - Endgame",
            year = "2019"
        ),
        viewState = MovieDetailsState.Success(
            MovieDetails(
                title = "Avengers - Endgame",
                imdbRating = "8.4",
                metascore = "78",
                imdbVotes = "100.000 votes",
                plot = "The Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                genre = "Action, Adventure, Drama",
                director = "Anthony Russo, Joe Russo",
                writer = "Christopher Markus, Stephen McFeely",
                actors = "Robert Downey Jr., Chris Evans, Mark Ruffalo",
                year = "2019",
                rated = "PG-13",
                runtime = "181 min"
            )
        ),
        sharedElementModifierProvider = SharedElementModifierProvider(),
        onRetryClicked = {}
    )
}

@Composable
@Preview
fun PreviewError() {
    MovieDetailsScaffold(
        movie = Movie(
            imdbID = "tt4154796",
            poster = "https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_SX300.jpg",
            title = "Avengers - Endgame",
            year = "2019"
        ),
        viewState = MovieDetailsState.Error(error = "Error loading movie details"),
        sharedElementModifierProvider = SharedElementModifierProvider(),
        onRetryClicked = {}
    )
}
