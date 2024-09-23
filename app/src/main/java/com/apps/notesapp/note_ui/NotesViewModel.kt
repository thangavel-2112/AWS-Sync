package com.apps.notesapp.note_ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.datastore.generated.model.Note
import com.amplifyframework.datastore.generated.model.Priority
import com.apps.notesapp.note_db.NoteFunctions
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    val notes = MutableLiveData<List<Note>>(emptyList())
    val createNoteResponse = MutableLiveData<Boolean>()
    val editNoteResponse = MutableLiveData<Boolean>()
    val deleteNoteResponse = MutableLiveData<Boolean>()

    init {
        fetchNotes()
    }

    fun fetchNotes() {
        viewModelScope.launch {
            NoteFunctions.note_crud.fetchNotes()
                .collect { notesList ->
                    notes.postValue(notesList)
                }
        }
    }

    fun createNote(title: String, description: String,priority: Priority) {
        viewModelScope.launch {
            NoteFunctions.note_crud.createNote(title, description,priority).collect { isSuccess ->
                createNoteResponse.value = isSuccess
            }
        }
    }

    fun editNote(noteId: String, title: String, description: String,priority: Priority) {
        viewModelScope.launch {
            NoteFunctions.note_crud.editNoteById(noteId, title, description,priority).collect { isSuccess ->
                editNoteResponse.value = isSuccess
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            NoteFunctions.note_crud.deleteNoteById(noteId).collect { isDeleted ->
                deleteNoteResponse.value = isDeleted
            }
        }
    }
}
