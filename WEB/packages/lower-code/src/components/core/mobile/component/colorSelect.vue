<template>
  <div class="color-select">
    <span
      :style="{ backgroundColor: color }"
      class="colorButton"
      size="small"
      @click="__eventBus.$emit('showColor')"
    ></span>
    <!-- <van-dialog v-model="showColor" show-confirm-button>
      <compact :value="fontColor" @input="resolveColor" :palette = "palette"></compact>
    </van-dialog> -->
  </div>
</template>
<script>
import { Compact } from "vue-color";

export default {
  props: ["value", "label", "options"],
  data() {
    let r = /\((.*)\)/.exec(this.value)
    let v = r ? this.resolveRgb(r[1]): { r: 0, g: 0, b: 0, a:1 }
    return {
      fontColor: v,
      showColor: false,
      palette: window.pageContentJson.style.fontColor
    };
  },
  components: {
    compact: Compact
  },
  computed: {
    color() {
      let rgb = ['r','g','b','a'].map((v) => {
        return this.fontColor[v]
      })
      return `rgba(${rgb.join(',')})` 
    },
  },
  // created() {
  //   let style = this.$route.query.styles
  //   let lmcolor = window.pageContentJson.style.lmcolor
  //   if (style) {
  //     lmcolor.some((v) => {
  //       if (style == v.code) {
  //         this.palette = v.rgb
  //         return true
  //       }
  //     })
  //   }
  // },
  created() {
    this.__eventBus.$on('resolveColor', (e) => {
        this.fontColor = e.rgba;
        this.$emit("input", this.color);
    })
  },
  destroyed() {
    this.__eventBus.$off('resolveColor')
  },
  methods: {
    resolveRgb(v) {
      let arr = v.split(',')
      return ({
        r: arr[0],
        g: arr[1],
        b: arr[2],
        a: 1
      })
    },
    // resolveColor(v) {
    //   this.fontColor = v.rgba;
    //   this.$emit("input", this.color);
    // },
  },
};
</script>
<style scoped lang="scss">
.color-select :deep(.vc-compact) {
  padding:10px 10px;
  width: 100%;
  .vc-compact-color-item {
    width: 25px;
    height: 25px;
  }
}
.color-select {
  :deep(.van-dialog__confirm) {
    color: #2f63f1;
  }
  .van-cell__value {
    overflow:visible;
  }
  .colorButton {
    position: relative;
    z-index: 111;
    display:inline-block;
    border: 1px solid #787878;
    width: 30px;
    height: 30px;
  }
  .colorTitle {
    color:#646566
  }
}
</style>
