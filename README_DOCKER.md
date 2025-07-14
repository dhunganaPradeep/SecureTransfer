# SecureTransfer Docker & UI Guide

## Prerequisites
- Docker & Docker Compose installed
- X11 server running on your host (Linux desktop)
- Allow Docker containers to access your X11 display:
  - Run: `xhost +local:docker`

## Build & Run

1. **Build and start both instances:**
   ```sh
   docker-compose up --build
   ```
   This will start two containers, each with its own ports and database.

2. **Accessing the UI:**
   - The JavaFX UI will appear as a window on your host desktop (via X11 forwarding).
   - You should see two separate SecureTransfer windows (one per instance).

3. **Testing File Transfer:**
   - Use the UI in each window to log in/register and test sending/receiving files between the two instances.
   - Each instance uses its own ports and database, so there will be no conflicts.

## Port Mapping
| Instance   | HTTP | WebSocket | P2P  | DB File              |
|------------|------|-----------|------|----------------------|
| instance1  | 8081 | 8446      | 8447 | /data/securetransfer1|
| instance2  | 8082 | 8448      | 8449 | /data/securetransfer2|

## Notes
- If you want to run more instances, add more services to `docker-compose.yml` with unique ports and DB files.
- If you see display errors, ensure your X11 server is running and `xhost +local:docker` is set.
- For non-Linux hosts, consider using VcXsrv (Windows) or XQuartz (macOS) and adjust DISPLAY accordingly. 