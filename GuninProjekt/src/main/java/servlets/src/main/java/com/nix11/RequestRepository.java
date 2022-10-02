package com.nix11;

import java.util.LinkedList;
import java.util.List;

public class RequestRepository {
    private final List<Request> requests;
    private static RequestRepository instance;

    private RequestRepository() {
        requests = new LinkedList<>();
    }

    public static RequestRepository getInstance() {
        if (instance == null) {
            instance = new RequestRepository();
        }
        return instance;
    }

    public void save(Request request) {
        requests.add(request);
    }

    public List<Request> getAll() {
        return requests;
    }
}
