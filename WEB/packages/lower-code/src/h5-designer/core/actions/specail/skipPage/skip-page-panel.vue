<!--
 * @Author: your name
 * @Date: 2021-11-11 17:17:15
 * @LastEditTime: 2021-11-12 16:21:34
 * @LastEditors: your name
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \前端埋点\UCP-FRAME\src\biz\cms\src\editor\core\actions\specail\skipPage\skip-page-panel.vue
-->
<template>
  <div>
    <div class="event-cancel-wrap">
      <div class="action-name">
        <span>跳转作品页面</span>
      </div>
      <div class="delete-event-button floatRight">
        <h-icon name="android-close icon-android-close" @on-click="deleteEvents" :size="16" />
      </div>
    </div>
    <div class="props-wrapper">
       <h-select v-model="eventData.result.params.out_url"  placeholder="请选择跳转页面" :clearable="false" @on-change="onchange" >
        <h-option
          v-for="(item,index) in this.$store.state.cms.pages.items"
          :label="item.name"
          :value="item.uuid"
          :key="index"
          :disabled="disable(item.uuid)"
          >{{ item.name }}</h-option
        >
      </h-select>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SkipPanel',
  props: {
    eventData: {
      type: Object,
      default: () => {
      }
    },
    worksInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  data() {
    return {

    }
  },
  created(){
    console.log('this.eventData',this.eventData)
  },
  methods: {
    deleteEvents() {
      this.$emit('deleteEvents')
    },
    onchange(value) {
      // if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
        const selectedPage = this.$store.state.cms.pages.items.find(item => { return item.uuid == this.$store.state.cms.editState.selectedPage })
        const element = this.$store.state.cms.elements.items[this.$store.state.cms.editState.selectedPage].find(item => { return item.uuid == this.$store.state.cms.editState.selectedElement })
        const pageIndex = this.$store.state.cms.pages.items.findIndex(item => { return item.uuid == this.$store.state.cms.editState.selectedPage })
        this.$store.dispatch('cms/events/updateEvents', {
          uuid: this.eventData.uuid,
          result: {
            params: {
              pageIndex,
              worksInfo: { works_title: this.worksInfo.works_title, publish_date_time: this.worksInfo.publish_date_time, works_id: this.worksInfo.works_id },
              page: { uuid: selectedPage.uuid, name: selectedPage.name },
              element: { name: element.name, element_name: element.element_name },
              page_uuid: value
            }
          }
        })
      // } else {
      //   this.$store.dispatch('cms/events/updateEvents', {
      //     uuid: this.eventData.uuid,
      //     result: {
      //       params: {
      //         page_uuid: value
      //       }
      //     }
      //   })
      // }
    },
    disable(uuid) {
      const selectedPage = this.$store.state.cms.editState.selectedPage
      return uuid == selectedPage
    }
  }
}

</script>
