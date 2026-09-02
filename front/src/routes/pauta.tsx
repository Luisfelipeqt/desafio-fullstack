import { useState } from 'react'
import type { FormEvent } from 'react'
import { createRoute, Link } from '@tanstack/react-router'
import { useMutation, useQuery } from '@tanstack/react-query'
import { IMaskInput } from 'react-imask'
import { abrirSessao, buscarResultado, registrarVoto } from '../api/pautas'
import type { Voto } from '../api/pautas'
import { ApiError } from '../api/client'
import { usePautasConhecidas } from '../api/pautasConhecidas'
import { useTempoRestante } from '../hooks/useTempoRestante'
import { rootRoute } from './root'

function formatarData(data: string) {
  return new Date(data).toLocaleString('pt-BR')
}

function PautaDetalhe() {
  const { pautaId } = pautaRoute.useParams()
  const { pautas } = usePautasConhecidas()
  const tituloConhecido = pautas.find((p) => p.id === pautaId)?.tituloPauta

  const [duracaoMinutos, setDuracaoMinutos] = useState('')
  const [associadoId, setAssociadoId] = useState('')
  const [voto, setVoto] = useState<Voto>('SIM')

  const sessaoMutation = useMutation({
    mutationFn: () =>
      abrirSessao(pautaId, {
        duracaoMinutos: duracaoMinutos ? Number(duracaoMinutos) : undefined,
      }),
  })

  const votoMutation = useMutation({
    mutationFn: () => registrarVoto(pautaId, { associadoId: associadoId.replace(/\D/g, ''), voto }),
  })

  const tempoRestante = useTempoRestante(sessaoMutation.data?.dataFechamento)

  const resultadoQuery = useQuery({
    queryKey: ['resultado', pautaId],
    queryFn: () => buscarResultado(pautaId),
    enabled: false,
    retry: false,
  })

  function handleAbrirSessao(event: FormEvent) {
    event.preventDefault()
    sessaoMutation.mutate()
  }

  function handleVotar(event: FormEvent) {
    event.preventDefault()
    votoMutation.mutate(undefined, {
      onSuccess: () => setAssociadoId(''),
    })
  }

  return (
    <div className="cover">
      <section className="center stack">
        <Link to="/">&larr; Voltar</Link>
        <h1>{tituloConhecido ?? 'Pauta'}</h1>
        <p>{pautaId}</p>

        <form className="card stack" onSubmit={handleAbrirSessao}>
          <h2>Abrir sessão de votação</h2>
          <div className="field">
            <label htmlFor="duracaoMinutos">Duração em minutos (opcional)</label>
            <input
              id="duracaoMinutos"
              type="number"
              min={1}
              value={duracaoMinutos}
              onChange={(e) => setDuracaoMinutos(e.target.value)}
              placeholder="padrão do servidor"
            />
          </div>
          <button type="submit" className="button" disabled={sessaoMutation.isPending}>
            {sessaoMutation.isPending ? 'Abrindo...' : 'Abrir sessão'}
          </button>
          {sessaoMutation.isError && (
            <p className="message error">
              {sessaoMutation.error instanceof ApiError
                ? sessaoMutation.error.message
                : 'Erro ao abrir sessão.'}
            </p>
          )}
          {sessaoMutation.isSuccess && tempoRestante && (
            <p className="message success">
              Sessão aberta até {formatarData(sessaoMutation.data.dataFechamento)}.{' '}
              {tempoRestante.encerrada
                ? 'Sessão encerrada.'
                : `Tempo restante: ${tempoRestante.formatado}`}
            </p>
          )}
        </form>

        <form className="card stack" onSubmit={handleVotar}>
          <h2>Registrar voto</h2>
          <div className="field">
            <label htmlFor="associadoId">CPF do associado</label>
            <IMaskInput
              id="associadoId"
              mask="000.000.000-00"
              value={associadoId}
              onAccept={(value: string) => setAssociadoId(value)}
              placeholder="000.000.000-00"
              required
            />
          </div>
          <div className="field">
            <label>Voto</label>
            <div className="cluster">
              <label>
                <input
                  type="radio"
                  name="voto"
                  checked={voto === 'SIM'}
                  onChange={() => setVoto('SIM')}
                />{' '}
                Sim
              </label>
              <label>
                <input
                  type="radio"
                  name="voto"
                  checked={voto === 'NAO'}
                  onChange={() => setVoto('NAO')}
                />{' '}
                Não
              </label>
            </div>
          </div>
          <button type="submit" className="button" disabled={votoMutation.isPending}>
            {votoMutation.isPending ? 'Enviando...' : 'Votar'}
          </button>
          {votoMutation.isError && (
            <p className="message error">
              {votoMutation.error instanceof ApiError ? votoMutation.error.message : 'Erro ao votar.'}
            </p>
          )}
          {votoMutation.isSuccess && <p className="message success">Voto registrado.</p>}
        </form>

        <div className="card stack">
          <h2>Resultado</h2>
          <button
            type="button"
            className="button"
            onClick={() => resultadoQuery.refetch()}
            disabled={resultadoQuery.isFetching}
          >
            {resultadoQuery.isFetching ? 'Consultando...' : 'Ver resultado'}
          </button>
          {resultadoQuery.isError && (
            <p className="message error">
              {resultadoQuery.error instanceof ApiError
                ? resultadoQuery.error.message
                : 'Erro ao consultar resultado.'}
            </p>
          )}
          {resultadoQuery.data && (
            <ul className="stack" role="list">
              <li>Sim: {resultadoQuery.data.votosSim}</li>
              <li>Não: {resultadoQuery.data.votosNao}</li>
              <li>Sessão encerrada: {resultadoQuery.data.sessaoEncerrada ? 'Sim' : 'Não'}</li>
              <li>Resultado: {resultadoQuery.data.resultado}</li>
            </ul>
          )}
        </div>
      </section>
    </div>
  )
}

export const pautaRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: '/pautas/$pautaId',
  component: PautaDetalhe,
})
