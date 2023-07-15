<template>
  <el-tabs v-model="currentTab" type="border-card">
    <el-tab-pane label="组件样式" v-if="selectedElement" name="componentStyle" class="tabs-content">
      <EProps :key="selectedElement"></EProps>
    </el-tab-pane>
    <el-tab-pane label="交互" v-if="showEvents" name="componentActions" class="tabs-content">
      <component-actions :worksInfo="worksInfo"></component-actions>
    </el-tab-pane>
    <el-tab-pane label="页面样式" v-if="!selectedElement" name="pageStyle" class="tabs-content">
      <page-sty></page-sty>
    </el-tab-pane>
    <el-tab-pane label="作品设置" v-if="!selectedElement" name="pageSet" class="tabs-content">
      <page-set></page-set>
    </el-tab-pane>
    <el-tab-pane v-for="(item, index) in extraPanelList" :label="item.title" :key="index" :name="item.tabName || ''">
      <Extra :index="index"></Extra>
    </el-tab-pane>
  </el-tabs>
  <!-- <h-tabs>
    <h-tab-pane v-if="selectedElement" label="组件样式" name="componentStyle">
      <EProps></EProps>
    </h-tab-pane>
    <h-tab-pane v-if="selectedElement" label="交互" name="componentActions">
      <component-actions></component-actions>
    </h-tab-pane>
    <h-tab-pane v-if="!selectedElement" label="页面样式" name="pageStyle">
    </h-tab-pane>
    <h-tab-pane v-if="!selectedElement" label="页面设置" name="pageSet">
      <page-set></page-set>
    </h-tab-pane>
  </h-tabs> -->
</template>

<script>
import { mapState, mapGetters } from 'vuex'

import ComponentActions from './componentActions.vue'
import PageSty from './pageSty.vue'
import PageSet from './pageSet.vue'
import EProps from './e-props-config'
import Extra from './extra.js'

export default {
  name: 'SettingPanel',
  props: ['worksInfo'],
  components: {
    ComponentActions,
    PageSet,
    PageSty,
    EProps,
    Extra
  },
  data() {
    return {
      currentTab: 'pageStyle'
    }
  },
  watch: {
    'selectedElement': {
      handler(newVal) {
        if (!newVal) {
          this.currentTab = 'pageStyle'
        } else {
          this.currentTab = 'componentStyle'
        }
      },
      deep: true
    }
  },
  computed: {
    ...mapState('cms/editState', [
      'selectedElement', 'selectedPage'
    ]),
    ...mapGetters('cms/editState', [
      'selectedElementData'
    ]),
    showEvents() {
      if (this.selectedElement) {
        if (this.selectedElementData && this.selectedElementData.editPanelConfig && this.selectedElementData.editPanelConfig.hideEvents) {
          return false
        } else {
          return true
        }
      } else {
        return false
      }
    },
    extraPanelList() {
      if (this.selectedElement) {
        if (this.selectedElementData && this.selectedElementData.extraRenderPanel) {
          return this.selectedElementData.extraRenderPanel
        } else {
          return []
        }
      } else {
        return []
      }
    }
  },
  mounted() {
  },
  methods: {

  }
}
</script>

<style scoped lang="scss" >
/deep/ .el-tabs__content {
  height: calc(100vh - 200px);
  overflow-y: auto;
  padding: 0;
}
.tabs-content {
  padding: 15px;
}
</style>
