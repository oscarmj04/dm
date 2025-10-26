[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wTylcrtj)
# 📱 Mobile Task Manager – Assignment 7

This repository contains the implementation for **Assignment 7** of the Task Manager App project.

## 🎯 Assignment Goal

_Extend the Task Manager application to support full task creation and editing using two-way data binding and shared state between fragments._

_Integrate argument passing and result handling through Safe Args and SavedStateHandle for a complete bidirectional flow._

## ✅ Implemented Features

- **TaskListFragment** – displays all tasks from the shared `TaskRepository` using a `RecyclerView`.  
  Allows navigation to task details and provides a “+” menu option for adding new tasks.

- **TaskDetailFragment** – shows full information about a selected task and includes an “Edit” option to modify it.  
  The detail screen now updates automatically when returning from the edit form.

- **TaskFormFragment** – implements a fully functional form for creating new tasks.  

- **TaskEditFragment** – reuses the same logic and layout structure as the creation form, allowing users to modify existing tasks.  
  When the user saves, the detail screen refreshes immediately through `SavedStateHandle`.

- **TaskRepository** – in-memory singleton that stores all tasks and supports `addOrUpdate()` logic.  
  Task IDs are now generated sequentially based on the highest existing ID.

- **nav_graph.xml** – updated with Safe Args definitions and navigation actions for the new form and edit fragments.


## 🚧 Known Issues

- Tasks remain stored only in memory; data resets when the app restarts.

## 📝 Notes

- This version focuses on data binding, argument passing, and fragment state sharing.  
- The app now achieves a complete add/edit flow fully synchronized across fragments.  
- Future work will integrate persistent storage (Room) and Material UI components.

---

> This assignment is part of the Mobile Development course at [Universidade de Vigo].  
> See the course syllabus and lab instructions for more details.
