package com.example.myapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentTaskFormBinding
import com.example.myapplication.Task
import com.example.myapplication.Category
import com.example.myapplication.TaskRepository
import java.time.LocalDate
import android.widget.ArrayAdapter

class TaskFormFragment : Fragment() {

    private var _binding: FragmentTaskFormBinding? = null
    private val binding get() = _binding!!

    // Solo para captura en UI (no precargamos nada)
    private var pickedDate: LocalDate = LocalDate.now()
    private var pickedCategory: Category = Category.OTRO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Categorías con threshold=0 y desplegable al tocar
        val catNames = Category.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, catNames)
        binding.actvCategory.setAdapter(adapter)
        binding.actvCategory.threshold = 0
        binding.actvCategory.setOnClickListener { binding.actvCategory.showDropDown() }
        binding.actvCategory.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) binding.actvCategory.showDropDown() }
        binding.actvCategory.setOnItemClickListener { _, _, pos, _ ->
            pickedCategory = Category.valueOf(catNames[pos])
        }

        // Fecha con DatePicker
        binding.etDueDate.setOnClickListener {
            val d = pickedDate
            DatePickerDialog(
                requireContext(),
                { _, y, m, day ->
                    pickedDate = LocalDate.of(y, m + 1, day)
                    binding.etDueDate.setText(pickedDate.toString())
                },
                d.year, d.monthValue - 1, d.dayOfMonth
            ).show()
        }
        // Valor inicial visible
        binding.etDueDate.setText(pickedDate.toString())
        binding.actvCategory.setText(pickedCategory.name, false)

        // Guardar → crear Task nueva y añadir al repo
        binding.btnSave.setOnClickListener {
            val newTask = Task(
                id = 0,
                title = binding.etTitle.text?.toString().orEmpty(),
                description = binding.etDescription.text?.toString().orEmpty(),
                dueDate = pickedDate,
                category = pickedCategory,
                isDone = binding.cbDone.isChecked
            )
            val saved = TaskRepository.add(newTask)

            // Notificar y volver
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("task_result", saved.id)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
