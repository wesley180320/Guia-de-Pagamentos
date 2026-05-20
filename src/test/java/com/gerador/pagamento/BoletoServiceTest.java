package com.gerador.pagamento;

import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Boleto;
import com.gerador.pagamento.repository.BoletoRepository;
import com.gerador.pagamento.service.BoletoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BoletoServiceTest {

    @InjectMocks
    private BoletoServiceImpl boletoService;

    @Mock
    private BoletoRepository boletoRepository;

    @Test
    void deveLancarExcecaoQuandoListaDeBoletosEstiverVazia() {

        Long idCliente = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Boleto> pageVazia = Page.empty();

        when(boletoRepository.findByClienteIdCliente(idCliente, pageable))
                .thenReturn(pageVazia);

        ClienteException exception = assertThrows(
                ClienteException.class,
                () -> boletoService.buscaPaginadaPorId(idCliente, pageable)
        );

        assertEquals("Erro lista de boletos vazia", exception.getMessage());


        verify(boletoRepository, times(1))
                .findByClienteIdCliente(idCliente, pageable);
    }

    @Test
    void deveLancarExcecaoQuandoRepositorioRetornarNull() {

        Long idCliente = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(boletoRepository.findByClienteIdCliente(idCliente, pageable))
                .thenReturn(null);

        ClienteException exception = assertThrows(
                ClienteException.class,
                () -> boletoService.buscaPaginadaPorId(idCliente, pageable)
        );

        assertEquals("Erro lista de boletos vazia", exception.getMessage());

        verify(boletoRepository, times(1))
                .findByClienteIdCliente(idCliente, pageable);
    }
}
