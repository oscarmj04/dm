[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wTylcrtj)
# 📱 Mobile Task Manager – Assignment 5

This repository contains the implementation for **Assignment 5** of the Task Manager App project.

## 🎯 Assignment Goal

_Build a refined Android UI layout using Kotlin and modern Android components._

_Replace the static TextView-based task list with a RecyclerView backed by a custom Adapter and ViewHolder._

_Use ConstraintLayout to create a clean and scalable layout structure._

_Display a list of dummy (static) Task objects using data binding within the RecyclerView._

## ✅ Implemented Features

- **Task.kt** – defines the `Task` data class reused from previous assignments.
- **Category.kt** – defines the `Category` enum used for task classification.
- **TaskAdapter.kt** – implements a custom `RecyclerView.Adapter` and `ViewHolder` to display task items.
- **item_task.xml** – defines the layout for each individual task item (title, due date, and completion status).
- **MainActivity.kt** – initializes a RecyclerView with a list of dummy `Task` objects and binds them to the adapter.
- **activity_main.xml** – redesigned using `ConstraintLayout`, displaying a title and a `RecyclerView` for tasks.

## 🚧 Known Issues

- Task creation and editing are not implemented yet (UI only).


## 📝 Notes

- This version focuses purely on UI rendering and view binding.
- All logic is handled directly in the Activity for simplicity.

---

> This assignment is part of the Mobile Development course at [Universidade de Vigo].  
> See the course syllabus and lab instructions for more details.
