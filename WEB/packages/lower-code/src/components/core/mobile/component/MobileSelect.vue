<template>
  <div>
    <van-button color="#7232dd" plain @click="showPicker=true" class="btn">{{buttonVal}}</van-button>
    <van-popup v-model="showPicker" position="bottom" :style="{ height: '30%' }">
      <van-picker 
        show-toolbar 
        :title="label" 
        :columns="columns" 
        :default-index="index" 
        @cancel="showPicker = false"
        @confirm="onConfirm"

      />
    </van-popup>

  </div>
</template>
<script>
  export default {
    props: ['value', 'label', 'options'],
    data(){
      return {
        model: this.value,
        showPicker: false
      }
    },
    computed: {
      columns(){
        return this.options.map((item) => {
          return item.label
        })
      },
      buttonVal() {
        const item = this.getItemByLabelOrValue(this.value, false)
        return (item ? item.label : this.label).slice(0,4)
      },
      index() {
        const item = this.getItemByLabelOrValue(this.value, false)
        return item ? this.options.indexOf(item) : 2
      }
    },
    methods: {
      getItemByLabelOrValue(val, isLabel = true) {
        const item = this.options.find((column) => {
          return column[isLabel ? 'label': 'value'] == val
        })
        return item
      },
      onConfirm(value) {
        const item = this.getItemByLabelOrValue(value)
        this.$emit('input', item.value)
        this.showPicker = false;
      },
    }
  }
</script>
<style scoped lang="scss">
  .btn {
    width: 100px;
    overflow: hidden;    
    height: 44px;
    :deep(.van-button__text) {
      line-height: 44px;
    }
  }
</style>