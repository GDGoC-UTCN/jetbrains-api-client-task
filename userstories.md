# 📝 API Client Task: Contestant Guide & Requirements

Welcome to the GDGoC JetBrains API Client challenge!

This app follows a Unidirectional Data Flow (UDF) architecture.



**The UI is completely pre-built and locked.** Your task is purely to implement the Business and Logic layers. The UI will render your `State` and emit user `Actions`; your code must listen to these actions, execute the logic, and update the state.

## 📂 Project Structure: Where to Code
Focus your efforts strictly on the logic files.

* 🚫 `src/.../ui/` & `verification-framework/` -> **DO NOT EDIT.** These contain the locked UI and hidden test runners.
* 🛠️ `.../helpers/StorageHelper.kt` -> **EDIT THIS.** Implement local file saving/loading logic.
* 🛠️ `.../execution/CurlExecutor.kt` -> **EDIT THIS.** Implement the system `curl` process builder.
* 🛠️ `.../viewmodel/` -> **EDIT THESE.** Implement state management in `RequestsViewModel` and `ExecutionViewModel`.

---

## 🎯 Main Epics & Implementation Rules

### Epic 1: Workspace Management & Local Storage
**Goal:** Manage multiple API requests in a single session (like browser tabs) and persist them locally to a `data.json` file.

* **Local Storage (`StorageHelper.kt`):** Both `loadRequests` and `saveRequests` must run asynchronously on the `Dispatchers.IO` coroutine dispatcher. Data must be saved as pretty-printed JSON. If the file is missing or malformed, loading must safely return an empty list instead of crashing.
* **Initialization (`RequestsViewModel`):** On startup, load the saved requests. If none exist, automatically create a default request named "New request", save it, and set it as active.
* **Add / Rename (`RequestsViewModel`):** When creating or renaming, ignore blank names. Update the state, set the modified request as active, and immediately trigger a save to storage.
* **Delete (`RequestsViewModel`):** Deleting the active request must shift the active selection to the first available request in the remaining list, followed by a save.

### Epic 2: Request Builder State
**Goal:** Capture and hold the state of the HTTP request parameters as the user types.

* **Track Input Changes (`RequestsViewModel`):** Any modifications to the active request's URL, HTTP Method, Headers, or Body must immediately update the request in the `StateFlow` list and trigger a save via `StorageHelper`.

### Epic 3: Executing Requests via `curl`
**Goal:** Run network calls. *Crucial rule: Do not use standard HTTP libraries (like Ktor or Retrofit). You must execute the system's `curl` command.*

* **Pre-Execution Validation (`CurlExecutor.kt`):** * If the URL is blank, output `Error: URL is empty` and return an exit code of `-1`.
    * If Headers are provided, parse them to ensure they are valid JSON. If malformed, output `Error: Headers must be valid JSON...` and return `-1`.
* **Execute System Command (`CurlExecutor.kt`):** Construct a valid system process starting with `curl -s -S -X <METHOD> <URL>`. Append valid JSON headers using the `-H` flag. If the method is POST, PUT, or PATCH, write the body text directly to the process's `outputStream`.
* **Execution State (`ExecutionViewModel`):** * Prevent concurrent runs: if `isRunning` is true, ignore new "Send" clicks.
    * While running, update the status to `Sending...` and clear the previous output window.
    * Stream standard output and standard error from the `curl` process directly into the `outputContent` state so the user sees real-time results.

### Epic 4: Contest Mechanics

* **Check Progress:** Clicking this runs the local test suite. You must satisfy the exact criteria in Epics 1-3 to pass the tests and score `execution: 8/8`, `helpers: 7/7`, and `viewmodel: 18/18`.
* **Submit Solution:** Handled entirely by the pre-built UI. Just ensure your coroutine logic does not block the main thread, so this submission dialog can render and send your code smoothly!