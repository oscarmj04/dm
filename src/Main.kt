fun main (){
    val tasklist = mutableListOf<Map<String, Any>>()
    var running = true
    while (running) {
        println("\n======Bienvenido al menú======")
        println("1. Añadir una tarea")
        println("2. Listar tareas")
        println("3. Marcar tareas completas")
        println("4. Filtrar tareas")
        println("5. Salir")
        println("Elija una opción")

        when(readLine()){
            "1" -> addTask(tasklist)
            "2" -> listTasks(tasklist)
            "3" -> markTasks(tasklist)
            "4" -> {
                print("¿Mostrar completadas? (s/n): ")
                val option = readLine()?.trim()?.lowercase()
                val completed = option == "s"
                filterTasks(tasklist, completed)
            }
            "5" -> running = false
            else -> println("Opción inválida")
        }



    }

    println("Finalizando programa ...")

}
fun createTask(id: Int, titulo: String, estado: Boolean, descripcion: String, fecha: String, categoria: String): Map<String, Any> {
    return mapOf(
        "id" to id,
        "titulo" to titulo,
        "estado" to estado,
        "descripcion" to descripcion,
        "fecha" to fecha,
        "categoria" to categoria
    )
}

fun addTask(tasklist:MutableList<Map<String, Any>>){
    println("Añadiendo tarea")
    val id = tasklist.size + 1
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
    val fecha = readLine()?.trim().orEmpty()

    println("Categoria")
    val categoria = readLine()?.trim().orEmpty()

    val tarea = createTask(id,titulo,estado,descripcion,fecha,categoria)
    tasklist.add(tarea)
    println("Tarea añadida satisfactoriamente")
}
fun listTasks(tasklist: List<Map<String, Any>>) {
    if (tasklist.isEmpty()){
        println("Lista de tareas vacía")
        return
    }
    println("-------------Listando tareas-------------")
    for (task in tasklist) {
        val st = if(task["estado"] as Boolean) "Completado" else "En progreso"
        println("Tarea ${task["id"]} | Titulo: ${task["titulo"]} | Estado: $st | Descripcion: ${task["descripcion"]} | Fecha: ${task["fecha"]} | Categoria: ${task["categoria"]}")
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
    println("-------------Listando tareas ($st)-------------")
    for (task in tasklist) {
        if(task["estado"] == completed){
            println("Tarea ${task["id"]} | Titulo: ${task["titulo"]} | Estado: $st | Descripcion: ${task["descripcion"]} | Fecha: ${task["fecha"]} | Categoria: ${task["categoria"]}")
        }
    }
}