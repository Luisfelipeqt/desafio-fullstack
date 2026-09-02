package technical.softdesign.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_voto",
        indexes = @Index(name = "idx_voto_sessao", columnList = "sessao_votacao_id"),
        uniqueConstraints = @UniqueConstraint(name = "uk_voto_sessao_associado",
                columnNames = {"sessao_votacao_id", "associado_id"}))
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class VotoEntity extends Auditor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sessao_votacao_id", nullable = false)
    private SessaoVotacaoEntity sessaoVotacao;

    @Column(name = "associado_id", nullable = false, length = 11)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "voto", nullable = false, length = 3)
    private Voto voto;
}
