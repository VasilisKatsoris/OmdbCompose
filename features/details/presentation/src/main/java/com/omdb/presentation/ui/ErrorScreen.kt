package com.omdb.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omdb.shared.presentation.resources.R

@Composable
internal fun ErrorScreen(
    message: String,
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(40.dp)
            .fadeInOnLoad(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )

        Button(
            modifier = Modifier.padding(15.dp),
            onClick = onRetryClicked
        ) {
            Text(text = stringResource(R.string.try_again))
        }
    }

}