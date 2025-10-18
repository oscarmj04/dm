package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import Task
import com.example.myapplication.R
import androidx.navigation.fragment.findNavController
import com.example.myapplication.TaskAdapter
import com.example.myapplication.databinding.FragmentTaskListBinding
import com.example.myapplication.Category
import java.time.LocalDate

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun onTaskClicked(task: Task) {
        val bundle = Bundle().apply {
            putSerializable("task", task)
        }
        findNavController().navigate(R.id.action_taskListFragment_to_taskDetailFragment, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyTasks = listOf(
            Task(1, "Aprender Kotlin", "Repasar fundamentos", LocalDate.now(), Category.PERSONAL, false),
            Task(2, "Sacar al perro", "Dar una vuelta al parque", LocalDate.now().plusDays(1), Category.URGENTE, true),
            Task(3, "Leer libro", "Terminar capítulo 4", LocalDate.now().plusDays(2), Category.PERSONAL, false)
        )

        val adapter = TaskAdapter(dummyTasks) { task ->
            // Aquí se lanza el detalle
            val bundle = Bundle().apply { putSerializable("task", task) }
            findNavController().navigate(R.id.action_taskListFragment_to_taskDetailFragment, bundle)
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
