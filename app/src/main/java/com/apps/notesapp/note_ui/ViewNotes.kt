package com.apps.notesapp.note_ui

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ViewNotes(viewModel: NotesViewModel) {
    val notesList by viewModel.notes.observeAsState(emptyList())
    var titleTxt by remember { mutableStateOf("") }
    var descriptionTxt by remember { mutableStateOf("") }
    var noteId by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    LazyColumn {
        items(notesList) { note ->
            NoteCard(
                title = note.title,
                description = note.description,
                onEditClick = {
                    titleTxt = note.title
                    descriptionTxt = note.description
                    noteId = note.id
                    showDialog = true
                }
            ) {
                showDeleteDialog = true
                noteId = note.id
            }
        }
    }
    if (showDialog) {
        CreateNoteDialog(
            header = "Edit Note",
            titleTxt = titleTxt,
            descriptionTxt = descriptionTxt,
            buttonText = "Edit",
            onDismissRequest =
            {
                showDialog = false
            },
            onSubmit = { title, description ->
                Log.e("TAG", "ViewNotes: edit ${title} ${description} ")
                viewModel.editNote(noteId, title, description)
            }
        )
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(onDismiss = { showDeleteDialog = false }) {
            viewModel.deleteNote(noteId)
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Note") },
        text = { Text("Are you sure you want to delete this note?") },
        confirmButton = {
            TextButton(onClick = {
                onDeleteConfirmed()
                onDismiss()
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
