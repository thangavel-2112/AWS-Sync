package com.apps.notesapp.note_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateNoteDialog(
    header: String,
    titleTxt: String,
    descriptionTxt: String,
    buttonText: String,
    onDismissRequest: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(titleTxt) }
    var description by remember { mutableStateOf(descriptionTxt) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = header) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(title, description)
                    onDismissRequest()
                },
                enabled = title.isNotEmpty() && description.isNotEmpty()
            ) {
                Text(buttonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
