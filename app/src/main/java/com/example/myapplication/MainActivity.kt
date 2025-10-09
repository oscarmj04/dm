package com.example.myapplication


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val dummyTasks = mutableListOf<String>()

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
            "Comprar leche",
            "Hacer ejercicio",
            "Enviar correo",
            "Leer un libro",
            "Preparar presentación",
            "Llamar a mamá",
            "Aprender Kotlin",
            "Organizar escritorio",
            "Pagar facturas",
            "Ver una película"
        )

        // Crear 10 tareas random
        repeat(10) {
            val randomTitle = sampleTitles.random() + " #${it + 1}"
            dummyTasks.add(randomTitle)
        }
    }

}




