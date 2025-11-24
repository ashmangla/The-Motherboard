# Testing the Agentic App

This guide explains how to run the Python server and the Android client to test the application.

## 1. Run the Server (The "Brain")

The server handles the logic and runs on your computer.

1.  **Open a terminal** and navigate to the `server` directory:
    ```bash
    cd server
    ```
2.  **Set up the environment** (if not already done):
    ```bash
    python3 -m venv venv
    source venv/bin/activate
    pip install -r requirements.txt
    ```
3.  **Start the server**:
    ```bash
    uvicorn main:app --reload
    ```
    You should see output indicating the server is running at `http://127.0.0.1:8000`.

## 2. Run the Android App (The "Body")

The Android app runs on an emulator or a physical device.

1.  **Open Android Studio**.
2.  **Open the Project**: Select "Open" and choose the `android` folder inside `The-Motherboard`.
3.  **Sync Gradle**: Let Android Studio download dependencies and configure the project.
4.  **Create/Select a Device**:
    *   **Emulator**: Go to Device Manager, create a device (e.g., Pixel 6) with API 34.
    *   **Physical Device**: Enable Developer Options and USB Debugging on your phone, then connect it via USB.
5.  **Run**: Click the green "Run" (Play) button in the toolbar.

## 3. What to Expect

*   **Server**: The terminal will show the server logs.
*   **Android App**: The app will launch and display "Hello Android Agent!".

## 4. Note on Connection

Currently, the Android app is a basic scaffold and **does not yet connect** to the Python server.
*   The server has a WebSocket endpoint ready at `ws://localhost:8000/ws`.
*   The Android app needs logic added to `MainActivity.kt` to connect to this endpoint.

**Next Step**: Implement the WebSocket client in the Android app to enable real communication.
