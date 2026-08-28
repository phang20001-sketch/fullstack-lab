import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '') // 按当前 mode 读取对应的 .env 环境变量

  return {
    plugins: [react()], // 启用 React JSX 转换与开发热更新插件
    server: { // Vite 本地开发服务器配置
      port: 5173, // 前端开发服务器监听端口
      proxy: { // 将指定请求转发给后端，开发时无需处理浏览器跨域
        '/api': { // 代理所有以 /api 开头的业务接口请求
          target: env.VITE_API_TARGET || 'http://localhost:8080', // 后端目标地址，优先读取 VITE_API_TARGET
          changeOrigin: true, // 将请求 Host 改为目标服务地址，便于后端正确接收代理请求
        },
        '/actuator': { // 代理所有以 /actuator 开头的健康检查与应用信息请求
          target: env.VITE_API_TARGET || 'http://localhost:8080', // Actuator 使用与业务接口相同的后端地址
          changeOrigin: true, // 将请求 Host 改为目标服务地址
        },
      },
    },
  }
})
