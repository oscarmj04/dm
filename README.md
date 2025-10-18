[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wTylcrtj)
# 📱 Mobile Task Manager – Assignment 6

This repository contains the implementation for **Assignment 6** of the Task Manager App project.

## 🎯 Assignment Goal

_Build a multi-screen Android UI using Kotlin, Fragments, and the Navigation Component._

_Implement navigation between the task list, detail, and task creation screens, and manage dynamic menu visibility._

## ✅ Implemented Features

- **TaskListFragment** – displays a list of dummy (static) `Task` objects inside a `RecyclerView`.  
  Allows navigation to a detailed task view when an item is tapped.
- **TaskDetailFragment** – shows full information about a selected task.
- **TaskFormFragment** – currently a placeholder screen showing an “Under Construction” message (to be completed in future assignments).
- **MainActivity.kt** – hosts the `NavHostFragment` and connects navigation actions to the ActionBar.
- **menu_main.xml** – defines a dynamic menu option.
- **nav_graph.xml** – defines navigation actions between fragments.
- **TaskAdapter.kt** – improved to handle click events on each task item.


## 🚧 Known Issues

- Task creation and editing are not implemented yet (the Task Form screen is only a placeholder).
- Task data is still static; no persistence or data source has been added yet.

## 📝 Notes

- This version focuses on navigation and UI structure using Fragments and MenuProvider.
- Logic is modularized across fragments, following Android’s recommended architecture.
- Future updates will include actual task creation and storage features.

---

> This assignment is part of the Mobile Development course at [Universidade de Vigo].  
> See the course syllabus and lab instructions for more details.
