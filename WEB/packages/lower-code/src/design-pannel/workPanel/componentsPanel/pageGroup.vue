<template>
  <div class="page-content">
    <div class="page-wrap">
      <div v-for="(item, index) in this.$store.state.cms.pages.items" :key="index" class="page-box"
        :class="{ 'selectedPage': selectedPage == item.uuid }" @mouseenter="mouseEnter(index)" @mouseleave="mouseLeave"
        @click="selectPage(item)">

        <!-- 页面标题 -->
        <div style="display: flex;align-items:center">
          <div style="display: flex;align-items:center">
            <img v-if="index === 0" :src="require('@Root/assets/images/indexPage.svg')" width="16" height="16">
            <div class="page-name" :title="item.name">{{item.name}}</div>
          </div>
          <div v-if="item.passValidate == false">
            <h-tooltip content="页面配置未完成，请完成组件配置" placement="top" :transfer="true">
              <h-icon name="information-circled"
                style="width: 16px;height: 16px;color: #F14C5D; margin-left: 8px; margin-top: 4px">
              </h-icon>
            </h-tooltip>
          </div>
        </div>

        <div style="display: flex" v-show="index == showIndex">
          <!-- 设置主页 -->
          <div v-if="index !== 0 && ((item.property && item.property.type !=='formResultPage') || !item.property)"
            @mouseenter="iconMouseEnter('indexPage')" @mouseleave="iconMouseLeave('indexPage')"
            @click="setIndexPage(item, index, $event)">
            <h-tooltip content="设置为主页" placement="top" :transfer="true">
              <img v-show="indexPageActive" style="margin-right: 6px"
                :src="require('@Root/assets/images/setIndexPageActive.svg')" width="16" height="16">
              <img v-show="!indexPageActive" style="margin-right: 6px"
                :src="require('@Root/assets/images/setIndexPage.svg')" width="16" height="16">
            </h-tooltip>
          </div>

          <!-- 复制页面 -->
          <div v-if="(item.property && item.property.type !=='formResultPage') || !item.property"
            @mouseenter="iconMouseEnter('copyPage')" @mouseleave="iconMouseLeave('copyPage')"
            @click="copyPage(item, index, $event)">
            <img v-show="copyPageActive" style="margin-right: 6px"
              :src="require('@Root/assets/images/copyPageActive.svg')" width="16" height="16">
            <img v-show="!copyPageActive" style="margin-right: 6px" :src="require('@Root/assets/images/copyPage.svg')"
              width="16" height="16">
          </div>

          <!-- 删除页面 -->
          <div v-if="index !== 0 && ((item.property && item.property.type !=='formResultPage') || !item.property)"
            @mouseenter="iconMouseEnter('deletePage')" @mouseleave="iconMouseLeave('deletePage')"
            @click="deletePage(item, index, $event)">
            <img v-show="deletePageActive" :src="require('@Root/assets/images/deletePageActive.svg')" width="16"
              height="16">
            <img v-show="!deletePageActive" :src="require('@Root/assets/images/deletePage.svg')" width="16" height="16">
          </div>

        </div>
      </div>
    </div>

    <h-msg-box v-model="showFixName" :mask-closable="false" :closable="false"
      class="preview-dialog" title="页面名称过长，请修改在30字以内">
      <h-row>
        <h-col :span="3" style="line-height: 2.5;">页面名称:</h-col>
        <h-col :span="21">
          <h-input v-model="newName" :filterRE="/[<>]/g" :maxlength="30" placeholder="最大输入30位"></h-input>
        </h-col>
      </h-row>
      <div slot="footer" style="text-align:right;">
        <h-button type="ghost" style="width:80px;" @click="cancelFixName">取消</h-button>
        <h-button type="primary" style="width:80px;" @click="sureName">确定</h-button>
      </div>
    </h-msg-box>
  </div>
</template>

<script>
import { cloneDeep } from 'lodash'
import { mapState, mapGetters } from 'vuex'
import { generateUID } from '@h5Designer/utils'

export default {
  name: 'pageGroup',
  props: ['pages'],
  data() {
    return {
      showIndex: null,
      indexPageActive: false,
      copyPageActive: false,
      deletePageActive: false,
      showFixName: false,
      newName: '',
      pageItem: {}
    }
  },
  methods: {
    async copyPage(item, index, event) {
      event.stopPropagation()
      let pageIndex = 0
      const { cms } = this.$store.state
      let elements = cloneDeep(cms.elements.items[item.uuid])
      let allElements = cloneDeep(cms.elements.items)
      let pages = cms.pages.items
      let hasFundListNum = 0
      Object.keys(allElements).forEach(pageUUID=>{
        allElements[pageUUID].forEach(item=>{
           if (item.name === 'hs-cms-business-fundList') {
            hasFundListNum+=1
          }
        })
      })
      // 有提交按钮，不允许复制页面，一个作品只有一个提交按钮
      if (elements) {
        let hasSubmitBtn = false
        let hasSmsVerifiCode = false
        let fundListNum = 0
        elements.forEach(item => {
          if (item.name === 'hs-cms-form-submit') {
            hasSubmitBtn = true
          }
          if (item.name === 'hs-cms-form-smsVerifiCode') {
            hasSmsVerifiCode = true
          }
          if (item.name === 'hs-cms-business-fundList') {
            fundListNum+=1
          }
          if (item.name === 'hs-cms-tabs') {
            let tempTabList = item.property.tabList
            for (let l = 0; l < tempTabList.length; l++) {
              tempTabList[l].elementContent = []
              tempTabList[l].canUseElement = []
            }
          }
        })
        if (hasSubmitBtn) {
          return this.$hMessage.error('一个作品只允许添加一个表单提交按钮')
        }
        if (hasSmsVerifiCode) {
          return this.$hMessage.error('一个作品只允许添加一个短信验证码')
        }
         if (fundListNum+hasFundListNum>5) {
          return this.$hMessage.error('一个作品最多添加5个产品列表组件')
        }
      }
      pages.forEach((j) => {
        if (j.name.indexOf(`${item.name}-副本`) == 0) {
          let newNameIndex = j['name'].substr(item.name.length + 3)
          if (Number(newNameIndex) + '' !== NaN + '') {
            if (newNameIndex > pageIndex) {
              pageIndex = parseInt(newNameIndex)
            }
          }
        }
        console.log(9999,j)
      })
      pageIndex = pageIndex + 1
      // console.log('${item.name}-副本${pageIndex}',`${item.name}-副本${pageIndex}`)
      this.newName = `${item.name}-副本${pageIndex}`
      if (this.newName.length > 30) {
        this.pageItem = cloneDeep(item)
        this.pageItem.index = index
        this.showFixName = true
        return
      }
      this.creatNewPage(cloneDeep(item), index, elements)
      // let newState = {
      //   mode: 'edit',
      //   selectedElement: null,
      //   ignore: true
      // }
      // await this.$store.dispatch('cms/editState/updateEditState', newState)
      // await this.$store.dispatch('cms/pages/createPage', { name: `${item.name}-副本${pageIndex}`, index: index + 1, style: item.style, ignore: true })
      // await elements && elements.forEach(j => {
      //   if (j.name.indexOf('hs-cms-form') === -1) {
      //     if (j.name !== 'hs-cms-getCoupons' || j.name !== 'hs-cms-getDongBeiCoupons') {
      //       j.uuid = generateUID()
      //       this.$store.dispatch('cms/elements/addElement', j)
      //     }
      //   }
      // })
      // this.$store.$$init()
    },
    async creatNewPage(item, index, elements) {
      let newState = {
        mode: 'edit',
        selectedElement: null,
        ignore: true
      }
      await this.$store.dispatch('cms/editState/updateEditState', newState)
      await this.$store.dispatch('cms/pages/createPage', { name: this.newName, index: index + 1, style: item.style, ignore: true })
      await elements && elements.forEach(j => {
        if (j.name.indexOf('hs-cms-form') === -1) {
          // if (j.name !== 'hs-cms-getCoupons' || j.name !== 'hs-cms-getDongBeiCoupons') {
          j.uuid = generateUID()
          this.$store.dispatch('cms/elements/addElement', j)
          // }
        }
      })
      this.$store.$$init()
    },
    async deletePage(item, index, event) {
      event.stopPropagation()
      let { cms } = this.$store.state
      let elements = cms.elements.items[item.uuid]
      let pages = cms.pages.items
      if (elements && elements.length > 0) {
        this.$hMsgBox.confirm({
          title: '提示',
          content: `页面删除后，已有内容无法恢复，是否确认删除？`,
          okText: `继续删除`,
          cancelText: `返回`,
          onOk: async () => {
            // 删除有表单提交按钮的页面，同时删除表单结果页
            let flag = false
            elements.forEach(item => {
              if (item.name === 'hs-cms-form-submit' && item.property.showSubmitResult === '1') {
                flag = true
              }
            })
            if (flag) {
              let { cms } = this.$store.state
              let pages = cms.pages.items
              let page = pages.find((item) => item.property.type === 'formResultPage')
              let index = pages.findIndex((item) => item.property.type === 'formResultPage')
              let elements = cms.elements.items[page.uuid]
              if (elements && elements.length > 0) {
                await this.$store.dispatch('cms/pages/deletePage', { index, ignore: true })
                await this.$store.dispatch('cms/elements/deleteElementsInpage', { uuid: page.uuid, ignore: true })
                await this.$store.dispatch('cms/events/deleteEventsInpage', { uuid: page.uuid, ignore: true })
              } else {
                await this.$store.dispatch('cms/pages/deletePage', { index, ignore: true })
              }
            }
            let newState = {
              selectedElement: null,
              ignore: true
            }
            if (item.uuid == cms.editState.selectedPage) {
              newState.selectedPage = pages[0].uuid
            }
            await this.$store.dispatch('cms/pages/deletePage', { index, ignore: true })
            await this.$store.dispatch('cms/elements/deleteElementsInpage', { uuid: item.uuid, ignore: true })
            await this.$store.dispatch('cms/events/deleteEventsInpage', { uuid: item.uuid, ignore: true })
            this.$store.dispatch('cms/editState/updateEditState', newState)
          },
          onCancel: () => {
          }
        })
      } else {
        let newState = {
          selectedElement: null,
          ignore: true
        }
        if (item.uuid == cms.editState.selectedPage) {
          newState.selectedPage = pages[0].uuid
        }
        await this.$store.dispatch('cms/pages/deletePage', { index, ignore: true })
        this.$store.dispatch('cms/editState/updateEditState', newState)
        this.$store.$$init()
      }
    },
    mouseEnter(index) {
      this.showIndex = index
    },
    mouseLeave(index) {
      this.showIndex = null
    },
    iconMouseEnter(name) {
      switch (name) {
        case 'indexPage':
          this.indexPageActive = true
          break
        case 'copyPage':
          this.copyPageActive = true
          break
        case 'deletePage':
          this.deletePageActive = true
          break
      }
    },
    iconMouseLeave(name) {
      switch (name) {
        case 'indexPage':
          this.indexPageActive = false
          break
        case 'copyPage':
          this.copyPageActive = false
          break
        case 'deletePage':
          this.deletePageActive = false
          break
      }
    },
    selectPage(page) {
      let newState = {
        currentState: 'edit',
        selectedPage: page.uuid,
        selectedElement: null,
        ignore: true
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.$store.$$init()
    },
    setIndexPage(item, index, event) {
      event.stopPropagation()
      this.$store.dispatch('cms/pages/setIndexPage', { index, uuid: item.uuid, ignore: true })
      this.$forceUpdate()
    },
    cancelFixName() {
      this.showFixName = false
    },
    sureName() {
      const { cms } = this.$store.state
      let names = cms.pages.items.map(item => item.name)
      if (this.newName.length > 30) {
        this.$hMessage.warning('页面名称请控制在30字以内')
        return
      }
      if (names.indexOf(this.newName) > -1) {
        this.$hMessage.warning('页面名称不可重复')
        return false
      }
      let elements = cloneDeep(cms.elements.items[this.pageItem.uuid])
      this.creatNewPage(this.pageItem, this.pageItem.index, elements)
      this.showFixName = false
    }
  },
  computed: {
    ...mapState('cms/editState', [
      'selectedPage'
    ]),
    ...mapGetters('cms/elements', [
      'selectedPageElements'
    ]),
    ...mapGetters('cms/events', [
      'selectedElementEvents'
    ])
  },
  watch: {
    'selectedPageElements': {
      async handler(value) {
        const { cms } = this.$store.state
        const pages = cms.pages.items
        await pages && pages.forEach(item => {
          let elements = cms.elements.items[item.uuid]
          item.passValidate = true
          elements && elements.forEach((j) => {
            if (j.property && j.property.passValidate) {
              for (var k in j.property.passValidate) {
                if (j.property.passValidate[k] == false) {
                  item.passValidate = false
                }
              }
            }
          })
        })
        this.$forceUpdate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page-content {
  width: 290px;
  height: 100%;
}
.page-box {
  width: 290px;
  height: 48px;
  border-bottom: 1px solid #eee;
  padding: 0px 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.selectedPage {
  background: #dce9ff;
}
.page-name {
  max-width: 133px;
  font-size: 12px;
  height: 16px;
  line-height: 16px;
  margin-left: 8px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
</style>
