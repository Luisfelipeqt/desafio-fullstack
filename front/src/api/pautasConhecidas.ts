import { useState } from 'react'

export interface PautaConhecida {
  id: string
  tituloPauta: string
}

const STORAGE_KEY = 'pautas-conhecidas'

function ler(): PautaConhecida[] {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY) ?? '[]')
  } catch {
    return []
  }
}

export function usePautasConhecidas() {
  const [pautas, setPautas] = useState<PautaConhecida[]>(ler)

  function adicionar(pauta: PautaConhecida) {
    setPautas((atual) => {
      const proximo = [pauta, ...atual.filter((p) => p.id !== pauta.id)]
      localStorage.setItem(STORAGE_KEY, JSON.stringify(proximo))
      return proximo
    })
  }

  return { pautas, adicionar }
}
