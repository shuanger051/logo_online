<template>
  <div>
    <NoticeBar :style="{ ...objProperty, '--textDecoration': objProperty['text-decoration']}"
      :messageList="property.messageList" :speed="property.speed" />
  </div>
</template>

<script>
import { mapValues } from 'lodash'
import NoticeBar from '@Components/NoticeBar'

export default {
  props: ['name', 'context', 'property', 'style'],
  data() {
    return {
      mode: '',
      objProperty: {}
    }
  },
  components: {
    NoticeBar
  },
  computed: {
  },
  created() {
    this.mode = this.context.mode
    this.objProperty = mapValues(this.property, (value, key) => {
      if (
        key === 'font-size'
      ) {
        return value + 'px'
      }
      return value
    })
    if (this.context.mode === 'preview') {
      this.objProperty.background = `${this.objProperty['background-color']}`
    }
    Object.assign(this.objProperty, this.style)
  },
  watch: {
    property: {
      handler(newVal) {
        this.objProperty = mapValues(this.property, (value, key) => {
          if (
            key === 'font-size'
          ) {
            return value + 'px'
          }
          return value
        })
        Object.assign(this.objProperty, this.style)
      },
      deep: true
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
/deep/ .join-content-item-txt {
  text-decoration: var(--textDecoration);
}
</style>
