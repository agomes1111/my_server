package com.my.server.service;

import android.util.Log;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.Map;

public class MyServer extends NanoHTTPD {

    public MyServer(int port) throws IOException {
        super(port);
        start(SOCKET_READ_TIMEOUT, false);
        Log.i("MyServer", "🔥 Servidor iniciado em http://localhost:" + port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Method method = session.getMethod();
        Log.d("MyServer", "Requisição: " + method + " " + uri);

        // Endpoint simples
        if ("/hello".equals(uri)) {
            return newFixedLengthResponse("Olá do Android! 🚀");
        }

        // Endpoint com parâmetros GET
        if ("/soma".equals(uri)) {
            Map<String, String> params = session.getParms();
            int a = Integer.parseInt(params.getOrDefault("a", "0"));
            int b = Integer.parseInt(params.getOrDefault("b", "0"));
            return newFixedLengthResponse("Resultado: " + (a + b));
        }

        // Endpoint POST (exemplo JSON)
        if ("/echo".equals(uri) && method == Method.POST) {
            try {
                Map<String, String> files = new java.util.HashMap<>();
                session.parseBody(files);
                String body = files.get("postData");
                return newFixedLengthResponse(Response.Status.OK, "application/json", "{\"echo\": " + body + "}");
            } catch (Exception e) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Erro ao processar POST");
            }
        }

        // Rota padrão
        return newFixedLengthResponse("Servidor NanoHTTPD Android ativo! 🌐");
    }
}
