import { useEffect, useState } from 'react'
import './App.css'

type BackendStatus = {
  status: string
  service: string
  timestamp: string
}

function App() {
  const [backend, setBackend] = useState<BackendStatus | null>(null)
  const [error, setError] = useState('')

  useEffect(() => {
    fetch('/api/health')
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`)
        }
        return response.json() as Promise<BackendStatus>
      })
      .then(setBackend)
      .catch((reason: unknown) => {
        setError(reason instanceof Error ? reason.message : '无法连接后端')
      })
  }, [])

  return (
    <main className="page-shell">
      <section className="hero-card">
        <p className="eyebrow">FULLSTACK LAB</p>
        <h1>React + Spring Boot 学习项目</h1>
        <p className="intro">
          前端使用 React、TypeScript 和 Vite，后端使用 Java 21 与 Spring Boot。
        </p>

        <div className="stack-grid">
          <article>
            <span>Frontend</span>
            <strong>React 19 · Vite 8</strong>
          </article>
          <article>
            <span>Backend</span>
            <strong>Spring Boot 4 · MyBatis-Plus</strong>
          </article>
          <article>
            <span>Infrastructure</span>
            <strong>PostgreSQL · Druid · Redisson · RabbitMQ</strong>
          </article>
        </div>

        <div className={`status ${backend ? 'is-up' : error ? 'is-down' : ''}`}>
          <span className="status-dot" />
          {backend
            ? `后端已连接：${backend.service}`
            : error
              ? `后端未连接：${error}`
              : '正在检查后端连接…'}
        </div>
      </section>
    </main>
  )
}

export default App
