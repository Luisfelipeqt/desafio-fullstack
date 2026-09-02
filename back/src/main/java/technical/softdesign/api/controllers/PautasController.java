package technical.softdesign.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import technical.softdesign.api.common.dtos.AbrirSessaoRequest;
import technical.softdesign.api.common.dtos.CriarPautaRequest;
import technical.softdesign.api.common.dtos.PautaResponse;
import technical.softdesign.api.common.dtos.ResultadoVotacaoResponse;
import technical.softdesign.api.common.dtos.SessaoVotacaoResponse;
import technical.softdesign.api.common.dtos.VotoRequest;
import technical.softdesign.core.service.PautaService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class PautasController implements IPautasController {

    private final PautaService pautaService;

    @Override
    public ResponseEntity<PautaResponse> criar(CriarPautaRequest request) {
        return ResponseEntity.status(CREATED).body(pautaService.criar(request));
    }

    @Override
    public ResponseEntity<SessaoVotacaoResponse> abrirSessao(UUID id, AbrirSessaoRequest request) {
        return ResponseEntity.status(CREATED).body(pautaService.abrirSessao(id, request));
    }

    @Override
    public ResponseEntity<Void> votar(UUID id, VotoRequest request) {
        pautaService.votar(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ResultadoVotacaoResponse> resultado(UUID id) {
        return ResponseEntity.ok(pautaService.resultado(id));
    }
}
