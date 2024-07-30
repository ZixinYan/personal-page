import './assets/main.scss'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from '@/router'
import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
import locale from 'element-plus/dist/locale/zh-cn.js'



const app = createApp(App);
const pinia = createPinia();
app.use(pinia);
pinia.use(createPersistedState());
app.use(ElementPlus,{locale});
app.use(router);
app.mount('#app');
