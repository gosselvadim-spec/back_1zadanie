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
        String query = exchange.getRequestURI().getQuery();

        try {
            if (method.equals("POST") && path.equals("/chefs")) {
                InputStream is = exchange.getRequestBody();
                Map<?, ?> json = mapper.readValue(is, Map.class);
                Chef chef = server.createChef((String) json.get("name"), (String) json.get("type"));
                sendJson(exchange, 201, chef);
            }
            else if (method.equals("POST") && path.equals("/dishes")) {
                InputStream is = exchange.getRequestBody();
                Map<?, ?> json = mapper.readValue(is, Map.class);
                Dishes dish = server.creatwdishes((String) json.get("name"), (String) json.get("chefId"));
                sendJson(exchange, 201, dish);
            }
            else if (method.equals("POST") && path.startsWith("/dishes/") && path.endsWith("/cook")) {
                String dishId = path.split("/")[2];
                Dishes dish = server.cook(dishId);
                sendJson(exchange, 200, dish);
            }

            else if (method.equals("GET") && path.equals("/chefs")) {
                String type = getQueryParam(query, "type");
                sendJson(exchange, 200, server.getChefs(type));
            }
            else if (method.equals("GET") && path.equals("/dishes")) {
                String status = getQueryParam(query, "status");
                sendJson(exchange, 200, server.getDishes(status));
            }

            else if (method.equals("PUT") && path.startsWith("/dishes/")) {
                String dishId = path.split("/")[2];
                InputStream is = exchange.getRequestBody();
                Map<?, ?> json = mapper.readValue(is, Map.class);
                server.updateDish(dishId, (String) json.get("name"));
                sendJson(exchange, 200, Map.of("message", "Блюдо обновлено"));
            }
            else if (method.equals("DELETE") && path.startsWith("/dishes/")) {
                String dishId = path.split("/")[2];
                server.deleteDish(dishId);
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
            }
            else {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
            }
        } catch (Exception e) {
            byte[] body = mapper.writeValueAsBytes(Map.of("error", e.getMessage()));
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(400, body.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(body);
            }
        }
    }

    private void sendJson(HttpExchange exchange, int status, Object obj) throws IOException {
        byte[] body = mapper.writeValueAsBytes(obj);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }

    private String getQueryParam(String query, String param) {
        if (query == null) return null;
        for (String s : query.split("&")) {
            if (s.startsWith(param + "=")) return s.split("=")[1];
        }
        return null;
    }
}