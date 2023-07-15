<template>
  <div>
    <van-tabs ref="tabs" v-model="activeIndex" :style="{'--tab-customer-fontSize': objProperty['tab-customer-fontSize'],
      '--tab-customer-selected-fontSize': objProperty['tab-customer-selected-fontSize'],
      '--tab-customer-fontWeight': objProperty['tab-customer-fontWeight'],
      '--tab-customer-selected-fontWeight': objProperty['tab-customer-selected-fontWeight'],
      '--tab-customer-color': objProperty['tab-customer-color'],
      '--tab-selected-color': objProperty['tab-selected-color'],
      '--text-customer-direction': objProperty['text-customer-direction'],
      '--text-customer-selected-direction': objProperty['text-customer-selected-direction'],
      '--background-color': objProperty['background-color'],
      '--selected-background-color': objProperty['selected-background-color'],
      '--tab-customer-style': objProperty['tab-customer-style'],
      '--tab-customer-selected-style': objProperty['tab-customer-selected-style']
      }" :type="objProperty.type" @click="tabChange" ellipsis>
      <van-tab v-for="(tab, index) in tabList" :title="tab.title" :key="index">
        <!-- 内容 {{ tab.title }} -->
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
import VanTabs from 'vant/es/tabs/index'
import 'vant/es/tabs/style/index'
import VanTab from 'vant/es/tab/index'
import 'vant/es/tab/style/index'
import { mapValues, difference, indexOf } from 'lodash'
import store from '@h5Render/store/h5State'

export default {
  props: ['name', 'context', 'property', 'style', 'uuid'],
  data() {
    return {
      activeIndex: 0
    }
  },
  components: { VanTabs, VanTab },
  computed: {
    tabList() {
      return this.property.tabList
    },
    objProperty() {
      return mapValues(this.property, (value, key) => {
        if (
          key.indexOf('fontSize') > 0 ||
          key === 'letter-spacing' ||
          key === 'padding-left' ||
          key === 'padding-right' ||
          key === 'padding-top' ||
          key === 'padding-bottom'
        ) {
          return value + 'px'
        }
        return value
      })
    }
  },
  watch: {
    'objProperty.activeTime': function () {
      if (this.context.mode === 'edit') {
        this.elementStateChange(this.property.activeIndex)
      }
    },
    'objProperty.type': function (val) {
      if (val === 'line') {
        this.$refs.tabs.resize()
      }
    },
    'style.width': function (val) {
      this.$refs.tabs.resize()
    },
    'objProperty.tabList.length': function () {
      this.$refs.tabs.resize()
    },
    'objProperty.activeIndex': function (val) {
      if (this.context.mode === 'edit') {
        this.activeIndex = val
      }
    }
  },
  mounted() {
    this.elementStateChange(0)
  },
  methods: {
    elementStateChange(index) {
      let showElemet = []
      let hideElemet = []
      for (let i = 0; i < this.tabList.length; i++) {
        if (this.tabList[i].elementContent.length === 0) continue
        for (let j = 0; j < this.tabList[i].elementContent.length; j++) {
          let uuid = this.tabList[i].elementContent[j]
          if (i === index) {
            if (indexOf(showElemet, uuid) === -1) {
              showElemet.push(uuid)
            }
          } else {
            if (indexOf(hideElemet, uuid) === -1) {
              hideElemet.push(uuid)
            }
          }
        }
      }
      for (let k = 0; k < showElemet.length; k++) {
        document.getElementById(showElemet[k]).style.display = 'block'
      }
      let diff = difference(hideElemet, showElemet)
      for (let m = 0; m < diff.length; m++) {
        document.getElementById(diff[m]).style.display = 'none'
      }
      // if (this.context.mode !== 'edit') {
      store.dispatch('changeCurElement', showElemet)
      // }
    },
    tabChange(index) {
      if (this.context.mode === 'edit') {
        let { updateElementProperty } = this.context
        updateElementProperty({ activeIndex: index })
      }
      this.elementStateChange(index)
    }
  },
  beforeDestroy() {
    if (this.context.mode === 'edit') {
      let allElemet = []
      for (let i = 0; i < this.tabList.length; i++) {
        for (let j = 0; j < this.tabList[i].elementContent.length; j++) {
          let uuid = this.tabList[i].elementContent[j]
          if (indexOf(allElemet, uuid) === -1) {
            allElemet.push(uuid)
          }
        }
      }
      for (let k = 0; k < allElemet.length; k++) {
        if (document.getElementById(allElemet[k])) {
          document.getElementById(allElemet[k]).style.display = 'block'
        }
      }
    }
  }
}
</script>
<style scoped lang="scss">
/deep/ .van-tab {
  color: var(--tab-customer-color);
  font-size: var(--tab-customer-fontSize);
  font-weight: var(--tab-customer-fontWeight);
  text-decoration: var(--text-customer-direction);
  background-color: var(--background-color);
  font-style: var(--tab-customer-style);
}

/deep/ .van-tab--active {
  font-size: var(--tab-customer-selected-fontSize);
  color: var(--tab-selected-color);
  font-weight: var(--tab-customer-selected-fontWeight);
  text-decoration: var(--text-customer-selected-direction);
  background-color: var(--selected-background-color);
  font-style: var(--tab-customer-selected-style);
}

/deep/ .van-tabs__line {
  background-color: var(--tab-selected-color);
}
/deep/ .van-tabs__nav--card .van-tab.van-tab--active {
  font-size: var(--tab-customer-selected-fontSize);
  color: var(--tab-selected-color);
  font-weight: var(--tab-customer-selected-fontWeight);
  text-decoration: var(--text-customer-selected-direction);
  background-color: var(--selected-background-color);
  font-style: var(--tab-customer-selected-style);
}

/deep/ .van-tabs__nav--card {
  border-color: var(--tab-customer-color);
}

/deep/ .van-tabs__nav--card .van-tab {
  border-right-color: var(--tab-customer-color);
}
/deep/ .van-tab__text--ellipsis {
  word-break: break-all;
}
</style>
