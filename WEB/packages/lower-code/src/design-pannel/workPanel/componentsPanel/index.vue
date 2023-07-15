<template>
  <el-tabs v-model="currentTab" type="border-card" :stretch="true">
    <el-tab-pane label="组件" name="templates" v-if="!castratedVersion">
      <div class="components-list-box">
        <div class="components-list-wrap">
          <div class="component-list">
            <div v-for="item in componentsClassifyList" :key="item.groupName">
              <components-group :list="item.componentsList" :groupName="item.groupName" :groupType="item.groupType">
              </components-group>
            </div>
          </div>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="页面" name="pages" style="over-flow:hidden">
      <pageGroup></pageGroup>
      <div class="button-content">
        <h-button type="primary" style="width: 100%" @click="addNewPage" v-if="!castratedVersion">新增页面</h-button>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import componentsGroup from './componentsGroup'
import pageGroup from './pageGroup'
// import { getComponentList } from '@Apis/works.js'

export default {
  name: 'ComponentsPanel',
  components: {
    componentsGroup,
    pageGroup
  },
  props: {
    componentsList: {
      type: Array,
      default: () => []
    },
    worksContent: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      componentsClassifyList: [
        {
          groupType: '1',
          groupName: '1',
          componentsList: []
        },
        {
          groupType: '2',
          groupName: '2',
          componentsList: []
        },
        {
          groupType: '3',
          groupName: '3',
          componentsList: []
        },
        {
          groupType: '4',
          groupName: '4',
          componentsList: []
        },
        {
          groupType: '5',
          groupName: '5',
          componentsList: []
        }
      ],
      currentTab: 'templates',
      castratedVersion: window.CMS_CONFIG.CASTRATED_VERSION && window.CMS_CONFIG.CASTRATED_VERSION == 'true' // 是否打开Lite版
    }
  },
  created() {
    // this.getComponentList()
    if (this.castratedVersion) {
      this.currentTab = 'pages'
    } else {
      this.currentTab = 'templates'
    }
  },
  watch: {
    componentsList: {
      handler(value) {
        if (value.length) {
          value.forEach(item => {
            switch (parseInt(item['component_type'])) {
              case 1:
                const basegroup = this.componentsClassifyList.find(j => { return j.groupType == 1 })
                const hasBaseComponent = basegroup.componentsList.find(i => {
                  return i.component_name == item.component_name
                })
                if (!hasBaseComponent) {
                  basegroup.componentsList.push(item)
                }
                basegroup.groupName = '基础类'
                break
              case 2:
                const formgroup = this.componentsClassifyList.find(j => { return j.groupType == 2 })
                const hasFormComponent = formgroup.componentsList.find(i => { return i.component_name == item.component_name })
                if (!hasFormComponent) {
                  formgroup.componentsList.push(item)
                }
                formgroup.groupName = '表单类'
                break
              case 3:
                const businessgroup = this.componentsClassifyList.find(j => { return j.groupType == 3 })
                const hasBusinesssComponent = businessgroup.componentsList.find(i => { return i.component_name == item.component_name })
                if (!hasBusinesssComponent) {
                  businessgroup.componentsList.push(item)
                }
                businessgroup.groupName = '业务类'
                break
              case 4:
                const activegroup = this.componentsClassifyList.find(j => { return j.groupType == 4 })
                if (activegroup && window.CMS_CONFIG.SHOW_ACTWIDGETS === 'true') {
                  const hasActiveComponent = activegroup.componentsList.find(i => { return i.component_name == item.component_name })
                  if (!hasActiveComponent) {
                    activegroup.componentsList.push(item)
                  }
                  activegroup.groupName = '活动类'
                }
                break
              case 5:
                const businessDatagroup = this.componentsClassifyList.find(j => { return j.groupType == 5 })
                const hasBusinesssDataComponent = businessDatagroup.componentsList.find(i => { return i.component_name == item.component_name })
                if (!hasBusinesssDataComponent) {
                  businessDatagroup.componentsList.push(item)
                }
                businessDatagroup.groupName = '业务数据类'
                break
            }
          })
        }
      }
    }
  },
  methods: {
    // getComponentList() {
    //   getComponentList({
    //   }).then(res => {
    //     this.getComponentList = res.data.rows
    //   })
    // }
    addNewPage() {
      this.$store.$$init()
      const { cms } = this.$store.state
      let pages = cms.pages.items
      let index = 0
      pages.forEach((item) => {
        if (item.name) {
          if (item.name.indexOf('页面') == 0) {
            let newNameIndex = item['name'].substr(2)
            if (Number(newNameIndex) + '' !== NaN + '') {
              if (newNameIndex > index) {
                index = parseInt(newNameIndex)
              }
            }
          }
        }
      })
      index = index + 1
      let newState = {
        currentState: 'edit',
        selectedElement: null,
        ignore: true
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.$store.dispatch('cms/pages/createPage', { name: `页面${index}`, ignore: true })
    }
  }
}
</script>

<style lang="scss" scoped>
.components-list-box {
  width: 260px;
  height: 100%;
}
.components-list-wrap {
  width: 100%;
}
.component-library {
  position: relative;
  height: 16px;
  span {
    position: relative;
    display: inline-block;
    font-size: 14px;
    font-weight: bold;
    line-height: 14px;
    color: #333;
    padding: 0 6px;
    border-left: 4px solid #037df3;
    background-color: #fff;
  }
}
.button-content {
  position: absolute;
  bottom: 0px;
  padding: 10px;
  border-top: 1px solid #eee;
  width: 100%;
  left: 0px;
  background: #fff;
}
/deep/ .el-tab-pane {
  height: calc(100vh - 200px);
  overflow-y: auto;
  overflow-x: hidden;
}
/deep/ .el-tabs__content {
  padding: 0px;
  overflow: hidden !important;
}
.page-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 290px;
  height: calc(100vh - 250px);
  overflow-x: hidden;
}
</style>
