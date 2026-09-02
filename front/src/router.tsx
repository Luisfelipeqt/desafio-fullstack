import { createRouter } from '@tanstack/react-router'
import { rootRoute } from './routes/root'
import { homeRoute } from './routes/home'
import { pautaRoute } from './routes/pauta'

const routeTree = rootRoute.addChildren([homeRoute, pautaRoute])

export const router = createRouter({ routeTree })

declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router
  }
}
