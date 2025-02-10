package com.example.duckit.presentation.screen.home.ui

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.duckit.presentation.component.dialog.VotingAlertDialog
import com.example.duckit.presentation.component.state.ShowError
import com.example.duckit.presentation.component.state.ShowLoading
import com.example.duckit.presentation.model.PostUiModel
import com.example.duckit.presentation.navigation.ScreenRoute
import com.example.duckit.presentation.screen.home.HomeUiAction
import com.example.duckit.presentation.screen.home.HomeUiEvent
import com.example.duckit.presentation.screen.home.HomeUiState
import com.example.duckit.presentation.screen.home.HomeViewModel
import com.example.duckit.presentation.screen.home.ScreenState
import com.example.duckit.presentation.util.mockPostUiModel1
import com.example.duckit.presentation.util.mockPostUiModel2
import com.example.duckit.ui.theme.DuckitTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is HomeUiEvent.OnError -> Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()

                    HomeUiEvent.OnConfirmDialog -> navController.navigate(ScreenRoute.Access.name)
                }
            }
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
            isSignedIn = state.screenState.isSignedIn,
            onAction = onAction
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    posts: List<PostUiModel>,
    isSignedIn: Boolean,
    onAction: (HomeUiAction) -> Unit
) {
    val shouldShowDialog = rememberSaveable { mutableStateOf(!isSignedIn) }

    if (shouldShowDialog.value) {
        VotingAlertDialog(
            shouldShowDialog = shouldShowDialog,
            onConfirm = { onAction(HomeUiAction.OnConfirmDialog) },
        )
    }

    LazyColumn(modifier = modifier) {
        items(
            items = posts,
            key = { post -> post.id }
        ) { post ->
            UpvoteCard(
                post = post,
                canVote = isSignedIn,
                onUpvote = { postId -> onAction(HomeUiAction.OnUpvote(postId)) },
                onDownvote = { postId -> onAction(HomeUiAction.OnDownvote(postId)) }
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
            isSignedIn = true,
            onAction = {}
        )
    }
}