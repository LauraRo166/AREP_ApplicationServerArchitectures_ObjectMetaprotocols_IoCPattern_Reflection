package co.edu.escuelaing.httpserver;

import co.edu.escuelaing.microspringboot.annotations.GetMapping;
import co.edu.escuelaing.microspringboot.annotations.RequestParam;
import co.edu.escuelaing.microspringboot.annotations.RestController;
import java.net.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple HTTP server implementation that can serve static files
 * and handle annotated controller methods defined with custom
 * MicroSpringBoot annotations such as {@link GetMapping}.
 */
public class HttpServer {

    /** Map of service endpoints to their associated methods. */
    public static Map<String, Method> services = new HashMap<>();

    /** Base package name where annotated controllers are located. */
    public static String packageName = "co.edu.escuelaing.microspringboot.examples";

    /**
     * Loads all controller classes annotated with {@link RestController} from
     * the configured {@link #packageName}, and registers their methods annotated
     * with {@link GetMapping}.
     *
     * @throws URISyntaxException if there is a problem resolving the classpath location.
     */
    public static void loadServices() throws URISyntaxException {
        try {
            String path = packageName.replace('.', '/');
            File directory = new File(HttpServer.class.getClassLoader().getResource(path).toURI());
            File[] files = directory.listFiles((d, name) -> name.endsWith(".class"));

            if (files != null) {
                for (File file : files) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> c = Class.forName(className);

                    if (c.isAnnotationPresent(RestController.class)) {
                        Method[] methods = c.getDeclaredMethods();
                        for (Method m : methods) {
                            if (m.isAnnotationPresent(GetMapping.class)) {
                                String mapping = m.getAnnotation(GetMapping.class).value();
                                services.put(mapping, m);
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            System.getLogger(HttpServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    /**
     * Starts the HTTP server, listening on port 35000. This method accepts
     * incoming connections, parses requests, and dispatches them either to
     * a registered service method (for paths starting with "/app") or serves
     * static files from the "webroot" directory.
     *
     * @throws IOException if an I/O error occurs while handling client connections.
     * @throws URISyntaxException if there is an error resolving file paths.
     */
    public static void runServer() throws IOException, URISyntaxException {
        loadServices();

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;

        boolean running = true;
        while (running) {
            try {
                System.out.println("Ready to receive ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine = null;

            boolean firstline = true;
            URI requri = null;

            while ((inputLine = in.readLine()) != null) {
                if (firstline) {
                    requri = new URI(inputLine.split(" ")[1]);
                    System.out.println("Path: " + requri.getPath());
                    firstline = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            if (requri.getPath().startsWith("/app")) {
                outputLine = invokeService(requri);
                out.write(outputLine.getBytes());
            } else {
                serveStaticFile(requri, out);
            }

            out.flush();
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Invokes a registered service method based on the given request URI.
     * The method is resolved using the {@link GetMapping} value, and query
     * parameters are bound to method parameters using {@link RequestParam}.
     *
     * @param requri the request URI.
     * @return an HTTP response string.
     */
    private static String invokeService(URI requri) {
        String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
        try {
            HttpRequest req = new HttpRequest(requri);
            HttpResponse res = new HttpResponse();
            String servicePath = requri.getPath().substring(4);
            Method m = services.get(servicePath);

            RequestParam rp = (RequestParam) m.getParameterAnnotations()[0][0];
            String[] argsValues;
            if (requri.getQuery() == null) {
                argsValues = new String[]{rp.defaultValue()};
            } else {
                String queryParamName = rp.value();
                argsValues = new String[]{req.getValue(queryParamName)};
            }
            return header + m.invoke(null, argsValues);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            System.getLogger(HttpServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return header + "Error!";
    }

    /**
     * Serves a static file (HTML, CSS, JavaScript, images, etc.) from the
     * {@code webroot} directory in the classpath.
     *
     * @param requestUri the requested resource URI.
     * @param out the output stream to write the response.
     * @throws IOException if an error occurs while reading the file or writing the response.
     * @throws URISyntaxException if the file URI cannot be resolved.
     */
    private static void serveStaticFile(URI requestUri, OutputStream out) throws IOException, URISyntaxException {
        String path = requestUri.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }

        try {
            URL fileURL = HttpServer.class.getClassLoader().getResource("webroot" + path);
            if (fileURL == null) {
                out.write(notFound().getBytes());
                out.flush();
                return;
            }
            File file = new File(fileURL.toURI());
            if (file.exists() && !file.isDirectory()) {
                String contentType = getContentType(path);
                byte[] fileData = Files.readAllBytes(file.toPath());

                String header = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: " + contentType + "\r\n"
                        + "Content-Length: " + fileData.length + "\r\n"
                        + "\r\n";

                out.write(header.getBytes());
                out.write(fileData, 0, fileData.length);
                out.flush();
            }
        } catch (Exception e) {
            out.write(notFound().getBytes());
            out.flush();
        }
    }

    /**
     * Determines the MIME type of a file based on its extension.
     *
     * @param path file path or URI string.
     * @return MIME type string.
     */
    private static String getContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html";
        }
        if (path.endsWith(".css")) {
            return "text/css";
        }
        if (path.endsWith(".js")) {
            return "application/javascript";
        }
        if (path.endsWith(".png")) {
            return "image/png";
        }
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (path.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream";
    }

    /**
     * Generates an HTTP 404 Not Found response. If a custom
     * {@code 404.html} file exists in {@code webroot}, it will be
     * returned as the response body.
     *
     * @return HTTP 404 response string.
     */
    public static String notFound() {
        try {
            URL fileURL = HttpServer.class.getClassLoader().getResource("webroot/404.html");
            if (fileURL != null) {
                File file = new File(fileURL.toURI());
                if (file.exists() && !file.isDirectory()) {
                    String body = Files.readString(file.toPath());
                    return "HTTP/1.1 404 Not Found\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content-Length: " + body.getBytes().length + "\r\n"
                            + "\r\n"
                            + body;
                }
            }
        } catch (Exception e) {
        }
        String body = "404 - Not Found";
        return "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/plain\r\n"
                + "Content-Length: " + body.getBytes().length + "\r\n"
                + "\r\n"
                + body;
    }


    /**
     * Returns the default response, which loads and serves the
     * {@code index.html} file from {@code webroot}.
     *
     * @return HTTP 200 response with the index page, or HTTP 500 if an error occurs.
     */
    public static String defaultResponse() {
        try {
            URL fileURL = HttpServer.class.getClassLoader().getResource("webroot/index.html");
            if (fileURL != null) {
                File file = new File(fileURL.toURI());
                if (file.exists() && !file.isDirectory()) {
                    String body = Files.readString(file.toPath());
                    return "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Content-Length: " + body.getBytes().length + "\r\n"
                            + "\r\n"
                            + body;
                }
            }
        } catch (Exception e) {
        }
        return "HTTP/1.1 500 Internal Server Error\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n"
                + "Error loading default page";
    }
}