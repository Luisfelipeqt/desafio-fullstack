import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // backend has no CORS config; proxy dev requests instead of touching it
      '/api': 'http://localhost:8080',
    },
  },
})
