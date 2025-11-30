package com.example.myapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Category
import com.example.myapplication.Task
import com.example.myapplication.model.TaskViewModel
import com.example.myapplication.databinding.FragmentTaskEditBinding
import java.util.*

class TaskEditFragment : Fragment() {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

    private var taskId: String? = null  //ahora es String, no Int

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

        // -------------------------------
        //   Leer ID remoto desde argumentos
        // -------------------------------
        taskId = arguments?.getString("taskId")

        if (taskId == null) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // -------------------------------
        //   Obtener la tarea del ViewModel
        // -------------------------------
        val loaded: Task? = viewModel.getTaskById(taskId!!)
        if (loaded == null) {
            Toast.makeText(requireContext(), "No se pudo cargar la tarea", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        binding.task = loaded

        // Inicializar campos visibles
        binding.etDueDate.setText(loaded.dueDate)
        binding.actvCategory.setText(loaded.category.name, false)

        // -------------------------------
        //   Categorías (AutoComplete)
        // -------------------------------
        val catNames = Category.values().map { it.name }
        val catAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, catNames)

        binding.actvCategory.apply {
            setAdapter(catAdapter)
            threshold = 0
            setOnClickListener { showDropDown() }
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showDropDown() }
            setOnItemClickListener { _, _, pos, _ ->
                binding.task?.category = Category.valueOf(catNames[pos])
            }
        }

        // -------------------------------
        //           DatePicker
        // -------------------------------
        binding.etDueDate.setOnClickListener {

            // Convertir fecha actual a calendar (si existe)
            val calendar = Calendar.getInstance()
            val parts = loaded.dueDate.split("-")

            if (parts.size == 3) {
                calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
            }

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val dateStr = String.format("%04d-%02d-%02d", year, month + 1, day)
                    binding.task?.dueDate = dateStr
                    binding.etDueDate.setText(dateStr)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // -------------------------------
        //       Guardar cambios → PUT
        // -------------------------------
        binding.btnSave.setOnClickListener {
            val updated = binding.task ?: return@setOnClickListener

            viewModel.updateTask(updated) {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
