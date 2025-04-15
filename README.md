# Steps to Create a Proxy Server

1. Choose a programming language and tools (e.g., Python, Node.js, Java).
2. Set up your development environment.
3. Create a basic server that listens for incoming HTTP requests.
4. Parse the incoming request to extract the destination URL.
5. Forward the request to the target server.
6. Receive the response from the target server.
7. Relay the response back to the original client.
8. Add error handling for failed requests or timeouts.
9. Optionally, implement support for other HTTP methods (POST, PUT, etc.).
10. Test your proxy using tools like curl, a browser, or Postman.
11. (Optional) Add extra features like logging, filtering, caching, authentication, or HTTPS support.

# TODO
- [X] Make a builder for the DTO entities.
- [ ] Make a handler for the incoming http request.
- [ ] End with the urlDto class.
- [ ] Write the makeRequest function.
- [ ] Try it out with foxyproxy.

# Types of Proxy Servers

1. **Forward Proxy**
   - Sits between the client and the internet.
   - Hides the client’s identity from the destination server.
   - Commonly used for anonymity, access control, and content filtering.

2. **Reverse Proxy**
   - Sits in front of one or more backend servers.
   - Clients interact with the proxy, which then forwards the request to the appropriate server.
   - Used for load balancing, caching, and securing backend services.

3. **Transparent Proxy**
   - Intercepts requests without modifying them or revealing itself to the client.
   - Often used in network monitoring and content filtering (e.g., in schools or businesses).

4. **Anonymous Proxy**
   - Hides the client’s IP address from the target server but identifies itself as a proxy.
   - Offers some privacy, but not complete anonymity.

5. **High Anonymity Proxy (Elite Proxy)**
   - Hides the client’s IP and doesn’t reveal that it’s a proxy.
   - Provides strong anonymity for browsing or scraping.

6. **Distorting Proxy**
   - Masks the client’s IP address but gives a false one to the destination.
   - Reveals itself as a proxy while providing misinformation about the client.

7. **SOCKS Proxy**
   - Works at a lower level than HTTP, forwarding any kind of traffic (TCP/UDP).
   - Used for things like torrenting, games, or bypassing firewalls.

8. **Web Proxy**
   - Accessed via a web page (e.g., hide.me, KProxy).
   - Allows browsing through a proxy without configuring the browser.

9. **Caching Proxy**
   - Stores copies of frequently accessed resources to reduce latency and bandwidth usage.
   - Useful for improving performance in large networks.

10. **Content Filtering Proxy**
    - Monitors and restricts access to certain content based on rules.
    - Common in schools, workplaces, and parental control setups.
