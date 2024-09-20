package com.apps.notesapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.plugin.Plugin
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.apps.notesapp.note_ui.CreateNoteDialog
import com.apps.notesapp.note_ui.NotesViewModel
import com.apps.notesapp.note_ui.ViewNotes
import com.apps.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Amplify.addPlugin(AWSDataStorePlugin())
            Amplify.addPlugin<Plugin<*>>(AWSApiPlugin())
            Amplify.configure(applicationContext)

            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
        setContent {
            NotesAppTheme {
                var showDialog by remember { mutableStateOf(false) }
                val notesViewModel: NotesViewModel = viewModel()
                val lifecycleOwner = LocalLifecycleOwner.current
                observeNoteCreation(notesViewModel, lifecycleOwner)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Text(
                            text = "My Notes",
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontFamily = FontFamily.Cursive,
                                fontWeight = FontWeight(500)
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showDialog = true },
                            modifier = Modifier.padding(bottom = 30.dp),
                            containerColor = Color.White,
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        )
                        ViewNotes(notesViewModel)
                    }
                    if (showDialog) {
                        CreateNoteDialog(
                            header = "Create Note",
                            titleTxt = "",
                            descriptionTxt = "",
                            buttonText = "Submit",
                            onDismissRequest =
                            {
                                showDialog = false
                            },
                            onSubmit = { title, description ->
                                notesViewModel.createNote(title, description)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun observeNoteCreation(
        notesViewModel: NotesViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        notesViewModel.createNoteResponse.observe(lifecycleOwner) {
            when (it) {
                true -> {
                    Toast.makeText(this, "Note created successfully", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, "Couldn't create note!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        notesViewModel.editNoteResponse.observe(lifecycleOwner) {
            when (it) {
                true -> {
                    Toast.makeText(this, "Note edited successfully", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, "Couldn't edit note!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        notesViewModel.deleteNoteResponse.observe(lifecycleOwner) {
            when (it) {
                true -> {
                    Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, "Couldn't delete note!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}