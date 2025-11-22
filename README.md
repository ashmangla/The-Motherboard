# Agentic App (The-Motherboard)

A client-server agentic application for Android, designed with a Python "Brain" and a Native Android "Body".

## Technology Stack

### 1. The Server (The "Brain")
**Technology:** Python with FastAPI
*   **Why Python?** It is the native language of AI (LangChain, OpenAI SDK, etc.).
*   **Why FastAPI?**
    *   **Async Support:** Handles agent "thinking" time without blocking.
    *   **Streaming:** Supports token-by-token text streaming.
    *   **Performance:** High-performance Python framework.

### 2. The Client (The "Body")
**Technology:** Android Native (Kotlin) + Jetpack Compose
*   **Why Native?** Reliable access to device hardware (Microphone, Camera, Location).
*   **Why Jetpack Compose?** Modern, declarative UI toolkit for building beautiful interfaces quickly.

### 3. Communication
**Protocol:** WebSockets
*   **Why?** Enables real-time, bi-directional communication essential for conversational agents.

---

## Project Structure

### Server (`server/`)
*   **`main.py`**: The entry point for the FastAPI server.
    *   `GET /`: Health check.
    *   `WS /ws`: WebSocket endpoint for agent communication.
*   **`requirements.txt`**: Python dependencies (`fastapi`, `uvicorn`, `websockets`).

### Android Client (`android/`)
*   **Native Kotlin Project**: Standard Android Studio project structure.
*   **`MainActivity.kt`**: The entry point for the Android app, using Jetpack Compose.

---

## Getting Started

### Prerequisites
*   Python 3.9+
*   Android Studio Hedgehog or newer

### Running the Server
1.  Navigate to the `server` directory:
    ```bash
    cd server
    ```
2.  Create and activate a virtual environment:
    ```bash
    python3 -m venv venv
    source venv/bin/activate
    ```
3.  Install dependencies:
    ```bash
    pip install -r requirements.txt
    ```
4.  Start the server:
    ```bash
    uvicorn main:app --reload
    ```
    The server will be running at `http://127.0.0.1:8000`.

### Running the Client
1.  Open Android Studio.
2.  Select **Open** and navigate to the `android` directory of this repository.
3.  Allow Gradle to sync.
4.  Run the app on an Emulator or physical device.
