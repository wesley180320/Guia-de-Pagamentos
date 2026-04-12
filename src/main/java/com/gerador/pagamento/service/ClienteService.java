package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.LoginDTO;
import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.model.Recebedor;
import com.gerador.pagamento.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void salvarCliente(LoginDTO loginDTO) {
        validarCliente(loginDTO);
        Cliente cliente = new Cliente();
        loginDTO.setSenha(passwordEncoder.encode(loginDTO.getSenha()));
        BeanUtils.copyProperties(loginDTO, cliente);
        Recebedor recebedor = new Recebedor();
        cliente.setRecebedor(recebedor);
        clienteRepository.save(cliente);
    }

    private void validarCliente(LoginDTO loginDTO) {
        Cliente clienteExistente = (Cliente) clienteRepository.findByCpf(loginDTO.getCpf());
        if (clienteExistente != null) {
            throw new ClienteException("Erro ao salvar o cliente: CPF já existe");
        }
    }
}
