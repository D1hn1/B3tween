package com.B3tween.app.modules.www.api.routes;

import java.io.*;
import java.net.*;
import java.util.List;
import org.json.*;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.enums.Method;
import com.B3tween.app.objects.dto.headerDto;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.objects.dto.responseDto;
import com.B3tween.app.modules.www.api.utils.apiUtils;
import com.B3tween.app.modules.proxy.utils.proxyUtils;
import com.B3tween.app.modules.auth.repository.authRepository;

public class handleRoutes {
    
    public static void Handle(Socket clientSocket) {

        try (BufferedWriter writer = 
                new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            // Get request from client
            requestDto request = proxyUtils.getRequest(clientSocket);
            Log.i("[API] " + request.getMethod().getLabel().toUpperCase() + " " 
                + request.getURL().getPath());

            // Parse routes
            switch (request.getURL().getPath()) {

                /**
                 *  / Path: functions like a Health check
                 */
                case "/":
                    apiUtils.responses.twoHundredOk(clientSocket);
                    break;
                
                /**
                 * /auth/register
                 * ~= POST data =~
                 * \_ username: String
                 * \_ password: String   
                 */
                case "/auth/register":

                    if (request.getMethod().equals(Method.POST)) {

                        // Parse JSON
                        JSONObject json = new JSONObject(request.getData());
                        String username = json.getString("username");
                        String password = json.getString("password");

                        // Check for null values
                        if (username.isEmpty() || password.isEmpty())
                            apiUtils.responses.userAlreadyExists(clientSocket);

                        else {
                            // Check if user exists
                            if (authRepository.canUserRegister(username, password)) {
                                // USER SANITAZIED
                                // GET THE JWT OF THE USER -> CRAFT IT HERE
                                // CREATE A FUNCTION TO SAVE THE AUTHDTO ENTITY IN THE REPOSITORY CODE OF THE AUTH
                            }

                        }
                        // GET request.data
                        // Parse the JSON
                        // Validate the username/password
                        // See if exists -> If exists redirect to login
                        //               |_ If not exists save them with the authdto into the list
                        //                  assigning a JWT and a ID with the apiutils. -> Then redirect to login.
                    } else {
                        apiUtils.responses.methodNotAllowed(clientSocket);
                    }

                    break;
 
                /**
                 *  /auth/login
                 */
                case "/auth/login":
                    apiUtils.responses.twoHundredOk(clientSocket);

                    // GET request.data
                    // Parse the JSON
                    // Validate the username/password.
                    // See if exists -> If not exists = error message.
                    //               \_ If exists: set the cookie the JWT and send it to another page. 
                    break;

                default:
                    apiUtils.responses.resourceNotFound(clientSocket);
                    break;
            }

            clientSocket.close();

        } catch (IOException io) {}

    }
    
}
