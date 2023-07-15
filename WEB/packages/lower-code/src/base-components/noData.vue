<template>
  <div class="empty-wrap" :style="{
    '--iconMt':iconMt,
    '--iconMb':iconMb
  }">
    <img class="empty-icon" :src="noDataImg" alt="">
    <div>{{content}}</div>
  </div>
</template>

<script>
import noDataImg from '@Root/assets/images/no-data.png'

export default {
  name: 'NoData',
  props: {
    content: {
      type: String,
      default: ''
    },
    iconMt:{
      type:String,
      default:'50px'
    },
    iconMb:{
      type:String,
      default:'20px'
    }
  },
  computed: {

  },
  created() {
    this.noDataImg = noDataImg
  }
}
</script>

<style lang="scss" scoped>
.empty-wrap {
  width: 100%;
  text-align: center;
  .empty-icon {
    width: 20%;
    margin: 0;
    margin-top:var(--iconMt);
    margin-bottom: var(--iconMb);
  }
}
</style>
