import java.time.LocalDate

class TaskController {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1

    fun addTask(){
        println("Añadiendo tarea")
        val id = nextId++
        println("Id asignado = $id")

        println("Titulo")
        val titulo = readlnOrNull()?.trim().orEmpty()

        println("Estado")
        println("1. Completado \n2. En progreso")

        val estado = when (readlnOrNull()?.trim().orEmpty()) {
            "1" -> true
            else -> false
        }

        println("Descripcion")
        val descripcion = readlnOrNull()?.trim().orEmpty()

        var fecha: String
        println("Fecha vencimiento (YYYY-MM-DD)")
        while (true) {
            val input = readlnOrNull()?.trim().orEmpty()
            try {
                // Intentamos parsear la fecha
                val localDate = LocalDate.parse(input)
                fecha = localDate.formatAsMonthDay()
                break // Salimos del bucle si la fecha es correcta
            } catch (_: Exception) {
                println("Formato de fecha inválido. Por favor ingresa YYYY-MM-DD.")
            }
        }

        println("Categoria")
        val categoria = readlnOrNull()?.trim().orEmpty()

        val categoriaEnum = Category.entries.find{it.name == categoria.uppercase()}?:Category.OTHER

        val tarea = Task(id, titulo, descripcion, fecha, categoriaEnum,estado)
        tasks.add(tarea)
        println("Tarea añadida satisfactoriamente")
    }
    fun listTasks() {
        if (tasks.isEmpty()){
            println("Lista de tareas vacía")
            return
        }
        println("-------------Listando tareas-------------")
        for (task in tasks) {
            val st = if(task.isDone) "Completado" else "En progreso"
            println("Tarea ${task.id} | Titulo: ${task.title} | Descripcion: ${task.description} | Estado: $st | Categoria: ${task.category} | Fecha vencimiento (MM DD): ${task.dueDate}")
        }
    }
    fun markTasks(){
        if (tasks.isEmpty()){
            println("Lista de tareas vacía")
            return
        }
        println("-------------Listando tareas incompletas-------------")
        for (task in tasks) {
            if(!task.isDone)
                println("Tarea ${task.id} | Titulo: ${task.title} | Fecha vencimiento (MM DD): ${task.dueDate}")
        }
        println("Seleccione una tarea (id) para marcarla como completada")
        val i = readlnOrNull()?.trim().orEmpty().toIntOrNull()
        if (i == null) {
            println("ID invalido")
        }else{
            val task = tasks.find { it.id == i }
            if (task == null) {
                 println("No se encontro tarea para el id $i")
            }else{
                task.isDone = true
        }
        }
    }


    fun filterTasks(completed: Boolean){
        if (tasks.isEmpty()){
            println("Lista de tareas vacía")
            return
        }
        val st = if(completed) "Completado" else "En progreso"
        println("-------------Listando tareas ($st)--------------")
        for (task in tasks) {
            if(task.isDone == completed){
                println("Tarea ${task.id} | Titulo: ${task.title} | Estado: $st | Descripcion: ${task.description} | Fecha: ${task.dueDate} | Categoria: ${task.category}")
            }
        }
}
}