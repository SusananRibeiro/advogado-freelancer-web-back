package com.advogado.freelancer.useCases.clientes.impl.mappers;
import com.advogado.freelancer.entities.Clientes;
import com.advogado.freelancer.entities.Usuario;
import com.advogado.freelancer.useCases.clientes.domanis.ClientesRequestDom;
import com.advogado.freelancer.useCases.clientes.domanis.ClientesResponseDom;

public class ClientesMapper {

    // Request - Método para converter ClientesRequestDom para Clientes
    public static Clientes clientesRequestDomToClientes(ClientesRequestDom clientesRequestDom, Usuario usuario){
        Clientes out = new Clientes();

        out.setNomeCompleto(clientesRequestDom.getNomeCompleto());
        out.setCpf(clientesRequestDom.getCpf());
        out.setDataNascimento(clientesRequestDom.getDataNascimento()); // Convertendo a data de nascimento
        out.setRua(clientesRequestDom.getRua());
        out.setNumero(clientesRequestDom.getNumero());
        out.setBairro(clientesRequestDom.getBairro());
        out.setCidade(clientesRequestDom.getCidade());
        out.setUf(clientesRequestDom.getUf());
        out.setCep(clientesRequestDom.getCep());
        out.setPais(clientesRequestDom.getPais());
        out.setTelefone(clientesRequestDom.getTelefone());
        out.setEmail(clientesRequestDom.getEmail());
        out.setComplemento(clientesRequestDom.getComplemento());
        out.setStatus(clientesRequestDom.getStatus());
        out.setUsuarioId(usuario);
        return out;
    }
    // Response
    public static ClientesResponseDom clientesToClientesResponseDom(Clientes clientes){
        ClientesResponseDom out = new ClientesResponseDom();
        out.setId(clientes.getId());
        out.setNomeCompleto(clientes.getNomeCompleto());
        out.setCpf(clientes.getCpf());
        out.setDataNascimento(clientes.getDataNascimento());
        out.setRua(clientes.getRua());
        out.setNumero(clientes.getNumero());
        out.setBairro(clientes.getBairro());
        out.setCidade(clientes.getCidade());
        out.setUf(clientes.getUf());
        out.setCep(clientes.getCep());
        out.setPais(clientes.getPais());
        out.setTelefone(clientes.getTelefone());
        out.setEmail(clientes.getEmail());
        out.setComplemento(clientes.getComplemento());
        out.setStatus(clientes.getStatus());
        out.setUsuarioId(clientes.getUsuarioId().getId());
        out.setNomeCompletoUsuario(clientes.getUsuarioId().getNomeCompleto());
        out.setEmailUsuario(clientes.getUsuarioId().getEmail());
        return out;
    }

}
