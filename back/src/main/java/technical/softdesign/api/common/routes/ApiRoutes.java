package technical.softdesign.api.common.routes;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ApiRoutes {

    public static final String API = "/api";
    public static final String VERSAO = "/v1";

    public static final String PAUTAS = API + VERSAO + "/pautas";
    public static final String SESSAO = "/{id}/sessao";
    public static final String VOTOS = "/{id}/votos";
    public static final String RESULTADO = "/{id}/resultado";

}
