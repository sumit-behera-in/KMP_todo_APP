package data.realm

import domain.RequestState
import domain.ToDoTask
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RealmDb {

    private var realm: Realm? = null

    init {
        if (realm == null || realm?.isClosed() == true) {
            val config = RealmConfiguration.Builder(schema = setOf(ToDoTask::class))
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    fun readActiveTasks(): Flow<RequestState<List<ToDoTask>>> {
        return realm?.query<ToDoTask>(query = "isCompleted == $0", false)?.asFlow()
            ?.map { result ->
                RequestState.Success(result.list.sortedByDescending { task -> task.favorite })
            } ?: flow {
            RequestState.Error("Realm not available")
        }
    }

    fun readAllCompletedTasks(): Flow<RequestState<List<ToDoTask>>> {
        return realm?.query<ToDoTask>(query = "isCompleted == $0", true)?.asFlow()
            ?.map { result ->
                RequestState.Success(result.list.sortedByDescending { task -> task.favorite })
            } ?: flow {
            RequestState.Error("Realm not available")
        }
    }

    suspend fun addTask(task: ToDoTask) {
        try {
            realm?.write { copyToRealm(task) }
        } catch (e: Exception) {
            RequestState.Error(e.message ?: "Realm not found")
        }
    }

    suspend fun deleteTask(task: ToDoTask) {
        realm?.write {
            try {
                val results = this.query<ToDoTask>(query = "_Id == $0", task._Id).find()
                delete(results)
            } catch (e: Exception) {
                RequestState.Error(e.message ?: "Task not found")
            }
        }
    }

    suspend fun updateTask(task: ToDoTask) {
        try {
            realm?.write {
                val results = this.query<ToDoTask>(query = "_Id == $0", task._Id).find().first()
                results.title = task.title
                results.description = task.description
                results.isCompleted = task.isCompleted
                results.favorite = task.favorite
            }
        } catch (e: Exception) {
            if (e.message == "Task not found") {
                addTask(task)
            } else {
                RequestState.Error(e.message ?: "Realm not found")
            }
        }
    }

    suspend fun setTaskFavorite(task: ToDoTask, isFavorite: Boolean) {
        realm?.write {
            val result = this.query<ToDoTask>(query = "_Id == $0", task._Id).find().first()
            result.apply {
                favorite = isFavorite
            }
        }
    }

    suspend fun setTaskCompleted(task: ToDoTask, isTaskCompleted: Boolean) {
        realm?.write {
            val result = this.query<ToDoTask>(query = "_Id == $0", task._Id).find().first()
            result.apply {
                isCompleted = isTaskCompleted
            }
        }
    }

}


