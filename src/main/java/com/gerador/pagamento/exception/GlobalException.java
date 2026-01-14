package com.gerador.pagamento.exception;

import com.gerador.pagamento.util.RequisicaoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationExceptions(MethodArgumentNotValidException ex) {
        return new RequisicaoUtils().retornoRequisicao(HttpStatus.BAD_REQUEST, ex.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ClienteException.class)
    public ResponseEntity<Map<String, Object>> clienteException(ClienteException ex) {
        return new RequisicaoUtils().retornoRequisicao(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> genericException(Exception ex) {
        return new RequisicaoUtils().retornoRequisicao(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}
