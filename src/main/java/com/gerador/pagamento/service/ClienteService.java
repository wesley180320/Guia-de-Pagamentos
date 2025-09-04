package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.ClienteDTO;
import com.gerador.pagamento.DTO.LoginDTO;
import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.model.Recebedor;
import com.gerador.pagamento.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public byte[] gerarPdf(ClienteDTO clienteDTO, Cliente cliente) {

        Cliente clienteLogado = (Cliente) clienteRepository.findByClienteComRecebedor(cliente.getCpf());

        if (clienteDTO == null || clienteLogado == null) {
            throw new ClienteException("Cliente não pode ser nulo");
        }

        clienteLogado.getRecebedor().setCidade(clienteDTO.getRecebedor().getCidade());
        clienteLogado.getRecebedor().setNome(clienteDTO.getRecebedor().getNome());

        BeanUtils.copyProperties(clienteDTO, clienteLogado, "idCliente", "recebedor", "cpf");
        byte[] pdfByte = GerarGuia.gerarGuiaPdf(
                clienteLogado.getProprietario(),
                clienteLogado.getCpf(),
                clienteLogado.getEndereco(),
                new BigDecimal(String.valueOf(clienteLogado.getValor())),
                LocalDate.now().plusDays(5),
                "chave-pix-exemplo",
                clienteLogado.getRecebedor().getNome(),
                clienteLogado.getRecebedor().getCidade());

        if (pdfByte == null) {
            throw new ClienteException("Erro ao gerar pdf");
        }

        try {
            clienteLogado.getRecebedor().setCliente(clienteLogado);
            clienteRepository.save(clienteLogado);
            return pdfByte;
        } catch (ConstraintViolationException e) {
            throw new ClienteException("Erro ao salvar o cliente: violação de constraint", e);
        } catch (DataAccessException e) {
            throw new ClienteException("Erro ao salvar o cliente no banco de dados", e);
        } catch (Exception e) {
            throw new ClienteException("Erro inesperado ao salvar o cliente", e);
        }
    }

    public void salvarCliente(LoginDTO loginDTO){
        Cliente clienteExistente = (Cliente) clienteRepository.findByCpf(loginDTO.getCpf());
        if(clienteExistente != null){
            throw new ClienteException("Erro ao salvar o cliente: CPF já existe");
        }
        Cliente cliente = new Cliente();
        loginDTO.setSenha(passwordEncoder.encode(loginDTO.getSenha()));
        BeanUtils.copyProperties(loginDTO, cliente);
        Recebedor recebedor = new Recebedor();
        cliente.setRecebedor(recebedor);
        clienteRepository.save(cliente);
    }

}
