package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.TaskListAdapter
import com.example.myapplication.TaskListItem
import com.example.myapplication.databinding.FragmentTaskListBinding
import com.example.myapplication.model.TaskViewModel
import com.example.myapplication.MyApplication
import com.example.myapplication.model.TaskViewModelFactory

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    // ViewModel con factory
    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (requireActivity().application as MyApplication).repository
        )
    }

    private lateinit var adapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter con callback de click → enviamos localId (INT)
        adapter = TaskListAdapter { task ->
            val args = Bundle().apply {
                putInt("taskId", task.localId)
            }
            findNavController().navigate(
                R.id.action_taskListFragment_to_taskDetailFragment,
                args
            )
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter

        // Observar lista
        viewModel.taskListItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        // Swipe + Drag & Drop
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val pos = viewHolder.bindingAdapterPosition
                if (pos == RecyclerView.NO_POSITION) return 0

                val item = adapter.currentList.getOrNull(pos) ?: return 0

                return when (item) {
                    is TaskListItem.Header -> 0
                    is TaskListItem.TaskItem -> {
                        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        makeMovementFlags(dragFlags, swipeFlags)
                    }
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.bindingAdapterPosition
                val toPos = target.bindingAdapterPosition

                val list = adapter.currentList.toMutableList()
                if (fromPos !in list.indices || toPos !in list.indices) return false

                val from = list[fromPos]
                val to = list[toPos]

                if (from !is TaskListItem.TaskItem || to !is TaskListItem.TaskItem) return false

                if (from.task.category != to.task.category) return false

                list.removeAt(fromPos)
                val insertIndex = if (fromPos < toPos) toPos - 1 else toPos
                list.add(insertIndex, from)

                adapter.submitList(list)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, dir: Int) {
                val pos = viewHolder.bindingAdapterPosition
                val item = adapter.currentList.getOrNull(pos)

                if (item !is TaskListItem.TaskItem) {
                    adapter.notifyItemChanged(pos)
                    return
                }

                val task = item.task

                when (dir) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteTask(task) {
                            // Nada extra, LiveData recarga
                        }
                    }

                    ItemTouchHelper.RIGHT -> {
                        if (!task.done) {
                            viewModel.updateTask(task.copy(done = true))
                        } else {
                            adapter.notifyItemChanged(pos)
                        }
                    }
                }
            }

            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val paint = android.graphics.Paint()
                val itemView = viewHolder.itemView

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX > 0) {
                        paint.color = requireContext().getColor(R.color.green)
                        c.drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.left + dX,
                            itemView.bottom.toFloat(),
                            paint
                        )
                    } else if (dX < 0) {
                        paint.color = requireContext().getColor(R.color.red)
                        c.drawRect(
                            itemView.right + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            paint
                        )
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewTasks)

        // Menú "+"
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_add_task -> {
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
