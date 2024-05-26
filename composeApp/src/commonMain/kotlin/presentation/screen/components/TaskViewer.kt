package presentation.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.ToDoTask

@Composable
fun TaskViewer(
    modifier: Modifier = Modifier,
    task: ToDoTask,
    showActive: Boolean = true,
    onSelected: (ToDoTask) -> Unit,
    onCompleted: (ToDoTask, Boolean) -> Unit,
    onFavorite: (ToDoTask, Boolean) -> Unit,
    onDeleted: (ToDoTask) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (showActive) {
                    onSelected(task)
                } else {
                    onDeleted(task)
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Checkbox(
            modifier = Modifier.size(20.dp),
            checked = task.isCompleted,
            onCheckedChange = { onCompleted(task, !task.isCompleted) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.alpha(if (showActive) 1f else 0.5f).weight(0.8f).fillMaxWidth(),
            text = task.title,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textDecoration = if (showActive) null else TextDecoration.LineThrough,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.width(8.dp))


        IconButton(
            onClick = {
                if (task.isCompleted) {
                    onDeleted(task)
                } else {
                    onFavorite(task, !task.favorite)
                }
            }
        ) {
            Icon(
                imageVector = if (!task.isCompleted) {
                    Icons.Default.Star
                } else {
                    Icons.Default.Delete
                },
                contentDescription = if (!task.isCompleted) {
                    "Favorite"
                } else {
                    "Delete"
                },
                tint = if (task.favorite || task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.3f
                )
            )
        }
    }
}