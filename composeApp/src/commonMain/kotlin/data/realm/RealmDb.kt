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
        return realm?.query<ToDoTask>(query = "completed == $0", false)?.asFlow()
            ?.map { result ->
                RequestState.Success(result.list.sortedByDescending { task -> task.favorite })
            } ?: flow {
            RequestState.Error("Realm not available")
        }
    }

    fun readAllCompletedTasks(): Flow<RequestState<List<ToDoTask>>> {
        return realm?.query<ToDoTask>(query = "completed == $0", true)?.asFlow()
            ?.map { result ->
                RequestState.Success(result.list.sortedByDescending { task -> task.favorite })
            } ?: flow {
            RequestState.Error("Realm not available")
        }
    }

    private suspend fun addTask(task: ToDoTask) {
        try {
            realm?.write { copyToRealm(task) }
        } catch (e: Exception) {
            RequestState.Error(e.message ?: "Realm not found")
        }
    }

    fun deleteTask(task: ToDoTask) {
        realm?.writeBlocking {
            try {
                val results = realm?.query<ToDoTask>(query = "_id == $0", task._Id)?.find()
                if (results != null) {
                    delete(results)
                } else {
                    throw IllegalArgumentException("Task not found")
                }
            } catch (e: Exception) {
                RequestState.Error(e.message ?: "Task not found")
            }
        }
    }

    suspend fun updateTask(task: ToDoTask) {
        try {
            realm?.write {
                val results = realm?.query<ToDoTask>(query = "_id == $0", task._Id)?.first()?.find()
                if (results != null) {
                    results.title = task.title
                    results.description = task.description
                    results.isCompleted = task.isCompleted
                    results.favorite = task.favorite
                } else {
                    throw IllegalArgumentException("Task not found")
                }
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
            val result = realm?.query<ToDoTask>(query = "_id == $0", task._Id)?.first()?.find()
            result?.apply {
                favorite = isFavorite
            } ?: RequestState.Error("Task not found")
        }
    }

    suspend fun setTaskCompleted(task: ToDoTask, isTaskCompleted: Boolean) {
        realm?.write {
            val result = realm?.query<ToDoTask>(query = "_id == $0", task._Id)?.first()?.find()
            result?.apply {
                isCompleted = isTaskCompleted
            } ?: RequestState.Error("Task not found")
        }
    }

}


