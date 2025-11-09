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
import java.time.LocalDate

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

        // Task base para two-way
        binding.task = Task(
            id = 0, // Room autogenera
            title = "",
            description = "",
            dueDate = LocalDate.now(),
            category = Category.PERSONAL,
            done = false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Categorías (AutoComplete) threshold=0
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

        // Fecha (DatePicker)
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

        // Guardar → insertar en Room (vía ViewModel)
        binding.btnSave.setOnClickListener {
            val newTask = binding.task!!.copy(id = 0) // id lo pone Room
            viewModel.addTask(newTask)
            findNavController().popBackStack() // la lista se actualiza sola por LiveData
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
