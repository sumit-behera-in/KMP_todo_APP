package presentation.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.RequestState
import domain.ToDoTask
import presentation.screen.components.ErrorScreen
import presentation.screen.components.LoadingScreen
import presentation.screen.components.TaskViewer

@Composable
fun DisplayTaskCompose(
    modifier: Modifier = Modifier,
    tasks: RequestState<List<ToDoTask>>,
    showActive: Boolean = true,
    onSelected: (ToDoTask) -> Unit,
    onFavorite: (ToDoTask) -> Unit,
    onCompleted: (ToDoTask) -> Unit,
    onDeleted: (ToDoTask) -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<ToDoTask?>(null) }

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
                    taskToDelete?.let { onDeleted.invoke(it) }
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

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = if (showActive) "Active Tasks" else "Completed Tasks",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        tasks.DisplayResult(
            onLoading = { LoadingScreen() },
            onError = { ErrorScreen(message = it) },
            onSuccess = { list ->
                if (list.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                        items(
                            count = list.size,
                        ) { index ->
                            TaskViewer(
                                showActive = showActive,
                                task = list[index],
                                onSelected = { onSelected.invoke(list[index]) },
                                onCompleted = { selectedTask, _ ->
                                    onCompleted(selectedTask)
                                },
                                onFavorite = { selectedTask, _ ->
                                    onFavorite.invoke(selectedTask)
                                },
                                onDeleted = { selectedTask ->
                                    taskToDelete = selectedTask
                                    showDialog = true
                                }
                            )
                        }
                    }
                } else {
                    ErrorScreen()
                }
            }
        )
    }
}
