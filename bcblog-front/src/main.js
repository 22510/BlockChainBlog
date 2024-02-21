import './assets/reset.css'
// 全局生效的css

import { createApp } from 'vue'
import App from './App.vue'
// 入口
import { isAndroid } from 'vue-device-detect';

const app = createApp(App)
// 所有的内容从App.vue开始读

import router from './router/index.js'
import AndroidRouter from './router/router-android'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// import 'element-plus/theme-chalk/index.css';
// 全局生效


import axios from 'axios'
axios.defaults.baseURL = 'http://localhost:8088/'
// axios.defaults.baseURL = 'https://www.qingheli.tech:8088/'

const isMobileDevice = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);

const routers = isMobileDevice ? AndroidRouter : router;

app.use(ElementPlus)
app.use(routers)
app.mount('#app')
// 绑定App.vue中的 id="app"


