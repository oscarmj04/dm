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

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

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

        // Adapter con callback de click en tareas
        adapter = TaskListAdapter { task ->
            val args = Bundle().apply { putInt("taskId", task.id) }
            findNavController().navigate(
                R.id.action_taskListFragment_to_taskDetailFragment,
                args
            )
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter

        // Observa la lista transformada (headers + tasks) desde el ViewModel
        viewModel.taskListItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        // Gestos: swipe izquierda/derecha y drag & drop para tareas (no headers)
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return 0

                val item = adapter.currentList.getOrNull(position) ?: return 0

                return when (item) {
                    is TaskListItem.Header -> 0 // headers: ni drag ni swipe
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
                if (fromPos == RecyclerView.NO_POSITION || toPos == RecyclerView.NO_POSITION) {
                    return false
                }

                val current = adapter.currentList.toMutableList()
                val fromItem = current.getOrNull(fromPos)
                val toItem = current.getOrNull(toPos)

                // Solo permitimos mover tareas dentro de la misma categoría
                if (fromItem !is TaskListItem.TaskItem || toItem !is TaskListItem.TaskItem) {
                    return false
                }
                if (fromItem.task.category != toItem.task.category) {
                    return false
                }

                // Reordenar solo a nivel de UI (no persistimos el orden)
                current.removeAt(fromPos)
                val insertIndex = if (fromPos < toPos) toPos - 1 else toPos
                current.add(insertIndex, fromItem)

                adapter.submitList(current)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return

                val item = adapter.currentList.getOrNull(position)
                if (item !is TaskListItem.TaskItem) {
                    // Por seguridad, para headers reponemos la vista
                    adapter.notifyItemChanged(position)
                    return
                }

                val task = item.task
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // Swipe izquierda: eliminar tarea
                        viewModel.deleteTask(task.id)
                    }
                    ItemTouchHelper.RIGHT -> {
                        // Swipe derecha: marcar como completada
                        if (!task.done) {
                            val updated = task.copy(done = true)
                            viewModel.updateTask(updated)
                        } else {
                            // Segunda vez → NO hacemos nada
                            adapter.notifyItemChanged(viewHolder.adapterPosition)
                            return
                        }}
                }

                // Room notificará cambios y taskListItems se actualizará sola
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
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val paint = android.graphics.Paint()

                    if (dX > 0) {
                        // Swipe derecha → completar (verde)
                        paint.color = androidx.core.content.ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                        c.drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.left.toFloat() + dX,
                            itemView.bottom.toFloat(),
                            paint
                        )
                    } else if (dX < 0) {
                        // Swipe izquierda → borrar (rojo)
                        paint.color = androidx.core.content.ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                        c.drawRect(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            paint
                        )
                    }
                }

                // Deja que el ItemTouchHelper mueva la vista como siempre
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewTasks)

        // Menú "+" solo en la lista
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
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
