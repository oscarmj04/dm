
fun main (){
    val controller = TaskController()
    var running = true
    while (running) {
        println("\n======Bienvenido al menú======")
        println("1. Añadir una tarea")
        println("2. Listar tareas")
        println("3. Marcar tareas completas")
        println("4. Filtrar tareas")
        println("5. Salir")
        println("Elija una opción")

        when(readlnOrNull()){
            "1" -> controller.addTask()
            "2" -> controller.listTasks()
            "3" -> controller.markTasks()
            "4" -> {
                print("¿Mostrar completadas? (s/n): ")
                val option = readlnOrNull()?.trim()?.lowercase()
                val completed = option == "s"
                controller.filterTasks(completed)
            }
            "5" -> running = false
            else -> println("Opción inválida")
        }
    }

    println("Finalizando programa ...")

}