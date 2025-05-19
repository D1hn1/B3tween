<p align="center">
   <img src="./B3.jpg">
</p>

# B3tween
B3tween is a proxy capable of handeling http & https connections.

## Tech Stack
- Java 21
- Maven

## Modules
- Proxy module
- API module

### TODO NEXT:
- MERGE: develop <- feature/dashboard
- Login endpoint
- Logout endpoint
- Web module (frontend)
- Unify proxy
- Split up the routes into different files:
     - Login
     - Register
     - Logout
     - ...
- Make a queue based Log, to print out stats at the bottom of the terminal
     - Create a new thread.
     - Create a new list in global.
     - Modify the log file to add to the queue the logs.
     - Add a DTO for the log, to input x, y, color, and the message.
