package com.omdb.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.omdb.domain.Movie

@Composable
fun MovieItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: (Movie) -> Unit = {},
    sharedElementModifierProvider: SharedElementModifierProvider
) {

    ConstraintLayout(
        modifier = modifier.fillMaxWidth().clickable {
             onClick(movie)
        }
    ) {
        val(poster, title, year) = createRefs()

        PosterImage(
            posterUrl = movie.poster,
            posterHeight = 160.dp,
            crossFade = true,
            modifier = sharedElementModifierProvider.sharedImageModifier(movie.imdbID)
                .constrainAs(poster) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = movie.title ?: "-",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = sharedElementModifierProvider.sharedTitleModifier(movie.imdbID)
                .padding(vertical = 15.dp)
                .verticalScroll(rememberScrollState()) // in case the title is too long
                .constrainAs(title) {
                    start.linkTo(poster.end, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    top.linkTo(poster.top)
                    bottom.linkTo(year.top)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )
        
        Text(
            text = movie.year ?: "-",
            style = MaterialTheme.typography.bodyLarge,
            modifier = sharedElementModifierProvider.sharedYearModifier(movie.imdbID)
                .constrainAs(year) {
                    start.linkTo(poster.end, margin = 16.dp)
                    bottom.linkTo(poster.bottom, margin = 16.dp)
                }
        )
    }


}

@Preview
@Composable
fun Preview(){
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            MovieItem(
                sharedElementModifierProvider = SharedElementModifierProvider(),
                movie = Movie(
                    imdbID = "1",
                    poster = "https://m.media-amazon.com/images/M/MV5BMmFiZGZjMmEtMTA0Ni00MzA2LTljMTYtZGI2MGJmZWYzZTQ2XkEyXkFqcGc@._V1_SX300.jpg",
                    title = "this is \n a really \nlong\ntitle\ntitle\ntitle",
                    year = "2024"
                )
            )
        }
    }
}