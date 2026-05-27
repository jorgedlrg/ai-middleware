# ComfyUI Image Generation - Functional Summary

## Overview
We are building an adapter that allows our middleware to trigger image generation via ComfyUI's API and automatically persist the resulting images to local disk when ComfyUI signals completion via WebSocket.

## Components

### 1. Settings (domain/user/Settings.java)
- New field `comfyUiHost` (String, e.g. `"127.0.0.1:8188"`) stored in the user's preferences

### 2. ComfyUiOutPort (application/port/out/)
- Outbound port interface defining `queuePrompt(workflow, clientId, relativePath)` → returns `promptId`
- `relativePath` is the target path relative to `~/aimiddleware/assets/` (e.g. `"portraits/hero-image.png"`)

### 3. ComfyUiClient (adapter/out/web/)
- HTTP client using Spring's `RestClient`
- `queuePrompt()` → POST to `/prompt`, returns `prompt_id`
- `getHistory(promptId)` → GET from `/history/{promptId}` to retrieve output metadata
- `getImage(filename, subfolder, type)` → GET from `/view` returning raw bytes

### 4. ComfyUiWebSocketListener (adapter/in/message/)
- Spring WebSocket client connecting to `ws://{host}/ws?clientId={clientId}`
- **Persistent connection** with auto-reconnect every 30s on drop
- On `executing` event with `node == null` + matching `prompt_id` → image is complete
- On `execution_error` → logs at WARN level
- On completion, notifies `ComfyUiAdapter` with the `promptId`

### 5. ComfyUiAdapter (adapter/out/comfyui/)
- Implements `ComfyUiOutPort`
- Maintains `ConcurrentHashMap<promptId, relativePath>` (note: could be externalized in future)
- `queuePrompt()` stores path mapping, calls `ComfyUiClient.queuePrompt()`, returns `promptId`
- Completion callback: fetches outputs via client, saves each image to `AssetRepository` using the stored `relativePath`

### 6. DTOs (adapter/out/web/dto/)
- `ComfyUiPromptRequest`, `ComfyUiPromptResponse`
- `ComfyUiHistoryResponse`, `ComfyUiWebSocketMessage`

## Data Flow

```
UseCase → ComfyUiOutPort.queuePrompt(workflow, clientId, "portraits/hero-image.png")
                    ↓
            ComfyUiAdapter stores {promptId → "portraits/hero-image.png"}
                    ↓
            ComfyUiClient POST /prompt → returns promptId
                    ↓
        ComfyUiWebSocketListener (persistent, waiting for promptId)
                    ↓
            ComfyUI processes, sends "executing" events via /ws
                    ↓
            On node==null + promptId match → completion detected
                    ↓
            ComfyUiAdapter fetches /history/{promptId} → gets output filenames
                    ↓
            For each image: GET /view → byte[] → AssetRepository.save("portraits", "hero-image.png", bytes)
```

## Folder Structure
Images saved to: `~/aimiddleware/assets/{relativePath}`
- Example: `relativePath = "portraits/hero-image.png"` → `~/aimiddleware/assets/portraits/hero-image.png`

## Configuration
- ComfyUI host comes from `Settings.comfyUiHost()` (per-user setting)
- No authentication for now (no extra validation on Settings)

## Non-Goals (for later)
- Multiple candidate generation / quality scoring
- Queue management / scaling to multiple GPU workers
- Workflow template management
- Any domain events or state updates beyond saving to disk