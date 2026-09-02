import { afterEach, describe, expect, it, vi } from 'vitest'
import { ApiError, request } from '../../api/client'

function mockFetch(response: { status: number; body?: unknown }) {
  return vi.fn().mockResolvedValue({
    status: response.status,
    ok: response.status >= 200 && response.status < 300,
    statusText: 'Error',
    json: () => Promise.resolve(response.body),
  })
}

afterEach(() => {
  vi.unstubAllGlobals()
})

describe('request', () => {
  it('retorna o corpo decodificado quando a resposta é ok', async () => {
    vi.stubGlobal('fetch', mockFetch({ status: 200, body: { id: '1' } }))

    await expect(request('/api/v1/pautas')).resolves.toEqual({ id: '1' })
  })

  it('retorna null para respostas 204', async () => {
    vi.stubGlobal('fetch', mockFetch({ status: 204 }))

    await expect(request('/api/v1/pautas/1/votos')).resolves.toBeNull()
  })

  it('lança ApiError com mensagem e violações quando a resposta não é ok', async () => {
    vi.stubGlobal(
      'fetch',
      mockFetch({
        status: 400,
        body: { message: 'Associado ja votou nesta pauta.', violations: [{ campo: 'voto', mensagem: 'obrigatorio' }] },
      }),
    )

    await expect(request('/api/v1/pautas/1/votos')).rejects.toMatchObject({
      status: 400,
      message: 'Associado ja votou nesta pauta.',
      violations: [{ campo: 'voto', mensagem: 'obrigatorio' }],
    })
  })

  it('usa o statusText quando o corpo do erro não tem mensagem', async () => {
    vi.stubGlobal('fetch', mockFetch({ status: 500 }))

    await expect(request('/api/v1/pautas')).rejects.toBeInstanceOf(ApiError)
  })
})
