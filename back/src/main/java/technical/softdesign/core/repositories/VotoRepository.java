package technical.softdesign.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import technical.softdesign.core.entities.Voto;
import technical.softdesign.core.entities.VotoEntity;

import java.util.UUID;

public interface VotoRepository extends JpaRepository<VotoEntity, UUID> {

    boolean existsBySessaoVotacaoIdAndAssociadoId(UUID sessaoVotacaoId, String associadoId);

    long countBySessaoVotacaoIdAndVoto(UUID sessaoVotacaoId, Voto voto);
}
