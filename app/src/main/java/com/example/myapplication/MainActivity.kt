package com.example.myapplication

import Task
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private val tasklist = mutableListOf<Task>()
    private lateinit var tvTasks: TextView
    private var nextId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a vistas
        tvTasks = findViewById(R.id.tvTasks)
        val btnAdd = findViewById<Button>(R.id.btnAddTask)
        val btnMark = findViewById<Button>(R.id.btnMarkTask)
        val btnExit = findViewById<Button>(R.id.btnExit)

        // Lógica de botones

        btnAdd.setOnClickListener {
            val etTitulo = findViewById<EditText>(R.id.etTitulo)
            val titulo = etTitulo.text.toString().trim()

            if (titulo.isEmpty()) {
                Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                tvTasks.text = "Tarea añadida"
                addTask(titulo, false, " ", LocalDate.now(), Category.OTRO)
                refreshTasks()
            }
            etTitulo.text.clear()
        }

        btnMark.setOnClickListener {
            val etIDtoMark = findViewById<EditText>(R.id.etIDtoMark)
            var n : Int = -1
            try {
                n = Integer.parseInt(etIDtoMark.text.toString())
            }catch (e: Exception){}
            if(n == -1){
                Toast.makeText(this, "ID invalido", Toast.LENGTH_SHORT).show()
            }else{
                markTask(n)
                refreshTasks()
                etIDtoMark.text.clear()
            }
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    // ======================
    // Funciones de gestión
    // ======================


    fun addTask(titulo: String, estado: Boolean, descripcion: String, fecha: LocalDate, categoria: Category) {
        val id = nextId++
        val tarea = Task(id, titulo, descripcion, fecha, categoria, estado)
        tasklist.add(tarea)
    }

    fun markTask(id: Int) {
        val t = tasklist.find {it.id == id}
        if(t != null){
            t.isDone = true
        }else{
            Toast.makeText(this, "La tarea $id no se encontró", Toast.LENGTH_SHORT).show()
        }

    }
    private fun refreshTasks() {
        if (tasklist.isEmpty()) {
            tvTasks.text = "No hay tareas"
        } else {
            val builder = StringBuilder()
            for (task in tasklist) {
                val estado = if (task.isDone) "Completado" else "En progreso"
                builder.append("(${task.id}) ${task.title} - $estado\n")
            }
            tvTasks.text = builder.toString()
        }
    }


}




