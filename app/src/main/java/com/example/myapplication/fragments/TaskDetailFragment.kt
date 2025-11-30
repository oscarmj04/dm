package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.Task
import com.example.myapplication.model.TaskViewModel
import com.example.myapplication.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

    private var taskId: String? = null   //ahora es String, no Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun render(task: Task) {
        binding.tvDetailTitle.text = task.title
        binding.tvDetailDescription.text = task.description
        binding.tvDetailDueDate.text = task.dueDate
        binding.tvDetailStatus.text = if (task.done) "✅ Completada" else "⏳ Pendiente"
        binding.tvDetailCategory.text = task.category.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --------------------------------------------
        //   Obtener el ID (String) desde los argumentos
        // --------------------------------------------
        taskId = arguments?.getString("taskId")

        if (taskId == null) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // --------------------------------------------
        //   Cargar la tarea desde el ViewModel
        // --------------------------------------------
        val task = viewModel.getTaskById(taskId!!)
        if (task != null) {
            render(task)
        } else {
            Toast.makeText(requireContext(), "No se encontró la tarea", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // --------------------------------------------
        //  Menú contextual: Editar / Eliminar
        // --------------------------------------------
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    // --------------------
                    //   EDITAR
                    // --------------------
                    R.id.action_edit -> {
                        val args = Bundle().apply {
                            putString("taskId", taskId)
                        }
                        findNavController().navigate(
                            R.id.action_taskDetailFragment_to_taskEditFragment,
                            args
                        )
                        true
                    }

                    // --------------------
                    //   ELIMINAR
                    // --------------------
                    R.id.action_delete -> {
                        taskId?.let { id ->
                            viewModel.deleteTask(id) {
                                findNavController().popBackStack()
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
