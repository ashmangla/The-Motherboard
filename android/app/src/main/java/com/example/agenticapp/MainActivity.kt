package com.example.agenticapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agenticapp.ui.theme.AgenticAppTheme
import kotlinx.coroutines.launch
import okhttp3.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgenticAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen()
                }
            }
        }
    }
}

// --- WebSocket Manager ---
class WebSocketManager(private val listener: WebSocketListener) {
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .pingInterval(30, TimeUnit.SECONDS)
        .build()
    private var webSocket: WebSocket? = null

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(text: String): Boolean {
        Log.d("WebSocketManager", "Sending to socket: $text")
        return webSocket?.send(text) ?: false
    }

    fun close() {
        webSocket?.close(1000, "User closing")
    }
}

// --- ViewModel ---
class ChatViewModel : ViewModel() {
    // Emulator uses 10.0.2.2 to access host localhost
    private val serverUrl = "ws://10.0.2.2:8000/ws"
    
    var messages = mutableStateListOf<String>()
        private set

    var connectionStatus by mutableStateOf("Disconnected")
        private set

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            viewModelScope.launch {
                connectionStatus = "Connected"
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("ChatViewModel", "Received message: $text")
            viewModelScope.launch {
                messages.add(text)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            viewModelScope.launch {
                connectionStatus = "Closing..."
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            viewModelScope.launch {
                connectionStatus = "Disconnected"
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            viewModelScope.launch {
                connectionStatus = "Error: ${t.message}"
            }
            Log.e("WebSocket", "Error", t)
        }
    }

    private val webSocketManager = WebSocketManager(webSocketListener)

    init {
        connect()
    }

    fun connect() {
        connectionStatus = "Connecting..."
        webSocketManager.connect(serverUrl)
    }

    fun sendMessage(text: String) {
        Log.d("ChatViewModel", "Attempting to send: $text")
        if (text.isNotBlank()) {
            messages.add("Me: $text") // Optimistic update
            val success = webSocketManager.sendMessage(text)
            Log.d("ChatViewModel", "WebSocket send result: $success")
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.close()
    }
}

// --- UI ---
@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll when messages change
    LaunchedEffect(viewModel.messages.size) {
        if (viewModel.messages.isNotEmpty()) {
            listState.animateScrollToItem(viewModel.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Status: ${viewModel.connectionStatus}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (viewModel.connectionStatus == "Connected") Color.Green else Color.Red
                )
                if (viewModel.connectionStatus != "Connected" && viewModel.connectionStatus != "Connecting...") {
                    Button(onClick = { viewModel.connect() }) {
                        Text("Reconnect")
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    viewModel.sendMessage(inputText)
                    inputText = ""
                }) {
                    Text("Send")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        ) {
            items(viewModel.messages) { message ->
                MessageBubble(message)
            }
        }
    }
}

@Composable
fun MessageBubble(text: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
