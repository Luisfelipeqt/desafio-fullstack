# softdesign

API para gerenciamento de pautas e sessões de votação de uma cooperativa.

## Como executar

Suba o banco de dados (PostgreSQL) via Docker Compose:

```bash
docker-compose up -d
```

Depois rode a aplicação:

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Documentação Swagger em `http://localhost:8080/swagger-ui.html`.

## Rotas

| Método | Rota                          | Descrição                                   |
|--------|-------------------------------|----------------------------------------------|
| POST   | `/api/v1/pautas`              | Cadastra uma nova pauta                       |
| POST   | `/api/v1/pautas/{id}/sessao`  | Abre a sessão de votação da pauta (padrão de 1 minuto, configurável no corpo da requisição) |
| POST   | `/api/v1/pautas/{id}/votos`   | Registra o voto de um associado (CPF) na pauta |
| GET    | `/api/v1/pautas/{id}/resultado` | Contabiliza os votos e retorna o resultado  |
