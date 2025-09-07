package httpserver;

import httpserver.services.HomeService;
import httpserver.services.IResourceService;

import java.util.Map;
import java.util.TreeMap;

public class Application {
    public static final Map<String, IResourceService> resourceMap = new TreeMap<>();

    public void init() {
        resourceMap.put("/home", new HomeService());
    }
}
