package com.apps.notesapp.note_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amplifyframework.datastore.generated.model.Priority

@Composable
fun CreateNoteDialog(
    header: String,
    titleTxt: String,
    descriptionTxt: String,
    priority: Priority,
    buttonText: String,
    onDismissRequest: () -> Unit,
    onSubmit: (String, String, Priority) -> Unit
) {
    var title by remember { mutableStateOf(titleTxt) }
    var description by remember { mutableStateOf(descriptionTxt) }
    var selectedPriority by remember { mutableStateOf(priority) }

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

                Spacer(modifier = Modifier.height(16.dp))

                Text("Priority")
                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPriority == Priority.LOW,
                            onClick = { selectedPriority = Priority.LOW }
                        )
                        Text(
                            text = "Low",
                            modifier = Modifier.clickable { selectedPriority = Priority.LOW })
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPriority == Priority.MID,
                            onClick = { selectedPriority = Priority.MID }
                        )
                        Text(
                            text = "Mid",
                            modifier = Modifier.clickable { selectedPriority = Priority.MID })
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPriority == Priority.HIGH,
                            onClick = { selectedPriority = Priority.HIGH }
                        )
                        Text(
                            text = "High",
                            modifier = Modifier.clickable { selectedPriority = Priority.HIGH })
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(title, description, selectedPriority)
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