<template>
  <div id="app">
    <HomeIndex></HomeIndex>
  </div>
</template>

<script>

import HomeIndex from "@/components/Index.vue";

export default {
  name: 'App',
  components: {
    HomeIndex
  }
}
</script>

<style>
#app {
  height: 100vh;
}
</style>
