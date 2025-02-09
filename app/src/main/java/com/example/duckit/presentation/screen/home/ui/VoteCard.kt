package com.example.duckit.presentation.screen.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.duckit.R
import com.example.duckit.presentation.model.PostUiModel
import com.example.duckit.presentation.util.mockPostUiModel1
import com.example.duckit.ui.theme.DuckitTheme

@Composable
fun UpvoteCard(
    modifier: Modifier = Modifier,
    post: PostUiModel,
    canVote: Boolean = false,
    onUpvote: (String) -> Unit,
    onDownvote: (String) -> Unit,
) {
    with(post) {
        var upvotes by remember { mutableIntStateOf(upvotes) }
        val thumbColor = if (canVote) MaterialTheme.colorScheme.onBackground else Color.Gray

        Column(
            modifier = modifier.padding(8.dp)
        ) {
            val context = LocalContext.current
            val placeholder = R.drawable.ic_launcher_foreground
            val imageRequest = ImageRequest.Builder(context)
                .data(image)
                .memoryCacheKey(image)
                .diskCacheKey(image)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()

            Text(
                text = headline,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(4.dp))

            AsyncImage(
                model = imageRequest,
                contentDescription = "Post image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .fillMaxWidth()
                    .requiredHeight(300.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Card(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .clip(RoundedCornerShape(50.dp))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .wrapContentWidth()
                ) {
                    IconButton(
                        enabled = canVote,
                        onClick = {
                            onUpvote(id)
                            upvotes++
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.thumb_up),
                            contentDescription = "Upvote",
                            tint = thumbColor,
                        )
                    }

                    Text(
                        text = "$upvotes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    IconButton(
                        enabled = canVote,
                        onClick = {
                            onDownvote(id)
                            upvotes--
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.thumb_down),
                            contentDescription = "Downvote",
                            tint = thumbColor,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVoteCard() {
    DuckitTheme {
        UpvoteCard(
            post = mockPostUiModel1,
            onDownvote = {},
            onUpvote = {},
        )
    }
}