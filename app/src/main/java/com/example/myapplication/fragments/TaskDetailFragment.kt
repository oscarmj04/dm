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

    private var taskId: Int = -1

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
        binding.tvDetailDueDate.text = task.dueDate.toString()
        binding.tvDetailCategory.text = task.category.toString()
        binding.tvDetailStatus.text = if (task.done) "✅ Completada" else "⏳ Pendiente"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar taskId (preferido). Si no, derivarlo de un Task serializado previo.
        taskId = arguments?.getInt("taskId", -1) ?: -1
        if (taskId == -1) {
            val t = arguments?.getSerializable("task") as? Task
            taskId = t?.id ?: -1
        }

        if (taskId == -1) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // Observa la lista y re-renderiza cuando cambie esta tarea
        viewModel.tasks.observe(viewLifecycleOwner) {
            viewModel.getTaskById(taskId)?.let(::render)
        }

        // Menú contextual: Editar + (opcional) Borrar
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu) // debe tener action_edit y opcional action_delete
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        val args = Bundle().apply { putInt("taskId", taskId) }
                        findNavController().navigate(R.id.action_taskDetailFragment_to_taskEditFragment, args)
                        true
                    }
                    R.id.action_delete -> {
                        viewModel.deleteTask(taskId)
                        findNavController().popBackStack() // volver a la lista
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
