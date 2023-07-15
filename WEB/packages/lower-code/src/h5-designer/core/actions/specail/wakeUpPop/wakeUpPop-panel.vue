<template>
  <div>
    <div class="event-cancel-wrap">
      <div class="action-name">
        <span>唤起弹窗</span>
      </div>
      <div class="delete-event-button floatRight">
        <h-icon name="android-close icon-android-close" @on-click="deleteEvents" :size="16" />
      </div>
    </div>
    <div style="margin-top: 6px">
      <h-poptip trigger="hover" placement="left" transfer width="188">
        <img src="@Root/assets/images/preview-pop-icon.svg" class="preview-pop-icon"/>
        <div class="preview-pop" slot="content">
          <img src="@Root/assets/images/preview-exp.png" class="preview-exp"/>
          <p class="pop-title">{{eventData.result.params.wakeUpPop_title}}</p>
          <p class="txt">{{eventData.result.params.wakeUpPop_content}}</p>
          <div class="btn-box">我知道了</div>
        </div>
      </h-poptip>
      弹窗标题
    </div>
    <div style="margin-top: 6px">
      <h-input placeholder="请输入弹框标题，20字以内" :filterRE="/^\s/g" v-model.trim="eventData.result.params.wakeUpPop_title" :maxlength="20"  @on-change="onchange('wakeUpPop_title', $event)" />
    </div>
    <div style="margin-top: 6px">
      弹窗说明
    </div>
    <div style="margin-top: 6px">
      <h-input type="textarea" placeholder="请输入弹框说明，200字以内"  :filterRE="/^\s/g" v-model.trim="eventData.result.params.wakeUpPop_content" :maxlength="200" @on-change="onchange('wakeUpPop_content', $event)" />
    </div>
  </div>
</template>

<script>

export default {
  name: 'wakeUpPopPanel',
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
  created(){
    console.log('this.eventData',this.eventData)
  },
  methods: {
    onchange(key, e) {
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
    },
    deleteEvents() {
      this.$emit('deleteEvents')
    },
  }
}

</script>
<style lang="scss" scoped>
// /deep/.lightblue textarea.h-input {
//   line-height: 20px;
// }
/deep/ .h-poptip-body {
  padding:0;
}
.preview-exp{
  width: 42px;
  height: 17px;
  position: absolute;
  left: 4px;
  top: 0;
}
.preview-pop-icon{
  width: 16px;
  height: 16px;
  cursor: pointer;
  vertical-align: text-top;
  position: relative;
}
.preview-pop{
  width: 100%;
  .pop-title{
    text-align: center;
    font-size: 11px;
    // font-weight: 500;
    padding: 15px 15px 0;
    word-wrap:break-word;
    white-space:initial;
    font-weight: 600;
  }
  .txt{
    min-height: 32.5px;
    max-height: 182.5px;
    overflow-y: auto;
    padding: 8.5px 15px;
    word-wrap:break-word;
    white-space:initial;
    font-size: 9px;
  }
  .btn-box{
    text-align: center;
    height: 29px;
    line-height: 29px;
    font-size: 10px;
    color: #4686F2;
    border-top: 1px solid #EBEBEB;
    // font-weight: 400;
  }
}
</style>
