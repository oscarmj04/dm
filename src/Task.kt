import java.time.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: LocalDate,
    val category: Category,
    var isDone: Boolean = false
)
