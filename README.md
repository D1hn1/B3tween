<p align="center">
   <img src="./B3.jpg">
</p>

# B3tween
B3tween is a proxy capable of handling http & https connections. It is built from the group up, using a controller > service > repository architecture. 
### Modules
It has 3 main modules which are:
- ``PROXY``: It is the module that forwards connection from the user
- ``API``: It is the backend part of the application, it handlers all the routes the frontend requests
- ``WEB (frontend)``: It is the web part of the application where the user logs in and it gives metrics about the connection and user data, and if logged in with the admin user you can see global metrics as well as configuring some parameters about the proxy.

## Tech Stack
- Java version 21
- Maven version 3.8.7
- HTML, CSS
- JavaScript

## Running
### Requisites
- To compile the program you will need to have ``Maven installed``.
- You also need to have a ``Java SDK installed``.
```bash
root@root~# apt install maven
root@root~# apt install openjdk-21-jdk
```
### Compiling
- To compile the program run the next command on the terminal.
```bash
root@root~# mvn package
root@root~# cd target/
```
### Running
- To run the program
```bash
root@root~# java -jar B3tween-X.X.jar
```

## Todo next:
- URL blocker proxy from txt list
- Make an already authed proxy list to faster log in the user