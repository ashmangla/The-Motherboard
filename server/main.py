from fastapi import FastAPI, WebSocket
from fastapi.responses import HTMLResponse

app = FastAPI()

@app.get("/")
async def get():
    return HTMLResponse("<h1>Agentic App Server is Running</h1>")

@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    try:
        while True:
            data = await websocket.receive_text()
            await websocket.send_text(f"Server received: {data}")
            # Here we will eventually hook up the agent logic
    except Exception as e:
        print(f"WebSocket error: {e}")
