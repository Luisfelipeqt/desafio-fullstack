import { renderHook } from '@testing-library/react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { useTempoRestante } from '../../hooks/useTempoRestante'

beforeEach(() => {
  vi.useFakeTimers()
  vi.setSystemTime(new Date('2026-01-01T10:00:00Z'))
})

afterEach(() => {
  vi.useRealTimers()
})

describe('useTempoRestante', () => {
  it('retorna null quando não há data de fechamento', () => {
    const { result } = renderHook(() => useTempoRestante(undefined))

    expect(result.current).toBeNull()
  })

  it('formata o tempo restante em minutos:segundos', () => {
    const { result } = renderHook(() => useTempoRestante('2026-01-01T10:01:30Z'))

    expect(result.current).toEqual({ encerrada: false, formatado: '1:30' })
  })

  it('marca como encerrada quando a data de fechamento já passou', () => {
    const { result } = renderHook(() => useTempoRestante('2026-01-01T09:59:00Z'))

    expect(result.current).toEqual({ encerrada: true, formatado: '0:00' })
  })
})
