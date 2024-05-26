package presentation.screen.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import presentation.screen.task.TaskScreen

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel: HomeScreenViewModel = getScreenModel()
        val activeTasks by remember {
            viewModel.activeTasks
        }

        var bgnColor by remember {
            mutableStateOf(Color.White)
        }

        val completedTasks by remember {
            viewModel.completedTasks
        }

        bgnColor = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray

        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Home Screen",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(35.dp),
                    colors = TopAppBarColors(
                        containerColor = bgnColor,
                        scrolledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.push(TaskScreen()) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { paddingValues: PaddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
            ) {
                DisplayTaskCompose(
                    modifier = Modifier.weight(1f),
                    tasks = activeTasks,
                    onSelected = { navigator.push(TaskScreen(it)) },
                    onCompleted = {
                        viewModel.setCompleted(it, !it.isCompleted)
                    },
                    onDeleted = {
                        viewModel.deleteTask(it)
                    },
                    onFavorite = {
                        viewModel.setFavorite(it, !it.favorite)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                DisplayTaskCompose(
                    modifier = Modifier.weight(1f),
                    tasks = completedTasks,
                    showActive = false,
                    onSelected = { navigator.push(TaskScreen(it)) },
                    onCompleted = {
                        viewModel.setCompleted(it, !it.isCompleted)
                    },
                    onDeleted = { viewModel.deleteTask(it) },
                    onFavorite = { viewModel.setFavorite(it, !it.favorite) }
                )
            }

        }
    }
}