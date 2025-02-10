package com.example.duckit.presentation.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.duckit.R
import com.example.duckit.ui.theme.DuckitTheme

@Composable
fun VotingAlertDialog(
    modifier: Modifier = Modifier,
    shouldShowDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
) {
    if (shouldShowDialog.value) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                shouldShowDialog.value = false
            },
            title = { Text(text = stringResource(R.string.sign_in_to_vote)) },
            text = { Text(text = stringResource(R.string.must_be_signed_in)) },
            confirmButton = {
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                        onConfirm()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.get_access),
                        color = MaterialTheme.colorScheme.background,
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { shouldShowDialog.value = false }
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        color = MaterialTheme.colorScheme.background,
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVotingAlertDialog() {
    DuckitTheme {
        VotingAlertDialog(
            shouldShowDialog = remember { mutableStateOf(true) },
            onConfirm = {},
        )
    }
}