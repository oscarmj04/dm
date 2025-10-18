package com.example.myapplication.fragments

import Task
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : Fragment() {
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)

        val task = arguments?.getSerializable("task") as? Task

        task?.let {
            binding.tvDetailTitle.text = it.title
            binding.tvDetailDescription.text = it.description
            binding.tvDetailDueDate.text = it.dueDate.toString()
            binding.tvDetailStatus.text = if (it.isDone) "✅" else "⏳"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
