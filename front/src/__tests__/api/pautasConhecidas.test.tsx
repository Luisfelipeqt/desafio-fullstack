import { act, renderHook } from '@testing-library/react'
import { afterEach, describe, expect, it } from 'vitest'
import { usePautasConhecidas } from '../../api/pautasConhecidas'

afterEach(() => {
  localStorage.clear()
})

describe('usePautasConhecidas', () => {
  it('começa vazio quando não há nada no localStorage', () => {
    const { result } = renderHook(() => usePautasConhecidas())

    expect(result.current.pautas).toEqual([])
  })

  it('adiciona uma pauta no início da lista e persiste no localStorage', () => {
    const { result } = renderHook(() => usePautasConhecidas())

    act(() => result.current.adicionar({ id: '1', tituloPauta: 'Reforma do estatuto' }))

    expect(result.current.pautas).toEqual([{ id: '1', tituloPauta: 'Reforma do estatuto' }])
    expect(JSON.parse(localStorage.getItem('pautas-conhecidas')!)).toEqual([
      { id: '1', tituloPauta: 'Reforma do estatuto' },
    ])
  })

  it('não duplica quando a mesma pauta é adicionada novamente', () => {
    const { result } = renderHook(() => usePautasConhecidas())

    act(() => result.current.adicionar({ id: '1', tituloPauta: 'Titulo original' }))
    act(() => result.current.adicionar({ id: '1', tituloPauta: 'Titulo atualizado' }))

    expect(result.current.pautas).toEqual([{ id: '1', tituloPauta: 'Titulo atualizado' }])
  })

  it('carrega pautas já existentes no localStorage ao montar', () => {
    localStorage.setItem('pautas-conhecidas', JSON.stringify([{ id: '9', tituloPauta: 'Pré-existente' }]))

    const { result } = renderHook(() => usePautasConhecidas())

    expect(result.current.pautas).toEqual([{ id: '9', tituloPauta: 'Pré-existente' }])
  })
})
