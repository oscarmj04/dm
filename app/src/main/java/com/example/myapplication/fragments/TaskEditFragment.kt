package com.example.myapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Category
import com.example.myapplication.Task
import com.example.myapplication.model.TaskViewModel
import com.example.myapplication.databinding.FragmentTaskEditBinding
import java.time.LocalDate

class TaskEditFragment : Fragment() {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = arguments?.getInt("taskId", -1) ?: -1
        val loaded: Task? = viewModel.getTaskById(taskId)
        if (loaded == null) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }
        binding.task = loaded   // two-way rellena los campos

        // Categoría
        val catNames = Category.values().map { it.name }
        val catAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, catNames)
        binding.actvCategory.apply {
            setAdapter(catAdapter)
            threshold = 0
            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showDropDown() }
            setText(binding.task?.category?.name ?: Category.PERSONAL.name, false)
            setOnItemClickListener { _, _, pos, _ ->
                binding.task?.category = Category.valueOf(catNames[pos])
            }
        }

        // Fecha
        binding.etDueDate.setText(binding.task?.dueDate?.toString().orEmpty())
        binding.etDueDate.setOnClickListener {
            val d = binding.task?.dueDate ?: LocalDate.now()
            DatePickerDialog(
                requireContext(),
                { _, y, m, day ->
                    val picked = LocalDate.of(y, m + 1, day)
                    binding.task?.dueDate = picked
                    binding.etDueDate.setText(picked.toString())
                },
                d.year, d.monthValue - 1, d.dayOfMonth
            ).show()
        }

        // Guardar cambios → ViewModel.updateTask
        binding.btnSave.setOnClickListener {
            val updated = binding.task!!.copy()
            viewModel.updateTask(updated)
            findNavController().popBackStack() // vuelve al detalle (o lista). Se refrescará por LiveData.
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
