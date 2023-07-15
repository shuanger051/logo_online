<template>
    <router-view />
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'BlankView',
  computed: {
    ...mapState('setting', ['multiPage', 'animate'])
  }
}
</script>

<style scoped>

</style>
