package com.gerador.pagamento;

import com.gerador.pagamento.controller.BoletoController;
import com.gerador.pagamento.model.Boleto;
import com.gerador.pagamento.service.BoletoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BoletoControllerTest {

    @Mock
    private BoletoService boletoService;

    @InjectMocks
    private BoletoController boletoController;

    private Pageable pageable;
    private Page<Boleto> boletoPage;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        Boleto boleto = new Boleto();

        boletoPage = new PageImpl<>(
                Collections.singletonList(boleto),
                pageable,
                1
        );
    }

    @Test
    void deveBuscarPaginadaBoletoPorIdComSucesso() {

        Long idCliente = 1L;

        when(boletoService.buscaPaginadaPorId(idCliente, pageable))
                .thenReturn(boletoPage);

        ResponseEntity<Page<Boleto>> response =
                boletoController.buscaPaginadaBoletoPorId(idCliente, pageable);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(boletoPage, response.getBody());

        verify(boletoService, times(1))
                .buscaPaginadaPorId(idCliente, pageable);
    }
}