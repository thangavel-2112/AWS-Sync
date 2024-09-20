package com.apps.notesapp.note_ui

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.amplifyframework.datastore.generated.model.Note
import com.apps.notesapp.note_db.NoteFunctions

@Composable
fun ViewNotes(onNoteAdded:()-> Unit) {
    val notes = remember { mutableStateOf(listOf<Note>()) }
    LaunchedEffect(Unit) {
        NoteFunctions.note_crud.fetchPosts(
            onResult = { fetchedNotes ->
                notes.value = fetchedNotes
                onNoteAdded()
            },
            onError = {
                Log.e("TAG", "Error fetching notes")
            }
        )
    }
    LazyColumn {
        items(notes.value) { note ->
            NoteCard(
                title = note.title,
                description = note.description,
                onEditClick = {  }
            ) {

            }
        }
    }
}
