# 📱 Mobile Task Manager – Assignment 10

This repository contains the implementation for **Assignment 10** of the Task Manager App project.

## 🎯 Assignment Goal

_Enhance the task list UI by introducing **category grouping**, **heterogeneous RecyclerView items**, and **gesture-based interactions** (swipe + drag), while maintaining MVVM structure with Room as the persistence layer._

## ✅ Implemented Features

- **TaskListItem.kt** – sealed class defining heterogeneous RecyclerView items (`Header` and `TaskItem`).
- **TaskViewModel.kt** – now exposes a transformed list  
  `taskListItems: LiveData<List<TaskListItem>>`  
  grouping tasks by category and ordering them by due date.
- **TaskListAdapter.kt** – migrated to `ListAdapter` with `DiffUtil`, supporting two distinct view types with separate ViewHolders.
- **item_header.xml** – new layout for category header sections, visually differentiating categories within the list.
- **TaskListFragment** – updated to observe `taskListItems` and to configure gesture-based interactions:
  - **Swipe left →** delete task  
  - **Swipe right →** mark task as completed  
  - **Drag & drop →** reorder tasks within the same category  
- **Gesture handling** – implemented using `ItemTouchHelper`, ensuring header elements cannot be swiped or dragged.

## 🚧 Known Issues

- Drag & drop order is not persisted (UI-only reordering).

## 📝 Notes

- Tasks are now visually grouped by category, improving readability and structure.
- The sealed class + ListAdapter architecture allows scalable, maintainable RecyclerView logic.
- All list transformations are handled inside the ViewModel, ensuring MVVM separation.
- Room continues to ensure persistent task storage, and all UI updates remain reactive through LiveData.

---

> This assignment is part of the Mobile Development course at **Universidade de Vigo**.  
> See the course syllabus and lab instructions for more details.
