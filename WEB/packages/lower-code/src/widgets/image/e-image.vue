<template>
  <collapse-wrap name="图片设置">
    <img-select v-model="propertyProxy.src" :size="5120" description="支持png、jpg、jpeg、gif格式，图片大小不超过5MB" :accept="['png', 'jpg','jpeg', 'gif']"></img-select>
  </collapse-wrap>
</template>
<script>
import ImgSelect from '@Components/ImgSelect'
import CollapseWrap from '@Components/collapseWrap'

export default {
  name: 'eImagePropsConfig',
  props: [
    'context', 'selectedElementData', 'selectedElement', 'selectedPage'
  ],
  components: {
    CollapseWrap,
    ImgSelect
  },
  computed: {
    propertyProxy() {
      return new Proxy(this.selectedElementData.property, {
        get: (target, name) => {
          return this.selectedElementData.property[name] || ''
        },
        set: (target, name, value) => {
          if (value == '') {
            this.updateFunc(375, 185, name, value)
          } else {
            const that = this
            const img = new Image()
            img.src = value
            img.onload = function() {
              if (this.width > 375 || this.width == 375) {
                const scale = 375 / this.width
                const newHeight = this.height * scale
                that.updateFunc(375, newHeight, name, value)
                that.updateStyleFunc(375, newHeight)
              } else {
                that.updateFunc(this.width, this.height, name, value)
                that.updateStyleFunc(this.width, this.height)
              }
            }
          }
          return true
        }
      })
    }
  },
  data() {
    return {
      id: '',
      pageId: ''
    }
  },
  created() {
    this.id = this.selectedElement // 记录下当前的元素的id
    this.pageId = this.selectedPage // 记录下当前页面的id

  },
  methods: {
    // 更新图片
    updateFunc(width, height, name, value) {
      let { updateElementProperty } = this.context
      updateElementProperty({
        width: width,
        height: height,
        [name]: value
      })
    },
    // 更新图片样式
    updateStyleFunc(width, height) {
      let { updateElementStyle } = this.context
      updateElementStyle({
        width: width,
        height: height,
        eleId: this.id,
        pageId: this.pageId
      })
    }
  }
}
</script>
