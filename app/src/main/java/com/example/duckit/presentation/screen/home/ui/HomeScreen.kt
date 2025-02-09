package com.example.duckit.presentation.screen.home.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.duckit.presentation.component.ShowError
import com.example.duckit.presentation.component.ShowLoading
import com.example.duckit.presentation.model.PostUiModel
import com.example.duckit.presentation.screen.home.HomeUiAction
import com.example.duckit.presentation.screen.home.HomeUiState
import com.example.duckit.presentation.screen.home.HomeViewModel
import com.example.duckit.presentation.screen.home.ScreenState
import com.example.duckit.presentation.util.mockPostUiModel1
import com.example.duckit.presentation.util.mockPostUiModel2
import com.example.duckit.ui.theme.DuckitTheme
import kotlinx.coroutines.flow.onEach

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    //navController: NavController,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.events.onEach { event ->
//            when (event) {
//
//            }
        }
    }

    HomeLayout(
        modifier = modifier,
        state = state,
        onAction = viewModel::handleAction
    )
}

@Composable
fun HomeLayout(
    modifier: Modifier,
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit
) {
    when (state.screenState) {
        ScreenState.Initial -> {}
        ScreenState.Loading -> ShowLoading()
        is ScreenState.Error -> ShowError(message = state.screenState.message)
        is ScreenState.Data -> HomeContent(
            modifier = modifier,
            posts = state.screenState.items,
            onAction = onAction
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    posts: List<PostUiModel>,
    onAction: (HomeUiAction) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = posts,
            key = { post -> post.id }
        ) { post ->
            UpvoteCard(
                post = post,
                canVote = true,
                onUpvote = {  },
                onDownvote = {  }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    DuckitTheme {
        HomeContent(
            posts = listOf(mockPostUiModel1, mockPostUiModel2),
            onAction = {}
        )
    }
}