package com.B3tween.app.modules.www.api.routes;

import java.io.*;
import java.net.*;
import org.json.*;
import java.time.Instant;

import com.B3tween.app.modules.log.Log;
import com.B3tween.app.objects.enums.Method;
import com.B3tween.app.objects.dto.requestDto;
import com.B3tween.app.modules.auth.dto.AuthDto;
import com.B3tween.app.modules.www.jwt.dto.JwtDto;
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
                 *  Root: (/)
                 *  Functions like a Health Check
                 */
                case "/":
                    apiUtils.responses.twoHundredOk(clientSocket);
                    break;
                
                /**
                 *  /auth/register
                 *  ~=== POST DATA ===~
                 * +-------------------+
                 * |   Field  |  Type  |
                 * +-------------------+
                 * | username | String |
                 * | password | String |
                 * +----------+--------+
                 */
                case "/auth/register":

                    if (request.getMethod().equals(Method.POST)) {

                        // Parse JSON
                        JSONObject json = new JSONObject(request.getData());
                        String username = json.getString("username").trim();
                        String password = json.getString("password").trim();

                        // Check for null values
                        if (username.isEmpty() || password.isEmpty())
                            apiUtils.responses.registerConflict(clientSocket);

                        else {
                            // Check if user exists
                            if (authRepository.canUserRegister(username, password)) {

                                // JWT Token
                                JwtDto jwt = JwtDto.builder()
                                    .header("{ \"alg\": \"HS256\", \"typ\": \"JWT\" }")
                                    .payload("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                                    .build();
                                jwt.generateToken();

                                // Auth DTO
                                AuthDto user = AuthDto.builder()
                                    .id(apiUtils.getNextUserId())
                                    .createdAt(Instant.now().toEpochMilli())
                                    .username(username)
                                    .password(password)
                                    .bearerToken(jwt.token)
                                    .build();

                                // Save user
                                Log.l("[API] User registered id=" + user.getId() + " username=" + user.getUsername());
                                authRepository.save(user);

                                // Response
                                // TODO: Redirect user to the login page
                                //apiUtils.responses.foundRedirect(clientSocket, "localhost:"+globalRuntime.WEB_PORT);
                                apiUtils.responses.twoHundredOk(clientSocket);

                            } else {
                                // User already exists
                                apiUtils.responses.registerConflictUsername(clientSocket);

                            }

                        }

                    } else {
                        // Method not valid
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
