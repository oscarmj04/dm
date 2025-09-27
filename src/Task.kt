import java.time.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val category: Category,
    var isDone: Boolean
)
