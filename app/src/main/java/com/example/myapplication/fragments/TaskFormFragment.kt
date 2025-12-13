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
import com.example.myapplication.databinding.FragmentTaskFormBinding
import com.example.myapplication.domain.Task
import com.example.myapplication.model.TaskViewModel
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

        binding.task = Task(
            localId = 0,
            remoteId = null,
            title = "",
            description = "",
            dueDate = getTodayString(),
            category = Category.PERSONAL,
            done = false
        )

        return binding.root
    }

    private fun getTodayString(): String {
        val c = Calendar.getInstance()
        return "%04d-%02d-%02d".format(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val catNames = Category.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, catNames)

        binding.actvCategory.apply {
            setAdapter(adapter)
            threshold = 0          // 👈 AÑADIDO PARA ABRIR EL MENÚ SIN ESCRIBIR
            setText(binding.task!!.category.name, false)

            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showDropDown() }

            setOnItemClickListener { _, _, pos, _ ->
                binding.task = binding.task!!.copy(category = Category.valueOf(catNames[pos]))
            }
        }

        // -----------------------------
        //        DATE PICKER
        // -----------------------------
        binding.etDueDate.setText(binding.task!!.dueDate)
        binding.etDueDate.setOnClickListener {
            val t = binding.task!!
            val parts = t.dueDate.split("-")
            val y = parts[0].toInt()
            val m = parts[1].toInt() - 1
            val d = parts[2].toInt()

            DatePickerDialog(requireContext(), { _, yy, mm, dd ->
                val newDate = "%04d-%02d-%02d".format(yy, mm + 1, dd)
                binding.task = t.copy(dueDate = newDate)
                binding.etDueDate.setText(newDate)
            }, y, m, d).show()
        }

        // -----------------------------
        //       SAVE
        // -----------------------------
        binding.btnSave.setOnClickListener {
            val t = binding.task!!.copy(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString()
            )

            viewModel.addTask(t) {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
