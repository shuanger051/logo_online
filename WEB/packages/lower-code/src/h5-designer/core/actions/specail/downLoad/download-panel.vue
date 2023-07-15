<template>
  <div>
    <div class="event-cancel-wrap">
      <div class="action-name">
        <span>跳转APP页面</span>
      </div>
      <div class="delete-event-button floatRight">
        <h-icon name="android-close icon-android-close" @on-click="deleteEvents" :size="16" />
      </div>
    </div>
    <div class="props-wrapper">
      <h-input placeholder="android跳转地址" :filterRE="/[<>]/g" :value="eventData.result.params.android_jump_url"
        @on-change="onchange('android_jump_url', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="android下载地址" :filterRE="/[<>]/g" :value="eventData.result.params.android_download_url"
        @on-change="onchange('android_download_url', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="ios跳转地址" :filterRE="/[<>]/g" :value="eventData.result.params.ios_jump_url"
        @on-change="onchange('ios_jump_url', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="ios下载地址" :filterRE="/[<>]/g" :value="eventData.result.params.ios_download_url"
        @on-change="onchange('ios_download_url', $event)" />
    </div>
    <!-- <div class="props-wrapper">
      <h-input placeholder="scheme标识" :filterRE="/[<>]/g" :value="eventData.result.params.scheme_url"
        @on-change="onchange('scheme_url', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="scheme-host地址" :filterRE="/[<>]/g" :value="eventData.result.params.scheme_host"
        @on-change="onchange('scheme_host', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="scheme-post地址" :filterRE="/[<>]/g" :value="eventData.result.params.scheme_post"
        @on-change="onchange('scheme_post', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="outChain-指定路径" :filterRE="/[<>]/g" :value="eventData.result.params.outChain_path"
        @on-change="onchange('outChain_path', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="outChain-指定参数" :filterRE="/[<>]/g" :value="eventData.result.params.outChain_key"
        @on-change="onchange('outChain_key', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="appstore下载地址" :filterRE="/[<>]/g" :value="eventData.result.params.appstore_url"
        @on-change="onchange('appstore_url', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="应用宝下载地址" :filterRE="/[<>]/g" :value="eventData.result.params.yingyongbao_url"
        @on-change="onchange('yingyongbao_url', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="intent-package名称：com.xx.xxx.xxxx" :filterRE="/[<>]/g" :value="eventData.result.params.intent_package"
        @on-change="onchange('intent_package', $event)" />
    </div>
    <div class="props-wrapper">
      <h-input placeholder="唤起失败默认加载页面" :filterRE="/[<>]/g" :value="eventData.result.params.fallback_url"
        @on-change="onchange('fallback_url', $event)" />
    </div> -->
  </div>
</template>
<script>

export default {
  name: 'DownloadPanel',
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
  computed: {

  },
  methods: {
    onchange(key, e) {
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
              [key]: e.target.value
            }
          }
        })
      // } else {
      //   this.$store.dispatch('cms/events/updateEvents', {
      //     uuid: this.eventData.uuid,
      //     result: {
      //       params: {
      //         [key]: e.target.value
      //       }
      //     }
      //   })
      // }
    },
    deleteEvents() {
      this.$emit('deleteEvents')
    }
  }
}
</script>
