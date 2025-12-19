package com.itu.demo.test;

import com.itu.demo.annotations.GetMapping;
import com.itu.demo.annotations.RestApi;
import com.itu.demo.model.ApiResponse;
import com.itu.demo.model.Emp;
import java.util.ArrayList;
import java.util.List;

public class ApiTestController {
    
    @GetMapping("/api/emps")
    @RestApi
    public ApiResponse getAllEmps() {
        List<Emp> emps = new ArrayList<>();
        emps.add(new Emp("Alice", "IT", 25));
        emps.add(new Emp("Bob", "HR", 30));
        emps.add(new Emp("Charlie", "Finance", 28));
        return new ApiResponse(200, "Success", emps);
    }

    @GetMapping("/api/emp")
    @RestApi
    public ApiResponse getEmpById(String id) {
        if (id == null || id.isEmpty()) {
            return new ApiResponse(400, "Bad Request: id is required", null);
        }
        try {
            int empId = Integer.parseInt(id);
            Emp emp = new Emp("Employee #" + empId, "Department", 25);
            return new ApiResponse(200, "Success", emp);
        } catch (NumberFormatException e) {
            return new ApiResponse(400, "Bad Request: invalid id format", null);
        }
    }

    @GetMapping("/api/error")
    @RestApi
    public ApiResponse testError() {
        return new ApiResponse(500, "Internal Server Error", null);
    }

    @GetMapping("/api/notfound")
    @RestApi
    public ApiResponse testNotFound() {
        return new ApiResponse(404, "Not Found", null);
    }
}