package fefu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class Handler implements HttpHandler {
    private final Server server;
    private final ObjectMapper mapper = new ObjectMapper();

    public Handler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (method.equals("POST") && path.equals("/chefs")) {
                InputStream is = exchange.getRequestBody();
                Map<?, ?> json = mapper.readValue(is, Map.class);

                Chef chef = server.createChef((String) json.get("name"), (String) json.get("type"));

                byte[] body = mapper.writeValueAsBytes(chef);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                exchange.sendResponseHeaders(201, body.length);
                try (OutputStream out = exchange.getResponseBody()) {
                    out.write(body);
                }
            }
            else if (method.equals("POST") && path.startsWith("/dishes/") && path.endsWith("/cook")) {
                String dishId = path.split("/")[2];
                Dishes dish = server.cook(dishId);

                byte[] body = mapper.writeValueAsBytes(dish);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream out = exchange.getResponseBody()) {
                    out.write(body);
                }
            }
            else {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
            }
        } catch (Exception e) {
            // 4. Ловим ошибки
            byte[] body = mapper.writeValueAsBytes(Map.of("error", e.getMessage()));
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(400, body.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(body);
            }
        }
    }
}


