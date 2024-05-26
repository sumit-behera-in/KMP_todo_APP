package presentation.screen.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.ToDoTask


const val DEFAULT_TITLE = "Enter The Title"
const val DEFAULT_DESCRIPTION = "Enter The Description"

class TaskScreen(private val task: ToDoTask? = null) : Screen {
    @Composable
    override fun Content() {

        var showDialog by remember { mutableStateOf(false) }
        var taskToDelete by remember { mutableStateOf<ToDoTask?>(null) }

        var currentTitle by remember {
            mutableStateOf(task?.title ?: "")
        }
        var currentDescription by remember {
            mutableStateOf(task?.description ?: "")
        }
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<TaskViewModel>()


        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back Arrow"
                        )
                    }

                    Text("Edit Screen", style = MaterialTheme.typography.titleLarge)

                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }

            },
            floatingActionButton = {
                if (currentTitle.isNotBlank() && currentDescription.isNotBlank()) {
                    FloatingActionButton(
                        onClick = {
                            if (task != null) {
                                viewModel.setAction(
                                    action = TaskAction.Update(
                                        ToDoTask().apply {
                                            _Id = task._Id
                                            title = currentTitle
                                            description = currentDescription
                                        }
                                    )
                                )
                            } else {
                                viewModel.setAction(
                                    action = TaskAction.Add(
                                        ToDoTask().apply {
                                            title = currentTitle
                                            description = currentDescription
                                        }
                                    )
                                )
                            }
                            navigator.pop()
                        },
                        shape = RoundedCornerShape(size = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checkmark Icon"
                        )
                    }
                }
            }
        ) {


            Column(
                modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f),
                    value = currentTitle,
                    placeholder = { Text(text = DEFAULT_TITLE) },
                    onValueChange = { title -> currentTitle = title },
                )

                Spacer(modifier = Modifier.weight(0.03f))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.9f),
                    value = currentDescription,
                    placeholder = { Text(text = DEFAULT_DESCRIPTION) },
                    onValueChange = { title -> currentDescription = title },
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                title = {
                    Text("Delete")
                },
                text = {
                    Text("Are you sure you want to delete this task?")
                },
                onDismissRequest = {
                    showDialog = false
                    taskToDelete = null
                },
                confirmButton = {
                    Button(onClick = {
                        task?.let { TaskAction.Delete(it) }
                            ?.let { viewModel.setAction(it) }
                        navigator.pop()
                        showDialog = false
                        taskToDelete = null
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = false
                        taskToDelete = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}