package com.example.myapplication.fragments

import Task
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.TaskRepository
import com.example.myapplication.databinding.FragmentTaskDetailBinding


class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private var task: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        task = arguments?.getSerializable("task") as? Task


        task?.let { t ->
            binding.tvDetailTitle.text = t.title
            binding.tvDetailDescription.text = t.description
            binding.tvDetailDueDate.text = t.dueDate.toString()
            binding.tvDetailCategory.text = t.category.toString()
            binding.tvDetailStatus.text = if (t.isDone) "✅ Completada" else "⏳ Pendiente"
        }

        // Observar cambios al volver del editor
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Int>("task_updated")
            ?.observe(viewLifecycleOwner) { updatedId ->
                val updated = TaskRepository.getById(updatedId)
                updated?.let { t ->
                    task = t
                    binding.tvDetailTitle.text = t.title
                    binding.tvDetailDescription.text = t.description
                    binding.tvDetailDueDate.text = t.dueDate.toString()
                    binding.tvDetailCategory.text = t.category.toString()
                    binding.tvDetailStatus.text = if (t.isDone) "✅ Completada" else "⏳ Pendiente"
                }
                findNavController().currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Int>("task_updated")
            }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        // Navegar a TaskEditFragment pasando el taskId
                        task?.let { t ->
                            val args = Bundle().apply { putInt("taskId", t.id) }
                            findNavController().navigate(
                                R.id.action_taskDetailFragment_to_taskEditFragment,
                                args
                            )
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
