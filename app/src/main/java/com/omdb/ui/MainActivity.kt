package com.omdb.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.omdb.core.framework.AppConstants
import com.omdb.domain.Movie
import com.omdb.presentation.ui.MovieDetailsScreen
import com.omdb.presentation.ui.SearchMoviesScreen
import com.omdb.presentation.ui.SharedElementModifierProvider
import com.omdb.presentation.ui.parcelableType
import com.omdb.ui.theme.OmdbComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OmdbComposeTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SharedTransitionLayout {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.Search,
                            enterTransition = ScreenTransitions.enterTransition,
                            exitTransition = ScreenTransitions.exitTransition,
                        ){

                            composable<Routes.Search>{
                                val modifierProvider = createSharedElementModifierProvider(this)
                                SearchMoviesScreen(
                                    openMovieDetails = { navController.navigate(Routes.Details(it)) },
                                    sharedElementModifierProvider = modifierProvider
                                )
                            }

                            composable<Routes.Details>(
                                typeMap = mapOf(typeOf<Movie>() to parcelableType<Movie>()),
                            ){
                                val modifierProvider = createSharedElementModifierProvider(this)
                                val movie = it.toRoute<Routes.Details>().movie
                                MovieDetailsScreen(
                                    movie = movie,
                                    sharedElementModifierProvider = modifierProvider
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Benefits of using this approach:
    // 1. Define all shared element transitions in one place making it easier to maintain and edit
    // 2. Avoid code duplication by declaring each sharedElement modifier only once
    // 3. Don't need to define all involved composables as extensions of SharedTransitionScope
    // 4. No need to wrap previews in SharedTransitionLayout
    // 5. Easily find where each shared element transition is used by finding usages of provider lambda
    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    private fun SharedTransitionScope.createSharedElementModifierProvider(
        scope: AnimatedVisibilityScope
    ) = SharedElementModifierProvider(
        sharedImageModifier =  { createSharedElementModifier("moviePoster/${it}", scope) },
        sharedTitleModifier = { createSharedElementModifier("movieTitle/${it}", scope) },
        sharedYearModifier = { createSharedElementModifier("releaseYear/${it}", scope) }
    )

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    private fun SharedTransitionScope.createSharedElementModifier(
        key: String,
        scope: AnimatedVisibilityScope
    ) = Modifier.sharedElement(
        state = rememberSharedContentState(key = key),
        animatedVisibilityScope = scope,
        boundsTransform = { _, _ -> tween(durationMillis = AppConstants.SHARED_ELEMENT_TRANSITION_DURATION) }
    )
}