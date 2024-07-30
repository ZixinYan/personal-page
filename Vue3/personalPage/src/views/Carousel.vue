<template>
    <el-carousel :interval="interval" arrow="always" indicator-position="outside">
      <el-carousel-item v-for="(item, index) in items" :key="index">
        <img :src="item.image" alt="">
        <div class="carousel-item-text">
          <h3>{{ item.title }}</h3>
          <p>{{ item.description }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>
  </template>
  
  <script>
  export default {
    name: 'Carousel',
    props: {
      items: {
        type: Array,
        required: true
      },
      interval: {
        type: Number,
        default: 5000
      }
    }
  }
  </script>
  
  <style scoped>
  .carousel-item-text {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.5);
    color: #fff;
    padding: 16px;
    box-sizing: border-box;
  }
  
  .carousel-item-text h3 {
    margin-top: 0;
    margin-bottom: 8px;
  }
  
  .carousel-item-text p {
    margin-top: 0;
    margin-bottom: 0;
  }
  </style>
  