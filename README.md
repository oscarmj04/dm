[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wTylcrtj)
# 📱 Mobile Task Manager – Assignment 3

This repository contains the implementation for **Assignment 3** of the Task Manager App project.

## 🎯 Assignment Goal

_Apply object-oriented design principles in Kotlin_

_Use a controller class to encapsulate business logic and data handling_

_Replace unstructured task representations with a formal Task class_

_Introduce enum-based classification (Category)_

_Improve code organization across multiple source files_

_Prepare the codebase for Android integration in Week 04_
## ✅ Implemented Features

- **TaskController.kt** – manages task operations and stores tasks.
- **Task.kt** – defines the Task data class.
- **Category.kt** – defines the Category enum.
- **Extensions.kt** – provides date formatting extensions for LocalDate.
- **Main.kt** – CLI interface to interact with the user.

## 🚧 Known Issues

- 1. **`dueDate` stored as `String`**
    - Currently, the due date is stored as a `String` in the `Task` class after formatting, which means the year information is lost.
    - As a result, date-based calculations (e.g., comparing deadlines or sorting by date) are not possible directly.


## 📝 Notes

- _dueDate saved as String in Task Class_
- _Specify any libraries used._

---

> This assignment is part of the Mobile Development course at [Universidade de Vigo].  
> See the course syllabus and lab instructions for more details.
