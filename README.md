[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wTylcrtj)
# 📱 Mobile Task Manager – Assignment 9

This repository contains the implementation for **Assignment 9** of the Task Manager App project.

## 🎯 Assignment Goal

_Add local persistence to the Task Manager app using **Room** and **Kotlin coroutines**, ensuring all task data is stored permanently on the device._

_Refactor the architecture so the ViewModel delegates all state management to the Room database layer._

## ✅ Implemented Features

- **Task.kt** – now annotated with `@Entity`, defining the table structure for Room.  
- **Converters.kt** – provides custom type converters for `LocalDate` and `Category` to allow Room to persist them.
- **TaskDao.kt** – defines all database operations (`insert`, `update`, `delete`, and queries) using suspend functions and LiveData.
- **AppDatabase.kt** – implements the Room singleton database (`tasks.db`) with type converters enabled.
- **TaskViewModel.kt** – refactored to interact directly with Room via `TaskDao`.  
- **TaskListFragment** – observes `viewModel.tasks` (LiveData from Room) and updates the RecyclerView automatically when data changes.
- **TaskDetailFragment** – observes a single task from Room, displaying full details (including **category**) and supporting **edit** and **delete** actions.

## 🚧 Known Issues

- The app currently stores data locally only.

## 📝 Notes

- Room now provides full **local persistence**, ensuring tasks remain saved after closing the app.
- The `TaskViewModel` acts as a bridge between the UI and the database, performing all I/O work off the main thread using coroutines.
- The architecture fully follows the **MVVM pattern**, combining Room, LiveData, and data binding for a reactive and maintainable design.
- LiveData from Room guarantees automatic UI updates without manual refreshes.

---

> This assignment is part of the Mobile Development course at [Universidade de Vigo].  
> See the course syllabus and lab instructions for more details.
