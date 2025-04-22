import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  devServer: {
    host:'0.0.0.0',
    port:8080,
    open:true,
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:12345',//代理服务器的地址
        changeOrigin: true,//允许跨域
        rewrite: path => path.replace(/^\/api/, '')//重写路径
      }
    }
  }
})
