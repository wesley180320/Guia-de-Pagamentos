package com.gerador.pagamento.service;
import com.gerador.pagamento.DTO.LoginDTO;
import com.gerador.pagamento.model.Cliente;

public interface ClienteService {
    void salvar(Cliente cliente);
    Cliente validaCpf(String cpf);
    Cliente popular(LoginDTO loginDTO);
}
