# MockTool - Mock Service for I-Eval Testing Toolkit
## 1. Project Introduction
MockTool is a Spring Boot application designed to simulate the HTTP services of the I-Eval testing toolkit. It provides mock interfaces for various devices, supporting device status management, data processing, and business process simulation.
## 2. HTTP Service Interfaces
### 2.1 Air Conditioner Device Interfaces
| No. | Interface Path                  | Request Method | Purpose                  | Description |
|-----|---------------------------------|----------------|--------------------------|-------------|
| 1   | `/api/air-conditioner/update`   | POST           | Update AC device status  | Update AC parameters (temperature, mode, fan speed, etc.) |
| 2   | `/api/air-conditioner/status`   | POST           | Query AC device status   | Get current AC operating status and parameters |
| 3   | `/api/air-conditioner/set`      | POST           | Set user feedback        | Receive user feedback information |
| 4   | `/api/air-conditioner/performance` | POST       | Get AC performance data  | Get AC performance metrics and statistics |
### 2.2 Post Service Interfaces
| No. | Interface Path          | Request Method | Purpose          | Description |
|-----|-------------------------|----------------|------------------|-------------|
| 1   | `/posts/{userId}/create`| POST           | Create post      | Create new post content |
| 2   | `/posts/posts/add`      | POST           | Add post         | Add post (simulates 20-second processing time) |
| 3   | `/posts/posts/modify`   | POST           | Modify post      | Modify existing post content |
### 2.3 Industrial Standard Interfaces
#### 2.3.1 Electrical Box Interfaces
| No. | Interface Path              | Request Method | Purpose          | Description |
|-----|-----------------------------|----------------|------------------|-------------|
| 1   | `/api/ebox/power_off`       | POST           | Power off operation | Execute the first step of power-off process |
| 2   | `/api/ebox/verify`          | POST           | Verify operation | Verify no-voltage status |
| 3   | `/api/ebox/handle`          | POST           | Handle operation | Execute handling steps |
| 4   | `/api/ebox/recheck`         | POST           | Recheck operation | Execute recheck steps |
| 5   | `/api/ebox/restore`         | POST           | Restore operation | Restore power supply, complete process |
#### 2.3.2 Method Pattern Interfaces
| No. | Interface Path                              | Request Method | Purpose          | Description |
|-----|---------------------------------------------|----------------|------------------|-------------|
| 1   | `/api/method/update_production_order`        | POST           | Update production order | Update production order status |
| 2   | `/api/method/update_device`                  | POST           | Update device information | Update device status information |
| 3   | `/api/method/create_quality_check`           | POST           | Create quality check | Create quality check record (simulates 5-second processing time) |
### 2.4 CNC Machining Management Interfaces
#### 2.4.1 Machining Plan Interfaces (`/cnc/process/plans`)
| No. | Interface Path                  | Request Method | Purpose          | Description |
|-----|---------------------------------|----------------|------------------|-------------|
| 1   | `/cnc/process/plans`            | POST           | Upload machining plan | Create new machining plan |
| 2   | `/cnc/process/plans/{planId}`   | GET            | Query single plan | Get plan details by plan ID |
| 3   | `/cnc/process/plans`            | GET            | Query all plans   | Get list of all machining plans |
| 4   | `/cnc/process/plans/{planId}/tasks` | GET       | Query plan tasks  | Get all tasks under specified plan |
#### 2.4.2 Workpiece Management Interfaces (`/cnc/process/workpieces`)
| No. | Interface Path                              | Request Method | Purpose          | Description |
|-----|---------------------------------------------|----------------|------------------|-------------|
| 1   | `/cnc/process/workpieces`                   | POST           | Upload workpiece info | Create new workpiece record |
| 2   | `/cnc/process/workpieces/{workpieceId}`     | GET            | Query workpiece info | Get details by workpiece ID |
| 3   | `/cnc/process/workpieces`                   | GET            | Query all workpieces | Get list of all workpieces |
| 4   | `/cnc/process/workpieces/{workpieceId}/batch` | POST       | Create batch      | Create batch for workpiece |
| 5   | `/cnc/process/workpieces/batch/{batchId}`   | GET            | Query batch info  | Get batch details |
| 6   | `/cnc/process/workpieces/{workpieceId}/transport` | POST | Upload transport info | Record workpiece transport information |
| 7   | `/cnc/process/workpieces/{workpieceId}/transport` | GET  | Query transport info | Query workpiece transport records |
| 8   | `/cnc/process/workpieces/{workpieceId}/storage` | POST   | Upload storage info | Record workpiece storage information |
| 9   | `/cnc/process/workpieces/{workpieceId}/storage` | GET    | Query storage info | Query workpiece storage records |
#### 2.4.3 Tool Management Interfaces (`/cnc/process/tools`)
| No. | Interface Path                              | Request Method | Purpose          | Description |
|-----|---------------------------------------------|----------------|------------------|-------------|
| 1   | `/cnc/process/tools`                        | POST           | Upload tool info | Create new tool record |
| 2   | `/cnc/process/tools/{toolId}`                | GET            | Query tool info  | Get details by tool ID |
| 3   | `/cnc/process/tools/usage`                  | POST           | Upload tool usage record | Record tool usage |
| 4   | `/cnc/process/tools/usage/{toolId}`         | GET            | Query tool usage record | Query tool usage history |
| 5   | `/cnc/process/tools/consumption`            | POST           | Upload tool consumption record | Record tool consumption |
| 6   | `/cnc/process/tools/consumption/{toolId}`   | GET            | Query tool consumption record | Query tool consumption history |
| 7   | `/cnc/process/tools/life/{toolId}`          | GET            | Query tool life   | Query remaining tool life |
| 8   | `/cnc/process/tools/life/{toolId}/adjust`   | POST           | Adjust tool life  | Manually adjust tool life value |
#### 2.4.4 Template and Standard Interfaces (`/cnc/process`)
| No. | Interface Path                  | Request Method | Purpose          | Description |
|-----|---------------------------------|----------------|------------------|-------------|
| 1   | `/cnc/process/templates`        | POST           | Upload process template | Create new process template |
| 2   | `/cnc/process/templates/{templateId}` | GET | Query template info | Get details by template ID |
| 3   | `/cnc/process/templates`        | GET            | Query all templates | Get list of all templates |
| 4   | `/cnc/process/standards`        | POST           | Upload process standard | Create new process standard |
| 5   | `/cnc/process/standards/{standardId}` | GET | Query standard info | Get details by standard ID |
| 6   | `/cnc/process/standards`        | GET            | Query all standards | Get list of all standards |
#### 2.4.5 Environment Monitoring Interfaces (`/cnc/process/environment`)
| No. | Interface Path                              | Request Method | Purpose          | Description |
|-----|---------------------------------------------|----------------|------------------|-------------|
| 1   | `/cnc/process/environment/temperature`      | POST           | Upload temperature data | Record ambient temperature |
| 2   | `/cnc/process/environment/temperature`      | GET            | Query temperature data | Query temperature history |
| 3   | `/cnc/process/environment/humidity`         | POST           | Upload humidity data | Record ambient humidity |
| 4   | `/cnc/process/environment/humidity`         | GET            | Query humidity data | Query humidity history |
| 5   | `/cnc/process/environment/air-quality`      | POST           | Upload air quality data | Record air quality metrics |
| 6   | `/cnc/process/environment/air-quality`      | GET            | Query air quality data | Query air quality history |
| 7   | `/cnc/process/environment/summary`          | GET            | Query environment monitoring summary | Get comprehensive environment monitoring data |
| 8   | `/cnc/process/environment/alerts`           | GET            | Query environment alerts | Get environment anomaly alerts |
#### 2.4.6 Other CNC Interfaces
| No. | Interface Path                  | Request Method | Purpose          | Description |
|-----|---------------------------------|----------------|------------------|-------------|
| 1   | `/cnc/process/logs`             | POST           | Upload machining log | Record machining process logs |
| 2   | `/cnc/process/logs/{logId}`     | GET            | Query machining log | Query specified log details |
| 3   | `/cnc/process/reports`          | POST           | Upload machining report | Create machining report |
| 4   | `/cnc/process/reports/{reportId}` | GET         | Query machining report | Query specified report details |
| 5   | `/cnc/process/schedules`        | POST           | Create production schedule | Create production schedule |
| 6   | `/cnc/process/schedules/{scheduleId}` | GET  | Query production schedule | Query schedule details |
### 2.5 Travel Plan Interfaces
#### 2.5.1 Basic Travel Plan Interfaces (`/travel-plans/basic`)
| No. | Interface Path                  | Request Method | Purpose          | Description |
|-----|---------------------------------|----------------|------------------|-------------|
| 1   | `/travel-plans/basic`           | POST           | Create travel plan | Create new travel plan |
| 2   | `/travel-plans/basic/{planId}`  | GET            | Query travel plan | Query specified plan details |
| 3   | `/travel-plans/basic/{planId}`  | PUT            | Update travel plan | Update plan information |
| 4   | `/travel-plans/basic/{planId}`  | DELETE         | Delete travel plan | Delete specified plan |
| 5   | `/travel-plans/basic`           | GET            | Query all plans   | Get list of all travel plans |
#### 2.5.2 Extended Travel Plan Interfaces (`/travel-plans-extended`)
| No. | Interface Path                              | Request Method | Purpose          | Description |
|-----|---------------------------------------------|----------------|------------------|-------------|
| 1   | `/travel-plans-extended/{planId}/transport/route` | GET | Query transport routes | Get transport route information |
| 2   | `/travel-plans-extended/{planId}/transport/flights` | GET | Query flight information | Get flight options |
| 3   | `/travel-plans-extended/{planId}/transport/trains` | GET | Query train information | Get train options |
| 4   | `/travel-plans-extended/{planId}/transport/cost-estimate` | GET | Estimate transport costs | Calculate transport costs |
#### 2.5.3 Advanced Travel Plan Interfaces (`/travel-plans-advanced`)
| No. | Interface Path                              | Request Method | Purpose          | Description |
|-----|---------------------------------------------|----------------|------------------|-------------|
| 1   | `/travel-plans-advanced/{planId}/attractions/recommendations` | GET | Get attraction recommendations | Get recommended attraction list |
| 2   | `/travel-plans-advanced/{planId}/attractions/{attractionId}` | GET | Query attraction details | Query specified attraction information |
| 3   | `/travel-plans-advanced/{planId}/activities/recommendations` | GET | Get activity recommendations | Get recommended activity list |
#### 2.5.4 Travel Plan Booking Interfaces (`/travel-plans/booking`)
| No. | Interface Path                              | Request Method | Purpose          | Description |
|-----|---------------------------------------------|----------------|------------------|-------------|
| 1   | `/travel-plans/booking/{planId}/hotels`     | POST           | Book hotel       | Create hotel reservation |
| 2   | `/travel-plans/booking/{planId}/hotels`     | GET            | Query hotel reservation | Query hotel reservation information |
| 3   | `/travel-plans/booking/{planId}/flights`    | POST           | Book flight      | Create flight reservation |
| 4   | `/travel-plans/booking/{planId}/flights`    | GET            | Query flight reservation | Query flight reservation information |
## 3. Packaging Method
### 3.1 Environment Requirements
- JDK 17 or higher
- Maven 3.6 or higher
### 3.2 Packaging Steps
1. **Package with Maven**
```bash
mvn clean package -Dmaven.test.skip=true
```
2. **Packaging Results**
After packaging, the following files will be generated in the `target` directory:
- `mocktool-0.0.1-SNAPSHOT.jar` - Executable Spring Boot JAR package
- `mocktool-0.0.1-SNAPSHOT.jar.original` - Original JAR package (without dependencies)
3. **Run JAR Package**
```bash
# Run directly
java -jar target/mocktool-0.0.1-SNAPSHOT.jar
# Run with specified port
java -jar target/mocktool-0.0.1-SNAPSHOT.jar --server.port=8081
# Run in background (Linux/Mac)
nohup java -jar target/mocktool-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```
## 4. Docker Container Deployment
### 4.1 Deploy with Docker Compose (QC Environment)
The project uses Docker Compose for containerized deployment, adopting a JAR package mounting approach without building images.
#### 4.1.1 Start Service
```bash
# 1. Ensure JAR file is packaged
mvn clean package -Dmaven.test.skip=true
# 2. Start service (run in background)
docker-compose up -d
# 3. Check service status
docker-compose ps
# 4. View logs
docker-compose logs -f
```
#### 4.1.2 Stop Service
```bash
# Stop service
docker-compose down
# Stop service and delete data volumes
docker-compose down -v
```
#### 4.1.3 Restart Service
```bash
# Restart service
docker-compose restart
```
### 4.2 Configuration Description
- **Deployment Method**: Docker Compose deployment
- **Log Configuration**: Use `logback.xml` for log configuration, log files mounted to `./logs` directory
- **Data Files**: Project uses data files in `src/main/resources` directory
- **Container Name**: mocktool
## 5. Interface Description
### 5.1 Request Format
- All interfaces use JSON format for data interaction
- Content-Type: `application/json`
### 5.2 Response Format
Standard response format:
```json
{
  "code": 200,
  "message": "Operation successful",
  "data": {}
}
```
### 5.3 Simulation Features
- **Delay Simulation**: Some interfaces simulate actual device processing delays (e.g., 2-20 seconds)
- **State Management**: Interfaces maintain device state, supporting status queries and updates
- **Random Results**: Some interfaces randomly return SUCCESS, FAIL, ERROR, etc.
- **Data Persistence**: Uses in-memory cache (Guava Cache) for data management
---
