package com.example.myapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Category
import com.example.myapplication.Task
import com.example.myapplication.model.TaskViewModel
import com.example.myapplication.databinding.FragmentTaskFormBinding
import java.util.*

class TaskFormFragment : Fragment() {

    private var _binding: FragmentTaskFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskFormBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // Tarea base para DataBinding (compatible con CrudCrud)
        binding.task = Task(
            id = null,                         // ID lo genera CrudCrud
            title = "",
            description = "",
            dueDate = getTodayString(),        // String, no LocalDate
            category = Category.PERSONAL,
            done = false
        )

        return binding.root
    }

    private fun getTodayString(): String {
        val calendar = Calendar.getInstance()
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH) + 1
        val d = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%04d-%02d-%02d", y, m, d)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --------------------------
        //   Categorías AutoComplete
        // --------------------------
        val catNames = Category.values().map { it.name }
        val catAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, catNames)

        binding.actvCategory.apply {
            setAdapter(catAdapter)
            threshold = 0

            setText(binding.task?.category?.name ?: Category.PERSONAL.name, false)

            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showDropDown() }
            setOnItemClickListener { _, _, pos, _ ->
                binding.task?.category = Category.valueOf(catNames[pos])
            }
        }

        // --------------------------
        //        DatePicker
        // --------------------------
        binding.etDueDate.setText(binding.task?.dueDate)

        binding.etDueDate.setOnClickListener {
            val dateStr = binding.task?.dueDate ?: getTodayString()
            val parts = dateStr.split("-")

            val year = parts[0].toInt()
            val month = parts[1].toInt() - 1
            val day = parts[2].toInt()

            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    val picked = String.format("%04d-%02d-%02d", y, m + 1, d)
                    binding.task?.dueDate = picked
                    binding.etDueDate.setText(picked)
                },
                year, month, day
            ).show()
        }

        // --------------------------
        //       Guardar → POST
        // --------------------------
        binding.btnSave.setOnClickListener {
            val newTask = binding.task ?: return@setOnClickListener

            viewModel.addTask(newTask) {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
