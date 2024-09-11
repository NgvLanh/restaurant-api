package org.edu.restaurantapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse <T> {
    Integer code;
    Boolean status;
    String message;
    T data;

    // Static method to create a success response with 200 code
    public static <T> ApiResponse<T> SUCCESS(T data) {
        return new ApiResponse<>(200, true, "success", data);
    }

    // Static method to create a created response with 201 code
    public static <T> ApiResponse<T> CREATED(T data) {
        return new ApiResponse<>(201, true, "created", data);
    }

    // Static method to create a bad request response with 400 code
    public static <T> ApiResponse<T> BAD_REQUEST() {
        return new ApiResponse<>(400, false, "bad request", null);
    }

    // Static method to create an unauthorized response with 401 code
    public static <T> ApiResponse<T> UNAUTHORIZED(String message) {
        return new ApiResponse<>(401, false, message, null);
    }

    // Static method to create a forbidden response with 403 code
    public static <T> ApiResponse<T> FORBIDDEN() {
        return new ApiResponse<>(403, false, "forbidden", null);
    }

    // Static method to create a not found response with 404 code
    public static <T> ApiResponse<T> NOT_FOUND() {
        return new ApiResponse<>(404, false, "not found", null);
    }

    // Static method to create a server error response with 500 code
    public static <T> ApiResponse<T> SERVER_ERROR() {
        return new ApiResponse<>(500, false, "internal server error", null);
    }
}
