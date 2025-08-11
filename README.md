
# City Finder – Klivvr Android Assignment

## 📋 Overview



| Loading     | Home Screen | Search        | Pull to Refresh |
|-------------|-------------|---------------|-----------------|
| <img width="372" height="802" alt="loading" src="https://github.com/user-attachments/assets/f2ca3212-ee18-4aed-bd12-b6d9ae52316c" /> | <img width="428" height="826" alt="home-screen" src="https://github.com/user-attachments/assets/fdebef1e-d1f4-4d8d-b33e-02ad84ca6891" /> | <img width="397" height="828" alt="search" src="https://github.com/user-attachments/assets/b134b727-21ff-4b7d-9524-2eace1c4941b" /> | <img width="386" height="815" alt="pull to refresh" src="https://github.com/user-attachments/assets/f949f602-3b8f-448f-a88d-f9d10e64dc83" /> |


City Finder is a Kotlin + Jetpack Compose Android app implementing the Klivvr assignment requirements:

- Loads a large dataset (~200k cities) from a local JSON asset.
- Uses an **in‑memory Trie** for **better‑than‑linear** prefix search performance.
- Displays results in a scrollable, grouped list with **sticky headers** and a **left sidebar group letter** with a vertical connector line.
- Allows **case‑insensitive** real‑time filtering as the user types.
- Shows each city with:
  - Country code of its country in a circular shape with gray background
  - City + country code as the title
  - Latitude/Longitude as the subtitle
- Tapping a city opens its location in Google Maps.
- Supports loading/empty/error states, and **screen rotation**.

---

## 🏗 Architecture

The app follows **Clean Architecture** principles with three main layers, fully decoupled for testability and maintainability.

### 1. **Domain Layer** (`domain/`)
- **Responsibilities:**
  - Core business logic
  - **Domain models** (pure Kotlin data classes)
  - **Repository abstractions** (interfaces)
  - **Use cases** (single‑purpose business rules)
- **Example:**
  - `CityRepository` interface
  - `SearchCitiesByPrefixUseCase`, `GetAllCitiesUseCase`

### 2. **Data Layer** (`data/`)
- **Responsibilities:**
  - Fetching and storing data
  - Holding all optimizations for retrieval (e.g., Trie search structure)
  - Mapping **raw data models** to **domain models**
- **Main Components:**
  - **Models** matching JSON (`CityResponse`, `CoordResponse`)
  - **Mapper** to map models to domain (`CityMapper`)
  - **Local Data Source** for loading assets (`CityLocalDataSource`)
  - **Trie Implementation** (`CityTrie`, `TrieNode`)
  - **Repository Implementation** (`CityRepositoryImpl`)  
    - Loads cities on first use  
    - Inserts into Trie for fast prefix search  
    - Sorts results by name then country code  
    - Uses `Dispatchers.Default` for CPU‑bound work

### 3. **Presentation Layer** (`presentation/` + `ui/`)
- **Responsibilities:**
  - Orchestrates UI state
  - Binds domain use cases to the Compose UI
  - Handles user events (search input, refresh, click)
- **Components:**
  - **ViewModels** (Hilt‑injected, lifecycle‑aware)
  - **Screen state data classes** (single source of truth)
  - **Jetpack Compose UI** (Grouped LazyColumn, sticky headers, search bar, animations)
  - **Error/Empty/Loading Composables**

---

## 🔌 Dependency Injection

- Implemented using **Dagger-Hilt** with **KSP** for annotation processing.


## 🗂 Data Pre‑Processing & Performance Optimization

Before bundling the `cities_sorted.json` file with the app, the raw city list was **pre‑processed offline**:

1. **Sorting:**  
   The entire dataset (≈200k entries) was sorted **alphabetically by city name** (and by country code as a secondary sort key).  
   This sorting is done **once** during preprocessing, not in the app.

2. **Resulting Asset:**  
   The processed file is stored in the `assets/` folder as `cities_sorted.json`.  
   It is loaded at runtime in already sorted order.

### 💡 Why This Matters for Performance

- **No runtime sorting overhead:**  
  Since the cities are already sorted, we completely avoid expensive `O(N log N)` sorting in the app during:
  - Initial dataset load
  - Applying new search queries
  - Refresh operations

- **Search efficiency:**  
  The in‑memory Trie returns results in near‑unsorted insertion order; since insertion uses pre‑sorted data, retrieval lists are already in the correct alphabetical order, removing extra sorting logic in the search path.

### 📈 Impact

This preprocessing step reduced runtime CPU work and improved:
- **App cold start** (dataset is ready for display and search faster)
- **Search response time** (queries return sorted results immediately)
- **User experience** (no UI hitching due to sorting large lists)

In combination with the **Trie search algorithm** (O(P) + O(M)), this strategy ensures that **every keystroke in the search bar updates results instantly**, even for the largest dataset size.


  
---

## 🔍 Search Algorithm

- **Prefix Search** implemented via a **Trie** (`CityTrie`).
- Search complexity: **O(P + M)**, where:
  - **P** = prefix length
  - **M** = number of matches
- Ensures **real‑time responsiveness** while typing.

---

## 🔄 Refresh Functionality (UX Enhancement)

We’ve implemented **Refresh** as an enhancement to the user experience:

- **User state is preserved**:
  - If the user has **no query**: refresh reloads the full dataset and displays it.
  - If the user **entered a search query**: refresh reloads data, **immediately applies the same query as a filter**.
- Benefits:
  - Keeps search context intact after refresh without forcing the user to re‑type.
  - Ensures displayed results are from the most up‑to‑date dataset without breaking their flow.

---


## ⚙️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **DI:** Dagger-Hilt (with KSP)
- **Coroutines & Flows** for async work & reactive state
- **JSON Parsing:** Gson
- **Architecture Pattern:** Clean Architecture + MVVM
- **Image Loading:** Coil (for flags if needed)
- **Min API:** 21
- **AGP:** 8.x

---

## 🚀 Running the App

1. Clone the repository
2. Open in Android Studio (latest stable)
3. Build project and install on an Android device/emulator (API 21+)
4. Search bar at bottom can be used to filter cities; click refresh to reload

---

## 📂 Project Structure

```
app/
├── data/
│   ├── datasource/
│   ├── mapper/
│   ├── model/
│   ├── repository/
│   └── trie/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── presentation/
│   ├── stateHolder/  # ViewModels, #CityScreenState
│   ├── ui/           # Screens and Composables
│   ├── model/           # Ui models
```

---

## ✨Potential Enhancements

- *Unit Testing**.
- **UI testing**.
- **Localization**
- **Add UI for empty data state, either regular data or search result**
- **Adding Icons for countries**: Resources are not available


---


