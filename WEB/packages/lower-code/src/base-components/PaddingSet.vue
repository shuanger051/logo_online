<template>
  <div class="padding-box">
    <h-row>
      <h-col :span="6">
        <h-input-number
          :precision="0"
          setzero
          size="small"
          v-model="pdL"
          @on-change="onchange('padding-left', pdL)"
        ></h-input-number>
      </h-col>
      <h-col :span="6">
        <h-input-number
          :precision="0"
          setzero
          size="small"
          v-model="pdR"
          @on-change="onchange('padding-right', pdR)"
        ></h-input-number>
      </h-col>
      <h-col :span="6">
        <h-input-number
          :precision="0"
          setzero
          size="small"
          v-model="pdT"
          @on-change="onchange('padding-top', pdT)"
        ></h-input-number>
      </h-col>
      <h-col :span="6">
        <h-input-number
          :precision="0"
          setzero
          size="small"
          v-model="pdB"
          @on-change="onchange('padding-bottom', pdB)"
        ></h-input-number>
      </h-col>
    </h-row>
    <h-row>
      <h-col :span="6">
        <p class="font-desc">左侧</p>
      </h-col>
      <h-col :span="6">
        <p class="font-desc">右侧</p>
      </h-col>
      <h-col :span="6">
        <p class="font-desc">顶部</p>
      </h-col>
      <h-col :span="6">
        <p class="font-desc">底部</p>
      </h-col>
    </h-row>
  </div>
</template>
<script>
export default {
  props: {
    paddingLeftVal: {
      type: Number,
      default: () => 0
    },
    paddingRightVal: {
      type: Number,
      default: () => 0
    },
    paddingTopVal: {
      type: Number,
      default: () => 0
    },
    paddingBottomVal: {
      type: Number,
      default: () => 0
    }
  },
  created () {
  },
  data() {
    return {
      pdL: this.paddingLeftVal,
      pdR: this.paddingRightVal,
      pdT: this.paddingTopVal,
      pdB: this.paddingBottomVal
    }
  },
  methods: {
    onchange(type, value) {
      const data = {
        name: type,
        value: value
      }
      this.$emit('updateFunc', data)
    }
  }
}
</script>
<style scoped lang="scss">
.font-desc {
  text-align: center;
  margin: 0 0 8px 0;
}
</style>
