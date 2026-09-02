package technical.softdesign.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "tb_pauta")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class PautaEntity extends Auditor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "titulo_pauta", nullable = false, length = 60)
    private String tituloPauta;


    @Column(name = "descricao_pauta", length = 500)
    private String descricaoPauta;
}
