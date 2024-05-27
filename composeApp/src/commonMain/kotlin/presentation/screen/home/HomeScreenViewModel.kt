package presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.realm.RealmDb
import domain.RequestState
import domain.ToDoTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias MutableTasks = MutableState<RequestState<List<ToDoTask>>>

class HomeScreenViewModel(private val realm: RealmDb) : ScreenModel {

    private var _activeTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val activeTasks: MutableTasks = _activeTasks

    private var _completedTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val completedTasks: MutableTasks = _completedTasks

    init {
        _activeTasks.value = RequestState.Loading
        _completedTasks.value = RequestState.Loading

        screenModelScope.launch(Dispatchers.IO) {
            delay(500)
            realm.readActiveTasks().collectLatest {
                _activeTasks.value = it
            }
        }

        screenModelScope.launch(Dispatchers.IO) {
            delay(500)
            realm.readAllCompletedTasks().collectLatest {
                _completedTasks.value = it
            }
        }


    }

    fun deleteTask(task: ToDoTask) {
        screenModelScope.launch(Dispatchers.IO) {
            realm.deleteTask(task = task)
        }

    }

    fun setFavorite(task: ToDoTask, favorite: Boolean) {
        screenModelScope.launch(Dispatchers.IO) {
            realm.setTaskFavorite(task = task, isFavorite = favorite)
        }
    }

    fun setCompleted(task: ToDoTask, completed: Boolean) {
        screenModelScope.launch(Dispatchers.IO) {
            realm.setTaskCompleted(task = task, isTaskCompleted = completed)
        }
    }
}