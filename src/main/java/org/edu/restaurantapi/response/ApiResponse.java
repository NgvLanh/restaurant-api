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
public class ApiResponse<T> {
    Integer code;
    Boolean status;
    Object message;
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
    public static <T> ApiResponse<T> BAD_REQUEST(String message) {
        return new ApiResponse<>(400, false, message, null);
    }

    // Static method to create an unauthorized response with 401 code
    public static <T> ApiResponse<T> UNAUTHORIZED(String message) {
        return new ApiResponse<>(401, false, message, null);
    }

    // Static method to create a forbidden response with 403 code
    public static <T> ApiResponse<T> FORBIDDEN(String message) {
        return new ApiResponse<>(403, false, message, null);
    }

    // Static method to create a not found response with 404 code
    public static <T> ApiResponse<T> NOT_FOUND(String message) {
        return new ApiResponse<>(404, false, message, null);
    }

    // Static method to create a server error response with 500 code
    public static <T> ApiResponse<T> SERVER_ERROR(String message) {
        return new ApiResponse<>(500, false, message, null);
    }

    public static <T> ApiResponse<T> BAD_REQUEST_VALIDATION(T message) {
        return new ApiResponse<>(400, false, message, null);
    }

    // Static method to create a success response with 200 code
    public static <T> ApiResponse<T> DELETE(String message) {
        return new ApiResponse<>(200, true, message, null);
    }

    // Static method to create a cancel response with appropriate status code
    public static <T> ApiResponse<T> CANCEL(T data) {
        return new ApiResponse<>(202, false, "cancelled", data);
    }

}
