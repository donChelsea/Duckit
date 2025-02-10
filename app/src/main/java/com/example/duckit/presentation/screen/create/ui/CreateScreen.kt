package com.example.duckit.presentation.screen.create.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
                    CreateUiEvent.OnPostFinished -> {
                        println("post created")
                        //navController.navigate(ScreenRoute.Home.name)
                    }
                    is CreateUiEvent.OnError -> Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
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
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
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
                modifier = Modifier.size(90.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = headline,
            onValueChange = { headline = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            placeholder = { Text("Enter a headline") },
            label = { Text("Enter a headline") },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            placeholder = { Text("Image URL") },
            label = { Text("Enter the URL to an image") },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalLineWithText()

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri == null) {
                Button(
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = {
                        getContent.launch("image/*")
                    }
                ) {
                    Text(
                        text = "Pick an Image",
                        color = MaterialTheme.colorScheme.background
                    )
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
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
                text = "Post",
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
            thickness = 2.dp,
        )
        Text(
            text = "OR",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            thickness = 2.dp,
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
        Toast.makeText(context, "You'll need to add a headline and image first!", Toast.LENGTH_SHORT).show()
        return false
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateContent() {
    CreateContent(onAction = {})
}
