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

    var realm: Realm? = null

    private fun configure() {
        if (realm == null || realm?.isClosed() == true) {
            val config = RealmConfiguration.Builder(schema = setOf(ToDoTask::class))
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    fun readAllTasks(): Flow<RequestState<List<ToDoTask>>> {
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

    suspend fun addTask(task: ToDoTask) {
        realm?.write { copyToRealm(task) }
    }

    fun deleteTask(task: ToDoTask) {
        realm?.writeBlocking {
            val results = realm?.query<ToDoTask>(query = "_id == $0", task._Id)?.find()
            if (results != null) {
                delete(results)
            } else {
                RequestState.Error("Task not found")
            }
        }
    }
}