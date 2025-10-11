package com.example.myapplication


import Task
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class MainActivity : AppCompatActivity() {
    private var nextId = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val dummyTasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // Referencia al RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Generar lista de tareas dummy
        generateDummyTasks()

        // Inicializar Adapter
        adapter = TaskAdapter(dummyTasks)
        recyclerView.adapter = this.adapter
    }
    private fun generateDummyTasks() {
        val sampleTitles = listOf(
            "Saltarse ABP",
            "Invitar a una caña a Carlos Castro",
            "Defraudar a Hacienda",
            "Plantar un Libro",
            "Escribir un árbol",
            "Apadrinar un Pingüino",
            "Aprender Kotlin",
            "Mirar horario trenes India",
            "Faltar al respeto a mi compañero",
            "Leer una película"
        )

        // Crear 10 tareas random
        for (title in sampleTitles) {
            dummyTasks.add(
                Task(
                    id = nextId++,
                    title = title,
                    description = "Descripción de prueba",
                    dueDate = LocalDate.now().plusDays((1..10).random().toLong()),
                    category = Category.OTRO, // o el valor por defecto que tengas
                    isDone = listOf(true, false).random()
                )
            )
        }

        }
    }







