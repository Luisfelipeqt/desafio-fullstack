package technical.softdesign.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import technical.softdesign.core.entities.SessaoVotacaoEntity;

import java.util.Optional;
import java.util.UUID;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacaoEntity, UUID> {

    Optional<SessaoVotacaoEntity> findByPautaId(UUID pautaId);

    boolean existsByPautaId(UUID pautaId);
}
