import { useEffect, useState } from 'react'

export interface TempoRestante {
  encerrada: boolean
  formatado: string
}

export function useTempoRestante(dataFechamento: string | undefined): TempoRestante | null {
  const [agora, setAgora] = useState(() => Date.now())

  useEffect(() => {
    if (!dataFechamento) return
    const id = setInterval(() => setAgora(Date.now()), 1000)
    return () => clearInterval(id)
  }, [dataFechamento])

  if (!dataFechamento) return null

  const restanteSegundos = Math.max(0, Math.floor((new Date(dataFechamento).getTime() - agora) / 1000))
  const minutos = Math.floor(restanteSegundos / 60)
  const segundos = restanteSegundos % 60

  return {
    encerrada: restanteSegundos <= 0,
    formatado: `${minutos}:${segundos.toString().padStart(2, '0')}`,
  }
}
