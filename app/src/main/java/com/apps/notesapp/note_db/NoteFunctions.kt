package com.apps.notesapp.note_db

import android.util.Log
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.generated.model.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NoteFunctions {

    fun createNote(title: String, description: String): Flow<Boolean> = callbackFlow {
        val note = Note.builder()
            .title(title)
            .description(description)
            .build()

        try {
            Amplify.DataStore.save(note,
                {
                    // Note saved successfully
                    Log.d("TAG", "createNote: success")
                    trySend(true) // Emit success
                },
                {
                    // Note is not saved, failure
                    Log.d("TAG", "createNote: failure")
                    trySend(false) // Emit failure
                }
            )
        } catch (err: DataStoreException) {
            // Error occurred while saving
            Log.e("TAG", "createNote: $err")
            trySend(false) // Emit failure on exception
        }

        awaitClose { }
    }


    fun fetchPosts(): Flow<List<Note>> = flow {
        val notes = suspendCancellableCoroutine<List<Note>> { coroutine ->
            Amplify.DataStore.query(Note::class.java,
                { matches ->
                    val notesList = mutableListOf<Note>()
                    while (matches.hasNext()) {
                        val note = matches.next()
                        notesList.add(note)
                    }
                    coroutine.resume(notesList)
                },
                { error ->
                    Log.e("MyAmplifyApp", "Error retrieving notes", error)
                    coroutine.resumeWithException(error)
                }
            )
        }
        emit(notes)
    }

    companion object {
        val note_crud = NoteFunctions()
    }
}