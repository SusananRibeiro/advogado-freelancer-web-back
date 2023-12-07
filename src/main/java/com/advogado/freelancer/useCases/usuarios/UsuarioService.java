package com.advogado.freelancer.useCases.usuarios;
import com.advogado.freelancer.entities.Usuario;
import com.advogado.freelancer.frameWork.utils.SenacException;
import com.advogado.freelancer.useCases.usuarios.domanis.UsuarioRequestDom;
import com.advogado.freelancer.useCases.usuarios.domanis.UsuarioResponseDom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsuarioService {
    List<UsuarioResponseDom> carregarUsuarios();
    UsuarioResponseDom criarUsuario(UsuarioRequestDom usuarioRequestDom) throws Exception;
    public Usuario fazerLogin(String email, String senha);
    UsuarioResponseDom carregarUsuarioById(Long id) throws SenacException;
}
