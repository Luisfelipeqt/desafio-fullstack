package technical.softdesign.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import technical.softdesign.api.common.dtos.AbrirSessaoRequest;
import technical.softdesign.api.common.dtos.CriarPautaRequest;
import technical.softdesign.api.common.dtos.PautaResponse;
import technical.softdesign.api.common.dtos.ResultadoVotacaoResponse;
import technical.softdesign.api.common.dtos.SessaoVotacaoResponse;
import technical.softdesign.api.common.dtos.VotoRequest;
import technical.softdesign.core.entities.PautaEntity;
import technical.softdesign.core.entities.SessaoVotacaoEntity;
import technical.softdesign.core.entities.VotoEntity;
import technical.softdesign.core.exceptions.NegocioException;
import technical.softdesign.core.exceptions.RecursoNaoEncontradoException;
import technical.softdesign.core.repositories.PautaRepository;
import technical.softdesign.core.repositories.SessaoVotacaoRepository;
import technical.softdesign.core.repositories.VotoRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static technical.softdesign.api.common.dtos.Resultado.APROVADA;
import static technical.softdesign.api.common.dtos.Resultado.EMPATE;
import static technical.softdesign.api.common.dtos.Resultado.REPROVADA;
import static technical.softdesign.core.entities.Voto.NAO;
import static technical.softdesign.core.entities.Voto.SIM;

@Slf4j
@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final VotoRepository votoRepository;

    @Value("${app.votacao.duracao-padrao-minutos}")
    private int duracaoPadraoMinutos;

    public PautaResponse criar(CriarPautaRequest request) {
        var pauta = new PautaEntity();
        pauta.setTituloPauta(request.tituloPauta());
        pauta.setDescricaoPauta(request.descricaoPauta());
        pauta = pautaRepository.save(pauta);
        log.info("Pauta criada. id={}", pauta.getId());
        return PautaResponse.from(pauta);
    }

    public SessaoVotacaoResponse abrirSessao(UUID pautaId, AbrirSessaoRequest request) {
        var pauta = buscarPauta(pautaId);
        if (sessaoVotacaoRepository.existsByPautaId(pautaId)) {
            throw new NegocioException("Pauta ja possui uma sessao de votacao.");
        }
        var duracaoMinutos = request != null && request.duracaoMinutos() != null
                ? request.duracaoMinutos()
                : duracaoPadraoMinutos;
        if (duracaoMinutos <= 0) {
            throw new NegocioException("Duracao da sessao deve ser maior que zero.");
        }

        var abertura = LocalDateTime.now();
        var sessao = new SessaoVotacaoEntity();
        sessao.setPauta(pauta);
        sessao.setDataAbertura(abertura);
        sessao.setDataFechamento(abertura.plusMinutes(duracaoMinutos));
        sessao = sessaoVotacaoRepository.save(sessao);
        log.info("Sessao de votacao aberta. pautaId={} sessaoId={} fechamento={}",
                pautaId, sessao.getId(), sessao.getDataFechamento());
        return new SessaoVotacaoResponse(sessao.getId(), pautaId,
                toInstant(sessao.getDataAbertura()), toInstant(sessao.getDataFechamento()));
    }

    public void votar(UUID pautaId, VotoRequest request) {
        var sessao = buscarSessao(pautaId);
        if (!sessao.isAberta()) {
            throw new NegocioException("Sessao de votacao encerrada.");
        }

        var associadoId = apenasDigitos(request.associadoId());
        if (votoRepository.existsBySessaoVotacaoIdAndAssociadoId(sessao.getId(), associadoId)) {
            throw new NegocioException("Associado ja votou nesta pauta.");
        }

        var voto = new VotoEntity();
        voto.setSessaoVotacao(sessao);
        voto.setAssociadoId(associadoId);
        voto.setVoto(request.voto());
        votoRepository.save(voto);
        log.info("Voto registrado. pautaId={} sessaoId={} voto={}", pautaId, sessao.getId(), request.voto());
    }

    public ResultadoVotacaoResponse resultado(UUID pautaId) {
        var pauta = buscarPauta(pautaId);
        var sessao = buscarSessao(pautaId);
        var votosSim = votoRepository.countBySessaoVotacaoIdAndVoto(sessao.getId(), SIM);
        var votosNao = votoRepository.countBySessaoVotacaoIdAndVoto(sessao.getId(), NAO);
        var resultado = votosSim > votosNao ? APROVADA
                : votosNao > votosSim ? REPROVADA
                : EMPATE;
        return new ResultadoVotacaoResponse(pautaId, pauta.getTituloPauta(), votosSim, votosNao,
                !sessao.isAberta(), resultado);
    }

    private Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private String apenasDigitos(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private PautaEntity buscarPauta(UUID pautaId) {
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta nao encontrada."));
    }

    private SessaoVotacaoEntity buscarSessao(UUID pautaId) {
        return sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessao de votacao nao encontrada para esta pauta."));
    }
}
