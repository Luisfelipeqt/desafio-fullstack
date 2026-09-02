package technical.softdesign;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import technical.softdesign.core.repositories.PautaRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class PautaVotacaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void deveCadastrarPautaAbrirSessaoVotarEContabilizarResultado() throws Exception {
        var pautaJson = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"tituloPauta":"Reforma do estatuto","descricaoPauta":"Discussao"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        var pautaId = UUID.fromString(objectMapper.readTree(pautaJson).get("id").asString());
        assertThat(pautaRepository.findById(pautaId)).isPresent();

        mockMvc.perform(post("/api/v1/pautas/{id}/sessao", pautaId)
                        .contentType(APPLICATION_JSON)
                        .content("{\"duracaoMinutos\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataFechamento").exists());

        mockMvc.perform(post("/api/v1/pautas/{id}/votos", pautaId)
                        .contentType(APPLICATION_JSON)
                        .content("{\"associadoId\":\"52998224725\",\"voto\":\"SIM\"}"))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/pautas/{id}/votos", pautaId)
                        .contentType(APPLICATION_JSON)
                        .content("{\"associadoId\":\"52998224725\",\"voto\":\"NAO\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Associado ja votou nesta pauta."));

        mockMvc.perform(get("/api/v1/pautas/{id}/resultado", pautaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votosSim").value(1))
                .andExpect(jsonPath("$.votosNao").value(0))
                .andExpect(jsonPath("$.resultado").value("APROVADA"));
    }

    @Test
    void deveRetornar404QuandoPautaNaoExiste() throws Exception {
        mockMvc.perform(get("/api/v1/pautas/{id}/resultado", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(greaterThan(0)));
    }
}