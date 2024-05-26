package domain

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ToDoTask :RealmObject {
    @PrimaryKey
    var _Id= ObjectId()
    var title: String = ""
    var description: String = ""
    var isCompleted: Boolean = false
    var favorite: Boolean = false
}