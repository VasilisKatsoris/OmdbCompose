package com.omdb.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omdb.shared.presentation.resources.R

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onValueChanged: (String) -> Unit,
    isLoading: Boolean
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onValueChanged,
        label = { Text(text = stringResource(R.string.search_for_a_movie)) },
        leadingIcon = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.Gray,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable { onValueChanged("") }.size(25.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = 16.dp
            )
    )
}