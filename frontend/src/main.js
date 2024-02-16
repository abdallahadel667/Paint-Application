import { createApp } from 'vue'
import App from './App.vue'
import ColorPicker  from 'primevue/colorpicker';
import InputText from 'primevue/inputtext'; 
import PrimeVue from 'primevue/config';


const app=createApp(App)
app.use(PrimeVue);
app.component('Picker', ColorPicker);
app.component('InputText', InputText);

app.mount('#app')