package httpserver;

import httpserver.parsing.Request;
import httpserver.services.IResourceService;
import httpserver.services.NotFoundService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler {

    final static Logger logger = LogManager.getLogger(RequestHandler.class);

    public void handle(Socket clientSocket) {
        try {
            // Поток для чтения данных от клиента
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            // Читаем пакет от клиента
            Request request = new Request(reader);

            while (true) {
                // Читаем пакет от клиента
                String message = reader.readLine();
                System.out.println(message);
                logger.debug(message);

                if (message.isEmpty()) {
                    logger.debug("end of request header");
                    OutputStream os = clientSocket.getOutputStream();
                    logger.debug("outputStream" + os);
                    IResourceService resourceService = Application.resourceMap.get(request.getResource());
                    if (resourceService != null) {
                        resourceService.service(request.getMethod(), request.getParams(), os);
                    } else {
                        new NotFoundService().service("GET", null, os);
                    }
                    os.flush();
                    logger.debug("outputStream" + os);
                    break;
                }
            }
        } catch (IOException e) {
            logger.atError().withThrowable(e);
        }

    }

}
