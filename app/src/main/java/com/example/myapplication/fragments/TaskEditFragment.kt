package com.example.myapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentTaskEditBinding
import Task
import com.example.myapplication.Category
import com.example.myapplication.TaskRepository
import java.time.LocalDate
import android.widget.ArrayAdapter

class TaskEditFragment : Fragment() {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    private var current: Task? = null
    private var pickedDate: LocalDate = LocalDate.now()
    private var pickedCategory: Category = Category.PERSONAL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = arguments?.getInt("taskId", -1) ?: -1
        current = TaskRepository.getById(taskId)

        current?.let { t ->
            pickedDate = t.dueDate
            pickedCategory = t.category

            binding.etTitle.setText(t.title)
            binding.etDescription.setText(t.description)
            binding.etDueDate.setText(t.dueDate.toString())
            binding.cbDone.isChecked = t.isDone
        }

        val catNames = Category.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, catNames)
        binding.actvCategory.setAdapter(adapter)
        binding.actvCategory.threshold = 0
        binding.actvCategory.setOnClickListener { binding.actvCategory.showDropDown() }
        binding.actvCategory.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) binding.actvCategory.showDropDown() }
        binding.actvCategory.setText(pickedCategory.name, false)
        binding.actvCategory.setOnItemClickListener { _, _, pos, _ ->
            pickedCategory = Category.valueOf(catNames[pos])
        }

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

        binding.btnSave.setOnClickListener {
            val base = current ?: return@setOnClickListener
            val updated = base.copy(
                title = binding.etTitle.text?.toString().orEmpty(),
                description = binding.etDescription.text?.toString().orEmpty(),
                dueDate = pickedDate,
                category = pickedCategory,
                isDone = binding.cbDone.isChecked
            )
            val saved = TaskRepository.addOrUpdate(updated)

            // Notificar y volver
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("task_updated", saved.id)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
