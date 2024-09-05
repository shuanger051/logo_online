<template>
  <div class="color-select">
    <span
      :style="{ backgroundColor: color }"
      class="colorButton"
      size="small"
      @click="showColor = true"
    ></span>
    <a-modal v-model="showColor" class="color-select-modal" title="颜色" cancelText="取消" okText="确定" @ok="showColor=false">
      <compact :value="fontColor" @input="resolveColor" :palette = "palette"></compact>
    </a-modal>
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
  created() {
    let style = this.$route.query.styles
    let lmcolor = window.pageContentJson.style.lmcolor
    if (style) {
      lmcolor.some((v) => {
        if (style == v.code) {
          this.palette = v.rgb
          return true
        }
      })
    }
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
    resolveColor(v) {
      this.fontColor = v.rgba;
      this.$emit("input", this.color);
    },
  },
};
</script>
<style scoped lang="scss">
.color-select-modal .vc-compact {
  padding:10px 10px;
  width: 100%;
  box-shadow: none;
  :deep(.vc-compact-color-item) {
    width: 25px;
    height: 25px;
  }
}
.color-select {
  .colorButton {
    position: relative;
    z-index: 111;
    display:inline-block;
    border: 1px solid #787878;
    width: 30px;
    height: 30px;
  }
}
.color-select-modal {

  .colorTitle {
    color:#646566
  }
}
</style>
