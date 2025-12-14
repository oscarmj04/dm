# 📱 Mobile Task Manager – Assignment 12

This repository contains the implementation for **Assignment 12** of the Task Manager App project.

## 🎯 Assignment Goal

Refine the app architecture by implementing the **Repository pattern**, introducing a clear separation between the UI, domain, local persistence (Room) and remote data source (CRUD CRUD).

The goal is to abstract all data access logic so the ViewModel interacts with a clean, unified interface, without directly depending on Room or Retrofit.

## ✅ Implemented Features

- **Repository Pattern**
  - Introduced `TaskRepository` as the single source of truth.
  - The ViewModel only calls repository methods (`getTasks`, `addTask`, `updateTask`, `deleteTask`).
  - No direct access to DAO or Retrofit from the ViewModel.

- **Separated Data Models**
  - `TaskEntity` for Room persistence (localId + remoteId).
  - `TaskDto` for network communication with CRUD CRUD.
  - `Task` as the domain/UI model exposed to the ViewModel and UI.

- **Dual Identity Handling**
  - Stable `localId` for UI and RecyclerView.
  - Remote `_id` mapped to `remoteId`.
  - Repository updates `remoteId` after remote creation or sync.

- **Room-first Data Strategy**
  - UI always reads from Room.
  - Writes are stored locally first and then synchronized with the server.
  - Remote refresh is used only to update local data on initial load.

## 📝 Notes

- Architecture responsibilities are clearly separated (UI, ViewModel, Repository, DAO, API).
- The current structure lays the groundwork for offline-first or synchronized workflows.
- UI behavior remains unchanged while the internal architecture is fully refactored.
