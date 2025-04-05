package com.omdb.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.omdb.core.framework.AppConstants
import com.omdb.core.framework.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchMoviesViewModel @Inject constructor(
    private val pagingSourceFactory: SearchPagingSourceFactory,
    dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    private val debouncedText = _searchText.debounce(AppConstants.SEARCH_TYPING_DELAY)
    private val inTypeDelayWindow = combine(_searchText, debouncedText){ searchText, debouncedText ->
        searchText != debouncedText
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData = debouncedText.flatMapLatest {
            Pager(
                config = PagingConfig(
                    pageSize = AppConstants.PAGE_SIZE,
                    prefetchDistance = AppConstants.PREFETCH_DISTANCE
                ),
                pagingSourceFactory = { pagingSourceFactory.create(it) }
            ).flow
        }
        .cachedIn(viewModelScope)
        .flowOn(dispatchersProvider.io)

    val viewState = combine(
        _searchText,
        inTypeDelayWindow
    ) { searchText, inTypeDelayWindow ->
        SearchMoviesState(
            searchText = searchText,
            inTypeDelayWindow = inTypeDelayWindow
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchMoviesState()
    )

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SearchTextTyped -> {
                _searchText.value = intent.text
            }
        }
    }

}

