package com.gerador.pagamento.service;

import com.gerador.pagamento.DTO.LoginDTO;
import com.gerador.pagamento.exception.ClienteException;
import com.gerador.pagamento.model.Cliente;
import com.gerador.pagamento.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void salvar(Cliente cliente) {
        if (clienteRepository.findByCpf(cliente.getCpf()) != null) {
            throw new ClienteException("cliente ja cadastrado");
        }
        clienteRepository.save(cliente);
    }

    @Override
    public Cliente popular(LoginDTO loginDTO) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(loginDTO, cliente);
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return cliente;
    }

    @Override
    public Cliente validaCpf(String cpf) {
        Cliente cliente = (Cliente) clienteRepository.findByCpf(cpf);
        if (cliente == null) {
            throw new ClienteException("cliente nao encontrado");
        }
        return cliente;
    }
}
