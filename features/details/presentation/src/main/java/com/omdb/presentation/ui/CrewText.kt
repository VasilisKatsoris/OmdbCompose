package com.omdb.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.core.text.HtmlCompat
import com.omdb.domain.MovieDetails
import com.omdb.shared.presentation.resources.R

@Composable
internal fun CrewText(
    movieDetails: MovieDetails?,
    modifier: Modifier = Modifier
) {
    val text = stringResource(
        R.string.crew_members,
        movieDetails?.director ?: "-",
        movieDetails?.writer ?: "-",
        movieDetails?.actors ?: "-"
    )
    val spanned = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
    val allSpans = spanned.getSpans(0, spanned.length, Any::class.java).toList()
    val annotatedString = buildAnnotatedString {
        append(spanned)
        allSpans.forEach { span ->
            addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold),
                start = spanned.getSpanStart(span),
                end = spanned.getSpanEnd(span),
            )
        }
    }
    Text(
        text = annotatedString,
        modifier = modifier
    )
}