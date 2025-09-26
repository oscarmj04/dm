import java.time.LocalDate

class TaskController {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1

    fun addTask(){
        println("Añadiendo tarea")
        val id = nextId++
        println("Id asignado = $id")

        println("Titulo")
        val titulo = readLine()?.trim().orEmpty()

        println("Estado")
        println("1. Completado \n2. En progreso")

        val estado = when (readLine()?.trim().orEmpty()) {
            "1" -> true
            else -> false
        }

        println("Descripcion")
        val descripcion = readLine()?.trim().orEmpty()

        println("Fecha vencimiento")
        val input = readLine()?.trim().orEmpty()

        println("Categoria")
        val categoria = readLine()?.trim().orEmpty()

        val categoriaEnum = Category.entries.find{it.name == categoria.uppercase()}?:Category.PERSONAL
        val fecha = LocalDate.parse(input)
        fecha.formatAsMonthDay()
        val tarea = Task(id, titulo, descripcion, fecha, categoriaEnum)
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
            val st = if(task.isDone as Boolean) "Completado" else "En progreso"
            println("Tarea ${task.id} ")
        }
    }
    fun markTasks(tasklist: MutableList<Map<String, Any>>){
        if (tasklist.isEmpty()){
            println("Lista de tareas vacía")
            return
        }
        println("-------------Listando tareas incompletas-------------")
        for (task in tasklist) {
            if(task["estado"] == false){
                println("Tarea ${task["id"]} | Titulo: ${task["titulo"]} ")
            }
        }
        println("Seleccione una tarea (id) para marcarla como completada")
        val i = readLine()?.trim().orEmpty().toIntOrNull()
        val index = tasklist.indexOfFirst { it["id"] == i }
        if(index != -1){
            val update = tasklist[index].toMutableMap()
            update["estado"] = true
            tasklist[index] = update
            println("Tarea $i marcada como completada")
        }else
            println("No se encontro la tarea")


    }
    fun filterTasks(tasklist: List<Map<String, Any>>,completed: Boolean){
        if (tasklist.isEmpty()){
            println("Lista de tareas vacía")
            return
        }
        val st = if(completed) "Completado" else "En progreso"
        println("-------------Listando tareas ($st)--------------")
        for (task in tasklist) {
            if(task["estado"] == completed){
                println("Tarea ${task["id"]} | Titulo: ${task["titulo"]} | Estado: $st | Descripcion: ${task["descripcion"]} | Fecha: ${task["fecha"]} | Categoria: ${task["categoria"]}")
            }
        }
}
}