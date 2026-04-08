# Access to Services and Tools

This document lists the commands to start the RetourMarche project infrastructure and the URLs to access them locally.

## Starting the Services

To correctly start the database, the application, and all monitoring / log tools managed by Docker Compose, run the following command at the project root (`/home/cytech/01_Kasetsart/WebProjet/RetourMarche`):

```bash
docker compose up -d
```

This command will launch the containers in the background.

*(If you have made changes to the application code, remember to rebuild the application image with `docker compose up -d --build app`)*.

---

## Local Addresses

Once the services are started, here is how to access them via your browser:

### 1. Main Application (RetourMarche)
- **URL**: [http://localhost:8080](http://localhost:8080)
- **Nginx Reverse Proxy**: [http://localhost:8081](http://localhost:8081)

### 2. Real-time Logs (Dozzle)
Dozzle provides a readable interface to view logs from all containers, in JSON or standard text format. Application login events will be clearly visible here.
- **URL**: [http://localhost:8888](http://localhost:8888)

### 3. Monitoring and Dashboards (Grafana)
Visual dashboards for monitoring requests, latency, and resources.
- **URL**: [http://localhost:3000](http://localhost:3000)
- **Default Credentials**: 
  - User: `admin`
  - Password: `admin`

### 4. Application Metrics (Prometheus)
A tool that collects raw application metrics (exposed via Actuator).
- **URL**: [http://localhost:9090](http://localhost:9090)
