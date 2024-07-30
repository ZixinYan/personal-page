<template>
    <div id="app" class="text-container">
      <h1 id="title">{{ welcomeMessage }}</h1>
      <p>{{ description }}</p>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        welcomeMessage: '',
        description: ''
      };
    },
    created() {
      this.updateWelcomeMessage();
    },
    methods: {
      updateWelcomeMessage() {
        const currentHour = new Date().getHours();
        if (currentHour < 12) {
          this.welcomeMessage = '早上好，祝您一天好心情';
        } else if (currentHour < 18) {
          this.welcomeMessage = '下午好！该好好工作了';
        } else {
          this.welcomeMessage = '晚上好，记得早点儿休息';
        }
      }
    }
  };
  </script>
  

  <style>
  .text-container {
    display: flex;
    font-size: larger;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 300px;
    text-align: center;
    background-color: transparent;
  }

  h1{
    font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    font-style: italic;
  }
  </style>
  