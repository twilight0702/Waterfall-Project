import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import eventBus from '@/eventBus';


const app = createApp(App)

app.config.globalProperties.$getHash = function () {
    const userAgent = navigator.userAgent;
    const language = navigator.language;

    const deviceInfo = `${userAgent}-${language}`;
    // console.log('设备信息：', deviceInfo);

    // 使用哈希函数生成唯一 ID
    let hash = 0;
    for (let i = 0; i < deviceInfo.length; i++) {
        hash = ((hash << 5) - hash) + deviceInfo.charCodeAt(i);
        hash |= 0; // 强制转换为 32 位整数
    }
    // console.log('生成的唯一 ID：', hash);
    return hash;
};

app.config.globalProperties.$getUser = function () {
    const hash = this.$getHash();
    axios.get(`/knowledge/findUser?key=${hash}`)
        .then(response => {
            console.log('获取用户信息成功：', response.data);
            if (response.data == "-114514")
                window.location.href = window.location.origin + '/login';
            else {
                eventBus.emit('usernameUpdated', response.data);
            }
        })
        .catch(error => {
            console.error('获取用户信息失败：', error);
        });
}

app.use(router)

app.mount('#app')
