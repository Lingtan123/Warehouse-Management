<template>
  <div id="app">
    <HomeIndex></HomeIndex>
  </div>
</template>

<script>

import HomeHomeIndex from "@/components/HomeIndex.vue";

export default {
  name: 'App',
  components: {
    HomeIndex: HomeHomeIndex
  }
}
</script>

<style>
#app {
  height: 100vh;
}
</style>
