import { request } from './client'

const BASE = '/api/v1/pautas'

export type Voto = 'SIM' | 'NAO'
export type Resultado = 'APROVADA' | 'REPROVADA' | 'EMPATE'

export interface CriarPautaRequest {
  tituloPauta: string
  descricaoPauta?: string
}

export interface PautaResponse {
  id: string
  tituloPauta: string
  descricaoPauta: string | null
}

export interface AbrirSessaoRequest {
  duracaoMinutos?: number
}

export interface SessaoVotacaoResponse {
  id: string
  pautaId: string
  dataAbertura: string
  dataFechamento: string
}

export interface VotoRequest {
  associadoId: string
  voto: Voto
}

export interface ResultadoVotacaoResponse {
  pautaId: string
  tituloPauta: string
  votosSim: number
  votosNao: number
  sessaoEncerrada: boolean
  resultado: Resultado
}

export function criarPauta(body: CriarPautaRequest) {
  return request<PautaResponse>(BASE, { method: 'POST', body: JSON.stringify(body) })
}

export function abrirSessao(pautaId: string, body: AbrirSessaoRequest) {
  return request<SessaoVotacaoResponse>(`${BASE}/${pautaId}/sessao`, {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

export function registrarVoto(pautaId: string, body: VotoRequest) {
  return request<void>(`${BASE}/${pautaId}/votos`, { method: 'POST', body: JSON.stringify(body) })
}

export function buscarResultado(pautaId: string) {
  return request<ResultadoVotacaoResponse>(`${BASE}/${pautaId}/resultado`)
}
