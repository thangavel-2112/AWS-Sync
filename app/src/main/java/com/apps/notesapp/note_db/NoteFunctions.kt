package com.apps.notesapp.note_db

import android.util.Log
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
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
                    trySend(true)
                },
                {
                    // Note is not saved, failure
                    Log.d("TAG", "createNote: failure")
                    trySend(false)
                }
            )
        } catch (err: DataStoreException) {
            // Error occurred while saving
            Log.e("TAG", "createNote: $err")
            trySend(false)
        }

        awaitClose { }
    }


    fun fetchNotes(): Flow<List<Note>> = flow {
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

    fun editNoteById(
        noteId: String,
        newTitle: String,
        newDescription: String
    ): Flow<Boolean> = callbackFlow {
        Amplify.DataStore.query(
            Note::class.java,
            Where.matches(Note.ID.eq(noteId)),
            { matches ->
                if (matches.hasNext()) {
                    val noteToEdit = matches.next()
                    val updatedNote = noteToEdit.copyOfBuilder()
                        .title(newTitle)
                        .description(newDescription)
                        .build()

                    Amplify.DataStore.save(updatedNote,
                        {
                            Log.d("TAG", "editNoteById: success")
                            trySend(true)
                        },
                        { error ->
                            Log.e("TAG", "editNoteById: failure", error)
                            trySend(false)
                        }
                    )
                } else {
                    Log.e("TAG", "editNoteById: No note found with id $noteId")
                    trySend(false)
                }
            },
            { error ->
                Log.e("TAG", "editNoteById: Query failed", error)
                trySend(false)
            }
        )

        awaitClose { }
    }

    fun deleteNoteById(noteId: String): Flow<Boolean> = callbackFlow {
        try {
            Amplify.DataStore.query(Note::class.java, Where.id(noteId),
                { matches ->
                    if (matches.hasNext()) {
                        val noteToDelete = matches.next()
                        Amplify.DataStore.delete(noteToDelete,
                            {
                                Log.i("TAG", "Note deleted successfully: $noteId")
                                trySend(true)
                            },
                            { error ->
                                Log.e("TAG", "Failed to delete note: $noteId", error)
                                trySend(false)
                            }
                        )
                    } else {
                        Log.e("TAG", "No note found with ID: $noteId")
                        trySend(false)
                    }
                },
                { error ->
                    Log.e("TAG", "Error querying note with ID: $noteId", error)
                    trySend(false)
                }
            )
        } catch (exception: Exception) {
            Log.e("TAG", "Error occurred during deletion: ${exception.localizedMessage}")
            trySend(false)
        }

        awaitClose { }
    }

    companion object {
        val note_crud = NoteFunctions()
    }
}