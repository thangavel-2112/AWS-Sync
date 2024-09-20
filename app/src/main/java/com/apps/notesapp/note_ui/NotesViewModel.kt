package com.apps.notesapp.note_ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.datastore.generated.model.Note
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

    private fun fetchNotes() {
        viewModelScope.launch {
            NoteFunctions.note_crud.fetchNotes()
                .collect { notesList ->
                    notes.postValue(notesList)
                }
        }
    }

    fun createNote(title: String, description: String) {
        viewModelScope.launch {
            NoteFunctions.note_crud.createNote(title, description).collect { isSuccess ->
                if (isSuccess) {
                    createNoteResponse.value = true
                    fetchNotes()
                } else {
                    createNoteResponse.value = false
                }
            }
        }
    }

    fun editNote(noteId: String, title: String, description: String) {
        viewModelScope.launch {
            NoteFunctions.note_crud.editNoteById(noteId, title, description).collect { isSuccess ->
                editNoteResponse.value = isSuccess
                if (isSuccess) {
                    fetchNotes()
                }
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            NoteFunctions.note_crud.deleteNoteById(noteId).collect { isDeleted ->
                deleteNoteResponse.value = isDeleted
                if (isDeleted) {
                    fetchNotes()
                }
            }
        }
    }
}
