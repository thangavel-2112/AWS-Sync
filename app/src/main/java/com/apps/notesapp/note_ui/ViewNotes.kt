package com.apps.notesapp.note_ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun ViewNotes(viewModel: NotesViewModel) {
    val notesList by viewModel.notes.observeAsState(emptyList())

    LazyColumn {
        items(notesList) { note ->
            NoteCard(
                title = note.title,
                description = note.description,
                onEditClick = { }
            ) {

            }
        }
    }
}