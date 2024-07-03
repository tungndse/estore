package com.coldev.estore.config.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.common.enumerate.EStoreErrorType;
import com.coldev.estore.config.exception.general.BadRequestException;
import com.coldev.estore.config.exception.general.DataNotFoundException;
import com.coldev.estore.domain.dto.ResponseObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ITEM_NOT_FOUND_EXCEPTION = "ItemNotFoundException";
    public static final String ITEM_UNAVAILABLE_EXCEPTION = "ItemUnavailableException";
    public static final String DUPLICATED_EXCEPTION = "DuplicatedException";
    public static final String ACTION_NOT_ALLOWED_EXCEPTION = "ActionNotAllowedException";
    public static final String INVALID_INPUT_EXCEPTION = "InvalidInputException";
    public static final String INVALID_OWNER_SHIP_EXCEPTION = "InvalidOwnerShipException";
    public static final String ITEM_DEPLETED_EXCEPTION = "ItemDepletedException";
    public static final String ITEM_MARKED_DELETED_EXCEPTION = "ItemMarkedDeletedException";
    public static final String CREATION_FAILED_EXCEPTION = "CreationFailedException";
    public static final String CUSTOM_API_EXCEPTION = "CustomApiException";

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        ResponseObject<List<String>> responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.BAD_REQUEST);
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted().toList();
        responseObject.setData(errors);
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FirebaseAuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> firebaseAuthExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.FIREBASE_TOKEN_ERROR);
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> exceptionHandler(Exception ex) {
        String exceptionClassName = ex.getClass().getSimpleName();
        return new ResponseEntity<>(
                this.getErrorResponse(ex),
                switch (exceptionClassName) {
                    case ITEM_NOT_FOUND_EXCEPTION -> HttpStatus.NOT_FOUND;
                    case ITEM_UNAVAILABLE_EXCEPTION -> HttpStatus.BAD_REQUEST;
                    case DUPLICATED_EXCEPTION -> HttpStatus.BAD_REQUEST;
                    case ACTION_NOT_ALLOWED_EXCEPTION -> HttpStatus.BAD_REQUEST;
                    case INVALID_INPUT_EXCEPTION -> HttpStatus.BAD_REQUEST;
                    case INVALID_OWNER_SHIP_EXCEPTION -> HttpStatus.FORBIDDEN;
                    case ITEM_DEPLETED_EXCEPTION -> HttpStatus.BAD_REQUEST;
                    case ITEM_MARKED_DELETED_EXCEPTION -> HttpStatus.NOT_FOUND;
                    case CREATION_FAILED_EXCEPTION -> HttpStatus.BAD_REQUEST;
                    case CUSTOM_API_EXCEPTION -> HttpStatus.INTERNAL_SERVER_ERROR;

                    default -> HttpStatus.INTERNAL_SERVER_ERROR;
                }
        );
    }

    private ResponseObject<?> getErrorResponse(Exception ex) {
        String exceptionClassName = ex.getClass().getSimpleName();

        return ResponseObject.builder()
                .message(ex.getMessage())
                .data(ex.getStackTrace())
                .errorType(switch (exceptionClassName) {
                    case ITEM_NOT_FOUND_EXCEPTION -> EStoreErrorType.NOT_FOUND;
                    case DUPLICATED_EXCEPTION -> EStoreErrorType.DUPLICATED;
                    case ACTION_NOT_ALLOWED_EXCEPTION -> EStoreErrorType.ACTION_NOT_ALLOWED;
                    case INVALID_INPUT_EXCEPTION -> EStoreErrorType.INVALID_INPUT;
                    case INVALID_OWNER_SHIP_EXCEPTION -> EStoreErrorType.INVALID_OWNERSHIP;
                    case ITEM_DEPLETED_EXCEPTION -> EStoreErrorType.QUANTITY_DEPLETED;
                    case ITEM_MARKED_DELETED_EXCEPTION -> EStoreErrorType.DELETED;
                    case CREATION_FAILED_EXCEPTION -> EStoreErrorType.CREATION_FAILED;
                    case CUSTOM_API_EXCEPTION -> EStoreErrorType.GOOGLE_API;

                    default -> EStoreErrorType.UNDEFINED;
                })
                .build();
    }

    @ExceptionHandler({TokenExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> tokenExpiredExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.TOKEN_EXPIRED);
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JWTDecodeException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseObject> JWTDecodeExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.ACCESS_DENIED);
        return new ResponseEntity<>(responseObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({JWTVerificationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseObject> JWTVerificationExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.ACCESS_DENIED);
        return new ResponseEntity<>(responseObject, HttpStatus.FORBIDDEN);
    }

    /*@ExceptionHandler({InvalidEmailException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseObject> invalidEmailExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.WRONG_CREDENTIALS_INFORMATION);
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }*/

    @ExceptionHandler({DataNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseObject> dataNotFoundExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.DATA_NOT_FOUND);
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> internalServerException() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestException ex) {
        ResponseObject<String> responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.BAD_REQUEST);
        responseObject.setData(ex.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    /*@ExceptionHandler(VNPAYNotSupported.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity<ResponseObject<String>> VNPAYNotSupportedHandler(VNPAYNotSupported ex) {
        ResponseObject<String> responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.VNPAY_NOT_SUPPORTED);
        responseObject.setData(ex.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_GATEWAY);
    }*/

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> missingServletRequestParameterExceptionExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.BAD_REQUEST);
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FirebaseMessagingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> firebaseMessagingExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.FIREBASE_MESSAGING_FAILURE);
        return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseObject> accessDeniedExceptionHandler() {
        ResponseObject responseObject = new ResponseObject<>();
        responseObject.setMessage(MessageDictionary.ACCESS_DENIED);
        return new ResponseEntity<>(responseObject, HttpStatus.FORBIDDEN);
    }
}
