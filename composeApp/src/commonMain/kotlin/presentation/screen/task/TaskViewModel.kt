package presentation.screen.task

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.realm.RealmDb
import kotlinx.coroutines.launch

class TaskViewModel(private val realmDb: RealmDb) : ScreenModel {
    fun setAction(action: TaskAction) {
        when (action) {
            is TaskAction.Add -> {
                screenModelScope.launch {
                    realmDb.addTask(task = action.task)
                }
            }

            is TaskAction.Update -> {
                screenModelScope.launch {
                    realmDb.updateTask(task = action.task)
                }
            }

            is TaskAction.Delete -> {
                screenModelScope.launch {
                    realmDb.deleteTask(task = action.task)
                }
            }

            is TaskAction.SetCompleted -> {
                screenModelScope.launch {
                    realmDb.setTaskCompleted(task = action.task, isTaskCompleted = action.completed)
                }
            }

            is TaskAction.SetFavorite -> {
                screenModelScope.launch {
                    realmDb.setTaskFavorite(task = action.task, isFavorite = action.favorite)
                }
            }
        }
    }
}