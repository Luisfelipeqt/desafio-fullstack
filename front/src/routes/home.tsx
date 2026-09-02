import { useState } from 'react'
import type { FormEvent } from 'react'
import { createRoute, Link } from '@tanstack/react-router'
import { useMutation } from '@tanstack/react-query'
import { criarPauta } from '../api/pautas'
import { ApiError } from '../api/client'
import { usePautasConhecidas } from '../api/pautasConhecidas'
import { rootRoute } from './root'
import './home.css'

function Home() {
  const [tituloPauta, setTituloPauta] = useState('')
  const [descricaoPauta, setDescricaoPauta] = useState('')
  const { pautas, adicionar } = usePautasConhecidas()

  const mutation = useMutation({
    mutationFn: criarPauta,
    onSuccess: (pauta) => {
      adicionar({ id: pauta.id, tituloPauta: pauta.tituloPauta })
      setTituloPauta('')
      setDescricaoPauta('')
    },
  })

  function handleSubmit(event: FormEvent) {
    event.preventDefault()
    mutation.mutate({ tituloPauta, descricaoPauta: descricaoPauta || undefined })
  }

  return (
    <div className="cover">
      <section className="center stack">
        <h1>Pautas</h1>

        <form className="card stack" onSubmit={handleSubmit}>
          <h2>Nova pauta</h2>
          <div className="field">
            <label htmlFor="tituloPauta">Título</label>
            <input
              id="tituloPauta"
              value={tituloPauta}
              onChange={(e) => setTituloPauta(e.target.value)}
              maxLength={60}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="descricaoPauta">Descrição</label>
            <textarea
              id="descricaoPauta"
              value={descricaoPauta}
              onChange={(e) => setDescricaoPauta(e.target.value)}
              maxLength={500}
            />
          </div>
          <button type="submit" className="button" disabled={mutation.isPending}>
            {mutation.isPending ? 'Criando...' : 'Criar pauta'}
          </button>
          {mutation.isError && (
            <p className="message error">
              {mutation.error instanceof ApiError ? mutation.error.message : 'Erro ao criar pauta.'}
            </p>
          )}
        </form>

        <div className="card stack">
          <h2>Pautas criadas neste navegador</h2>
          {pautas.length === 0 ? (
            <p>Nenhuma pauta criada ainda.</p>
          ) : (
            <ul className="stack link-list" role="list">
              {pautas.map((pauta) => (
                <li key={pauta.id}>
                  <Link to="/pautas/$pautaId" params={{ pautaId: pauta.id }}>
                    {pauta.tituloPauta}
                  </Link>
                </li>
              ))}
            </ul>
          )}
        </div>
      </section>
    </div>
  )
}

export const homeRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: '/',
  component: Home,
})
