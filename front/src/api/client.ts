export interface Violation {
  campo: string
  mensagem: string
}

export class ApiError extends Error {
  status: number
  violations: Violation[]

  constructor(status: number, message: string, violations: Violation[] = []) {
    super(message)
    this.status = status
    this.violations = violations
  }
}

export async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(path, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...init?.headers },
  })
  const body = res.status === 204 ? null : await res.json().catch(() => null)
  if (!res.ok) {
    throw new ApiError(res.status, body?.message ?? res.statusText, body?.violations ?? [])
  }
  return body as T
}
