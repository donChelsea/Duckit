package com.example.duckit.presentation.screen.create.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.duckit.R
import com.example.duckit.domain.model.NewPost
import com.example.duckit.presentation.navigation.ScreenRoute
import com.example.duckit.presentation.screen.create.CreateUiAction
import com.example.duckit.presentation.screen.create.CreateUiEvent
import com.example.duckit.presentation.screen.create.CreateViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateViewModel = hiltViewModel(),
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    CreateUiEvent.OnPostFinished -> navController.navigate(ScreenRoute.Home.name)
                    is CreateUiEvent.OnError -> Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    CreateContent(
        modifier = modifier,
        onAction = viewModel::handleAction
    )
}

@Composable
fun CreateContent(
    modifier: Modifier = Modifier,
    onAction: (CreateUiAction) -> Unit,
) {
    val context = LocalContext.current
    var headline by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_30)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.Default.Create,
                contentDescription = "",
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large)),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_40)))

        OutlinedTextField(
            value = headline,
            onValueChange = { headline = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            placeholder = { Text(stringResource(R.string.headline)) },
            label = { Text(stringResource(R.string.enter_headline)) },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_20)))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            placeholder = { Text(stringResource(R.string.image_url)) },
            label = { Text(stringResource(R.string.enter_image_url)) },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_20)))

        HorizontalLineWithText()

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_20)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.image_size))
                .border(
                    width = dimensionResource(R.dimen.border_width),
                    color = Color.Gray,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_5))
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri == null) {
                Button(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_5)),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = {
                        getContent.launch("image/*")
                    }
                ) {
                    Text(
                        text = stringResource(R.string.pick_an_image),
                        color = MaterialTheme.colorScheme.background
                    )
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_20)))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_5)),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                if (checkNewPost(headline, imageUrl, selectedImageUri, context)) {
                    val newPost = NewPost(
                        headline = headline,
                        image = imageUrl
                    )
                    onAction(CreateUiAction.OnNewPost(newPost))
                }
            }
        ) {
            Text(
                text = stringResource(R.string.post),
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
fun HorizontalLineWithText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            thickness = dimensionResource(R.dimen.separator_width),
        )
        Text(
            text = stringResource(R.string.or),
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_16))
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            thickness = dimensionResource(R.dimen.separator_width),
        )
    }
}

private fun checkNewPost(
    headline: String,
    imageUrl: String?,
    selectedImageUri: Uri?,
    context: Context
): Boolean {
    if (headline.isNotEmpty() && (imageUrl != null || selectedImageUri != null)) {
        return true
    } else {
        Toast.makeText(
            context,
            context.getString(R.string.add_headline_image_error),
            Toast.LENGTH_SHORT
        ).show()
        return false
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateContent() {
    CreateContent(onAction = {})
}
