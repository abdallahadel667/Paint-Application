<template>
  <Drawing_App/>
  </template>
  
  <script>
  import Drawing_App from './components/Drawing_App.vue'
  
  export default {
    name: 'App',
    components: {
      Drawing_App,
    }
  }
  </script>
  
  <style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
  </style>
  