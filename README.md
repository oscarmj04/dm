[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wTylcrtj)
# 📱 Mobile Task Manager – Assignment 8

This repository contains the implementation for **Assignment 8** of the Task Manager App project.

## 🎯 Assignment Goal

_Implement a shared ViewModel architecture (MVVM) to centralize task management and enable reactive UI updates._

_Use LiveData and data binding to synchronize state automatically between fragments (list, detail, and form)._

## ✅ Implemented Features

- **TaskViewModel.kt** – acts as the single source of truth for all task data using `LiveData<List<Task>>`.  
  Handles adding, updating, and deleting tasks reactively.
- **TaskListFragment** – observes `viewModel.tasks` and updates the RecyclerView automatically when tasks are added or modified.
- **TaskFormFragment** – uses **two-way data binding** (`@={...}`) to create new tasks, updating the ViewModel directly through `addTask()`.
- **TaskEditFragment** – edits existing tasks loaded from the shared ViewModel using `updateTask()`.  
  All fields (title, description, due date, category, completion state) remain synchronized via two-way binding.
- **TaskDetailFragment** – retrieves the selected task from the ViewModel using its ID and displays all its details, including **category**.  
  Provides an option to edit or delete the task, updating the shared state instantly.
- **LiveData observation** – ensures any change in the data model immediately reflects across all active fragments.

## 🚧 Known Issues

- Tasks are still stored in-memory; persistence with Room will be introduced in future assignments.
- UI components use basic Android widgets (Material components not yet applied).

## 📝 Notes

- The app now follows the **MVVM pattern**, decoupling UI logic from state management.
- The `TaskViewModel` replaces the old in-memory repository and manages all CRUD operations.
- The property `done` (proxy for `isDone`) enables proper two-way binding for completion state.
- LiveData propagation guarantees that all fragments stay synchronized without manual refreshes.

---

> This assignment is part of the Mobile Development course at [Universidade de Vigo].  
> See the course syllabus and lab instructions for more details.
