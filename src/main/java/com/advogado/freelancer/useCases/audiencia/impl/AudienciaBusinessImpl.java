package com.advogado.freelancer.useCases.audiencia.impl;

import com.advogado.freelancer.entities.Audiencia;
import com.advogado.freelancer.entities.Clientes;
import com.advogado.freelancer.entities.Processo;
import com.advogado.freelancer.entities.Usuario;
import com.advogado.freelancer.frameWork.annotions.Business;
import com.advogado.freelancer.frameWork.utils.SenacException;
import com.advogado.freelancer.frameWork.utils.Status;
import com.advogado.freelancer.frameWork.utils.StringUtil;
import com.advogado.freelancer.useCases.audiencia.AudienciaBusiness;
import com.advogado.freelancer.useCases.audiencia.domains.AudienciaRequestDom;
import com.advogado.freelancer.useCases.audiencia.domains.AudienciaResponseDom;
import com.advogado.freelancer.useCases.audiencia.impl.mappers.AudienciaMapper;
import com.advogado.freelancer.useCases.audiencia.impl.repositorys.AudienciaClientesRespository;
import com.advogado.freelancer.useCases.audiencia.impl.repositorys.AudienciaProcessoRepository;
import com.advogado.freelancer.useCases.audiencia.impl.repositorys.AudienciaRepository;
import com.advogado.freelancer.useCases.audiencia.impl.repositorys.AudienciaUsuarioRepository;
import com.advogado.freelancer.useCases.clientes.domanis.ClientesResponseDom;
import com.advogado.freelancer.useCases.clientes.impl.mappers.ClientesMapper;
import com.advogado.freelancer.useCases.usuarios.impl.repositorys.UsuarioAudienciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Business
public class AudienciaBusinessImpl implements AudienciaBusiness {

    @Autowired
    private AudienciaRepository audienciaRepository;

    @Autowired
    private AudienciaClientesRespository audienciaClientesRespository;

    @Autowired
    private AudienciaProcessoRepository audienciaProcessoRepository;
    @Autowired
    private AudienciaUsuarioRepository audienciaUsuarioRepository;
    @Autowired
    private UsuarioAudienciaRepository usuarioAudienciaRepository;
    @Override
    public AudienciaResponseDom criarAudiencia(AudienciaRequestDom audienciaRequestDom) throws Exception {
        this.validacaoManutencaoAudiencia(audienciaRequestDom);

        Optional<Clientes> cliente = audienciaClientesRespository.findById(audienciaRequestDom.getClienteId());
        if(!cliente.isPresent()){
            throw new SenacException("Cliente não encontrado");
        }

        Optional<Processo> processo = audienciaProcessoRepository.findById(audienciaRequestDom.getProcessoId());
        if (!processo.isPresent()){
            throw new SenacException("Processo não encontrado!");
        }
        
        Optional<Usuario> usuario = audienciaUsuarioRepository.findById(audienciaRequestDom.getUsuarioId());
        if(!usuario.isPresent()){
            throw new SenacException("Usuario não encontrado");
        }

        Audiencia audienciaRetorno = audienciaRepository.save(AudienciaMapper
                .audienciaResquestDomToAudiencia(audienciaRequestDom, cliente.get(), processo.get(), usuario.get()));


        return AudienciaMapper.audienciaToAudienciaResponseDom(audienciaRetorno);
    }

    @Override
    public List<AudienciaResponseDom> carregarAudiencia() {
        List<Audiencia> audienciaList = audienciaRepository.findAll();

        List<AudienciaResponseDom> out = audienciaList.stream().map(AudienciaMapper::audienciaToAudienciaResponseDom)
                .collect(Collectors.toList());
        return out;
    }

    @Override
    public AudienciaResponseDom atualizarAudiencia(Long id, AudienciaRequestDom audienciaRequestDom) throws SenacException {
        this.validacaoManutencaoAudiencia(audienciaRequestDom);

        Optional<Clientes> cliente = audienciaClientesRespository.findById(audienciaRequestDom.getClienteId());
        if(!cliente.isPresent()){
            throw new SenacException("Cliente não encontrado!");
        }

        Optional<Processo> processo = audienciaProcessoRepository.findById(audienciaRequestDom.getProcessoId());
        if (!processo.isPresent()){
            throw new SenacException("Processo não encontrado!");
        }


        Optional<Audiencia> audiencia = audienciaRepository.findById(id).map(record ->{
                    record.setCliente(cliente.get());
                    record.setStatus(audienciaRequestDom.getStatus());
                    record.setHora(audienciaRequestDom.getHora());
                    record.setData(audienciaRequestDom.getData());
                    record.setLocal(audienciaRequestDom.getLocal());

                    return audienciaRepository.save(record);
                });

        if(!audiencia.isPresent()){
            throw new SenacException("Audiencia não informada!");
        }

        AudienciaResponseDom out = AudienciaMapper.audienciaToAudienciaResponseDom(audiencia.get());
        return out;
    }

    @Override
    public void deletarAuciencia(Long id) {
        audienciaRepository.deleteById(id);
    }

    @Override
    public AudienciaResponseDom carregarAudienciaById(Long id) throws SenacException {
        Optional<Audiencia> optionalAudiencia = audienciaRepository.findById(id);


        if(!optionalAudiencia.isPresent()){
            throw new SenacException("Audiencia não informada!");
        }

        Audiencia audiencia = optionalAudiencia.get();

        AudienciaResponseDom out = AudienciaMapper.audienciaToAudienciaResponseDom(audiencia);

        return out;
    }

    @Override
    public List<AudienciaResponseDom> carregarAudienciaByUsuarioId(Long id) throws SenacException {
        List<Audiencia> optionalAudiencia = usuarioAudienciaRepository.carregarAudienciaByUsuarioId(id);

        List<AudienciaResponseDom> out = optionalAudiencia
                .stream()
                .map(AudienciaMapper:: audienciaToAudienciaResponseDom)
                .collect(Collectors.toList());

        return out;
    }


    private void validacaoManutencaoAudiencia(AudienciaRequestDom audiencia) throws SenacException{

        if (StringUtil.validarString(audiencia.getData())){
           throw new SenacException("Não foi informada a data!");
        }

        if (StringUtil.validarString(audiencia.getHora())){
          throw new SenacException("Não foi informada a hora!");
        }

        if (StringUtil.validarString(audiencia.getLocal())){
            throw new SenacException("Não foi informado o local!");
        }

        if (audiencia.getClienteId() == null || audiencia.getClienteId()<=0){
            throw new SenacException("Cliente não informado!");
        }

        if (audiencia.getProcessoId() == null || audiencia.getProcessoId()<=0){
            throw new SenacException("Processo não informado");
        }

        if (audiencia.getStatus() == null){
            throw new SenacException("Status não informado");
        }
    }

}
