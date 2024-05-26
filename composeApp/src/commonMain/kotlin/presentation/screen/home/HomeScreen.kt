package presentation.screen.home

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import domain.RequestState

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Home", color = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
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
                    tasks = RequestState.Idle,
                    onSelected = { /*TODO*/ },
                    onCompleted = { /*TODO*/ },
                    onDeleted = { /*TODO*/ },
                    onFavorite = { /*TODO*/ }
                )
                Spacer(modifier = Modifier.height(12.dp))
                DisplayTaskCompose(
                    modifier = Modifier.weight(1f),
                    tasks = RequestState.Idle,
                    showActive = false,
                    onSelected = { /*TODO*/ },
                    onCompleted = { /*TODO*/ },
                    onDeleted = { /*TODO*/ },
                    onFavorite = { /*TODO*/ }
                )
            }

        }
    }
}