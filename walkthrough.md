# Verification Walkthrough - WebSocket Connection

## Goal
Verify that the Android app can connect to the Python server and exchange messages.

## Changes Made
1.  **AndroidManifest.xml**: Added Internet permission and allowed cleartext traffic.
2.  **MainActivity.kt**: Implemented `WebSocketManager`, `ChatViewModel`, and `ChatScreen`.

## Verification Steps

### 1. Start the Server
1.  Open a terminal in the `server` directory.
2.  Run `uvicorn main:app --reload`.
3.  Ensure it says `Uvicorn running on http://127.0.0.1:8000`.

### 2. Run the Android App
1.  **Launch Android Studio**.
2.  **Open Project**:
    *   Click **Open** (or **File > Open** if you are already inside a project).
    *   Navigate to `/Users/ashimamangla/The-Motherboard/The-Motherboard/android`.
    *   **Crucial**: Select the `android` folder specifically, NOT the top-level `The-Motherboard` folder.
    *   Click **Open**.
3.  **Wait for Sync**:
    *   Look at the bottom-right corner. You will see a progress bar saying "Gradle Sync".
    *   Wait until this finishes and the file structure appears on the left.
4.  **Create an Emulator** (if you don't have one):
    *   Click the phone icon on the top right (Device Manager).
    *   Click **+** or **Create Device**.
    *   Select **Pixel 6** (or similar).
    *   Select a System Image (Recommended: **API 34** or **UpsideDownCake**). You might need to download it first.
    *   Finish the setup.
5.  **Run the App**:
    *   Select your emulator from the dropdown menu in the top toolbar.
    *   Click the green **Play** button (Run 'app').
    *   The emulator should launch, and then the app will open.

### 3. Test Connection
1.  **Check Status**: The app should show "Status: Connected" in green at the top.
2.  **Send Message**: Type "Hello Agent" and click "Send".
3.  **Verify UI**:
    *   You should see your message "Me: Hello Agent".
    *   You should see the server's reply "Server received: Hello Agent".
4.  **Verify Server Logs**:
    *   The terminal running the server should show the incoming message.

## Troubleshooting
*   **Status: Disconnected**: Ensure the server is running.
*   **Status: Error**: Check if you are using an Emulator. If using a physical device, you must change `10.0.2.2` in `MainActivity.kt` to your computer's actual IP address.
