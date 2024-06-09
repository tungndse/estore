package com.coldev.estore.domain.dto;


import com.coldev.estore.common.enumerate.EstoreErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;

@Data
@AllArgsConstructor
public class ResponseObject<T> {

    private String message;
    private int totalItems;
    private Pagination pagination;

    private T data;
    private EstoreErrorType errorType;

    public ResponseObject() {
        this.data = (T) Collections.EMPTY_LIST;
    }

    public ResponseObject(ResponseObjectBuilder<T> builder) {
        message = builder.message;
        totalItems = builder.totalItems;

        data = builder.data != null ? builder.data : (T) Collections.EMPTY_LIST;
        pagination = builder.pagination;
        errorType = builder.errorType;

    }

    public static ResponseObjectBuilder builder() {
        return new ResponseObjectBuilder<>();
    }

    public static class ResponseObjectBuilder<T> {
        private String message;
        private int totalItems;
        private Pagination pagination;
        private T data;
        private EstoreErrorType errorType;

        public ResponseObjectBuilder() {
            this.data = (T) Collections.EMPTY_LIST;
        }

        public ResponseObjectBuilder<T> message(String message) {
            this.message = message;
            return this;
        }
        public ResponseObjectBuilder<T> totalItems(int totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public ResponseObjectBuilder<T> pagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public ResponseObjectBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseObjectBuilder<T> errorType(EstoreErrorType errorType) {
            this.errorType = errorType;
            return this;
        }

        public ResponseObject<T> build() {
            return new ResponseObject<>(this);
        }

    }
}
