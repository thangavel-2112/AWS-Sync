package com.apps.notesapp.note_db

import android.util.Log
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.generated.model.Note

class NoteFunctions {

    fun createNote(title: String, description: String, onResult: (Boolean) -> Unit) {
        val note = Note.builder()
            .title(title)
            .description(description)
            .build()

        try {
            Amplify.DataStore.save(note,
                {
                    // Note saved successfully
                    Log.d("TAG", "createNote: success")
                    onResult(true)
                },
                {
                    // Note is not saved, failure
                    Log.d("TAG", "createNote: failure ")
                    onResult(false)
                }
            )
        } catch (err: DataStoreException) {
            // Error occurred while saving
            Log.e("TAG", "createNote: $err")
            onResult(false)
        }
    }

    fun fetchPosts(onResult: (List<Note>) -> Unit, onError: (Throwable) -> Unit) {
        Amplify.DataStore.query(Note::class.java,
            { matches ->
                val notes = mutableListOf<Note>()
                while (matches.hasNext()) {
                    val note = matches.next()
                    notes.add(note)
                }
                onResult(notes)
            },
            { error ->
                Log.e("MyAmplifyApp", "Error retrieving notes", error)
                onError(error)
            }
        )
    }

    companion object {
        val note_crud = NoteFunctions()
    }
}