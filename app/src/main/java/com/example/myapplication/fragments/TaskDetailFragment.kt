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
import com.example.myapplication.databinding.FragmentTaskDetailBinding
import com.example.myapplication.model.TaskViewModel
import com.example.myapplication.MyApplication
import com.example.myapplication.model.TaskViewModelFactory

class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    // ViewModel con Factory
    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (requireActivity().application as MyApplication).repository
        )
    }

    private var localId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun render(task: com.example.myapplication.domain.Task) {
        binding.tvDetailTitle.text = task.title
        binding.tvDetailDescription.text = task.description
        binding.tvDetailDueDate.text = task.dueDate

        binding.tvDetailStatus.text =
            if (task.done) "✅ Completada" else "⏳ Pendiente"

        // category es ENUM → usamos name
        binding.tvDetailCategory.text = task.category.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener ID local enviado por navegación
        localId = arguments?.getInt("taskId", -1) ?: -1

        if (localId == -1) {
            Toast.makeText(requireContext(), "Tarea no encontrada", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // Obtener tarea desde ViewModel
        viewModel.tasks.observe(viewLifecycleOwner) { list ->
            val updated = list.find { it.localId == localId }
            if (updated != null) {
                render(updated)
            }
        }

        // Menú contextual
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_edit -> {
                        val args = Bundle().apply {
                            putInt("taskId", localId)
                        }
                        findNavController().navigate(
                            R.id.action_taskDetailFragment_to_taskEditFragment,
                            args
                        )
                        true
                    }

                    R.id.action_delete -> {
                        val t = viewModel.getTaskById(localId)
                        if (t != null) {
                            viewModel.deleteTask(t) {
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
