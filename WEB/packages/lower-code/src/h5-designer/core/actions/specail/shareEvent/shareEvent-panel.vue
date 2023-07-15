<template>
  <div>
    <div class="event-cancel-wrap">
      <div class="action-name">
        <span>唤起分享</span>
      </div>
      <div class="delete-event-button floatRight">
        <h-icon name="android-close icon-android-close" @on-click="deleteEvents" :size="16" />
      </div>
    </div>
    <!-- <div class="props-wrapper"> -->
      <!-- <h-form :model="eventData.result.params">
        <h-form-item class="no-label" prop="wakeUpPop_title" >
          <h-input placeholder="请输入弹框标题，20字以内" :filterRE="/^\s/g" v-model.trim="eventData.result.params.wakeUpPop_title" :maxlength="20"  @on-change="onchange('wakeUpPop_title', $event)" />
        </h-form-item>
        <h-form-item class="no-label" prop="wakeUpPop_content" >
          <h-input type="textarea" placeholder="请输入弹框说明，200字以内"  :filterRE="/^\s/g" v-model.trim="eventData.result.params.wakeUpPop_content" :maxlength="200" @on-change="onchange('wakeUpPop_content', $event)" />
        </h-form-item>
      </h-form> -->
      <!-- <h-form :model="eventData.result.params"> -->
        <h-form :model="eventData.result.params" :rules="ruleInline" class="share-event">
          <div class="label-require" style="margin-top: 6px;">分享图片</div>
          <div style="margin-top: 6px;">
            <img-select v-model="eventData.result.params.share_event_img_url"  @input="(url)=>{updateData('share_event_img_url',url)}" :size="5120" description="支持png、jpg格式，大小为100*100px，图片大小不超过5MB" :accept="['png', 'jpg', 'jpeg']" :isShare="true" name="分享图片"></img-select>
          </div>
          <!-- <div style="margin-bottom: 6px">
            分享标题
          </div> -->
          <div style="margin-bottom: 6px">
            <h-form-item label='分享标题' prop="share_event_title">
              <h-input placeholder="请输入分享标题，20字以内" :filterRE="/[<>]/g" v-model="eventData.result.params.share_event_title" @on-change="onchange('share_event_title', $event)" :maxlength="20"></h-input>
            </h-form-item>
          </div>
          <!-- <div style="margin-bottom: 6px">
            分享内容
          </div> -->
          <div style="margin-bottom: 6px">
            <h-form-item label='分享内容' prop="share_event_content">
              <h-input placeholder="请输入分享内容，150字以内" :filterRE="/[<>]/g" type="textarea" v-model="eventData.result.params.share_event_content" @on-change="onchange('share_event_content', $event)" :maxlength="150"></h-input>
            </h-form-item>
          </div>
          <div class="label-require" style="margin-bottom: 6px;margin-top:10px">分享链接</div>
          <div style="margin-bottom: 6px">
            <h-radio-group v-model="eventData.result.params.share_event_url_type" @on-change="onchange('share_event_url_type',$event)">
              <h-radio label="page_url">
                <span>本作品链接</span>
              </h-radio>
              <h-radio label="custom_url">
                <span>自定义链接</span>
              </h-radio>
            </h-radio-group>
            <!-- <h-radio v-model="isShowUrl" @on-click="setShowUrl">自定义分享URL地址</h-radio> -->
          </div>
          <div style="margin-bottom: 6px" v-if="eventData.result.params.share_event_url_type === 'custom_url'">
            <h-form-item prop="share_event_page_url" class="cancel-require-icon">
              <h-input placeholder="请输入分享落地页链接" :filterRE="/[<>]/g" v-model="eventData.result.params.share_event_page_url" @on-change="onchange('share_event_page_url', $event)" :maxlength="150"></h-input>
            </h-form-item>
          </div>
        </h-form> 
    <!-- </div> -->
  </div>
</template>

<script>
import ImgSelect from '@Components/ImgSelect'
export default {
  name: 'shareEventPanel',
  components:{
    ImgSelect,
  },
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
      ruleInline: {
        // share_event_title: [{ required: true, message: "请输入分享标题", trigger: "blur" }],
        share_event_content: [{ required: true, message: "请输入分享内容", trigger: "blur" }],
        share_event_page_url: [{ required: true, message: "请输入分享落地页链接", trigger: "blur" }]
      }
    }
  },
  computed: {

  },
  created(){
    console.log('this.eventData',this.eventData)
  },
  methods: {
    onchange(key, e) {
      console.log('e.target.value',e)
      this.updateData(key, key === 'share_event_url_type'?e:e.target.value)
      if(key === 'share_event_url_type'){
        this.updateData('share_event_page_url', '')
      }
    },
    
    updateData(key, v){
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
            [key]: v
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
.label-require::before{
  content: "*";
  display: inline-block;
  margin-right: 2px;
  line-height: 0;
  font-size: 12px;
  color: #f5222d;
}
.cancel-require-icon /deep/.h-form-item-requiredIcon{
  display: none;
}
/deep/.h-form-item {
  margin-bottom: 5px;
}
.share-event /deep/.h-form-item-label{
  background: #fff;
}
</style>
