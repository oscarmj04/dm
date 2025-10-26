package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.TaskAdapter
import com.example.myapplication.TaskRepository
import com.example.myapplication.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TaskRepository.ensureSeed()

        fun reload() {
            val items = TaskRepository.getAll()
            val adapter = TaskAdapter(items) { task ->
                // Navegar al detalle con el Task seleccionado
                val bundle = Bundle().apply { putSerializable("task", task) }
                findNavController().navigate(
                    R.id.action_taskListFragment_to_taskDetailFragment,
                    bundle
                )
            }
            binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewTasks.adapter = adapter
        }

        reload()

        // Escuchar UNA VEZ el resultado que devuelve el formulario y recargar la lista
        val handle = findNavController().currentBackStackEntry?.savedStateHandle
        handle?.getLiveData<Int>("task_result")?.observe(viewLifecycleOwner) {
            reload()
            handle.remove<Int>("task_result")
        }

        // Menú "+" solo en este fragment
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_task -> {
                        // Alta: navegar al formulario (sin args)
                        findNavController().navigate(
                            R.id.action_taskListFragment_to_taskFormFragment
                        )
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
