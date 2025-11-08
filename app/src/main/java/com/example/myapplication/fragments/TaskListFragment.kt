package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.Task
import com.example.myapplication.TaskAdapter
import com.example.myapplication.model.TaskViewModel
import com.example.myapplication.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())

        // Observa tareas de Room y pinta la lista
        viewModel.tasks.observe(viewLifecycleOwner) { items ->
            val adapter = TaskAdapter(items) { task: Task ->
                val args = Bundle().apply { putInt("taskId", task.id) }
                findNavController().navigate(
                    R.id.action_taskListFragment_to_taskDetailFragment,
                    args
                )
            }
            binding.recyclerViewTasks.adapter = adapter
        }

        // Menú "+" solo en la lista
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_task -> {
                        findNavController().navigate(R.id.action_taskListFragment_to_taskFormFragment)
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
