package fefu;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);

        httpServer.createContext("/", new Handler(server));

        httpServer.setExecutor(null);
        httpServer.start();

        System.out.println("Сервер запущен на http://localhost:8000");
    }
}