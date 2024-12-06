import { fileURLToPath, URL } from 'node:url';

import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueDevTools from 'vite-plugin-vue-devtools';

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      // 将 '/knowledge' 路径代理到后端服务 http://localhost:8080
      '/knowledge': {
        target: 'http://localhost:8080', // 后端服务地址
        changeOrigin: true, // 允许跨域
        rewrite: (path) => path.replace(/^\/knowledge/, '/knowledge'), // 重写路径
      },
    },
  },
});


