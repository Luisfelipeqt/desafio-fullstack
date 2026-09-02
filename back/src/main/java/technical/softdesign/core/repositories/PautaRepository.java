package technical.softdesign.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import technical.softdesign.core.entities.PautaEntity;

import java.util.UUID;

public interface PautaRepository extends JpaRepository<PautaEntity, UUID> {
}
