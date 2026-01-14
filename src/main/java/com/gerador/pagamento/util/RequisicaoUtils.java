package com.gerador.pagamento.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequisicaoUtils {

    public ResponseEntity retornoRequisicao(HttpStatus httpStatus, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", httpStatus.value());
        body.put("error", mensagem);
        return ResponseEntity.status(httpStatus.value()).body(body);
    }

}
