<template>
  <div v-if="worksInfo && worksInfo.link_url" class="preview_info">
    <titleBar title="作品信息" />
    <h-row class="line" v-if="worksInfo && worksInfo.audit">
      <h-col :span="6" class="leftPanel">审核状态</h-col>
      <h-col :span="12">{{ worksInfo.audit }}</h-col>
    </h-row>
    <h-row class="line">
      <h-col :span="6" class="leftPanel">作品名称</h-col>
      <h-col :span="12" style="font-weight: bold;font-size: 16px;">{{ worksInfo ? worksInfo.works_title : '--' }}
      </h-col>
    </h-row>
    <h-row class="line">
      <h-col :span="6" class="leftPanel">{{ worksInfo && worksInfo.works_status === 'D' ? '分享二维码' : '预览二维码' }}</h-col>
      <h-col :span="12">
        <img :src="qrcodeUrl" alt="" class="qr-code-img">
      </h-col>
    </h-row>
    <h-row class="line">
      <h-col :span="6" class="leftPanel">{{ worksInfo && worksInfo.works_status === 'D' ? '分享链接' : '测试链接' }}</h-col>
      <h-col :span="12">{{ worksInfo ? worksInfo.link_url : '--' }}</h-col>
      <h-col class="copy-btn">
        <span @click="copyText(worksInfo.link_url)">
          <h-icon name="ios-copy-outline">
          </h-icon>
        </span>
      </h-col>
    </h-row>
    <div class="warn" v-if="isTest">
      <div>预览二维码和测试链接仅限于查看编辑效果，</div>
      <div>{{contentInfo}}</div>
    </div>
    <spn v-if="showShareInfo">
      <h-row class="line">
        <h-col :span="6" class="leftPanel">分享图片</h-col>
        <h-col :span="12">
          <img :src="share_info.share_img_url" alt="">
        </h-col>
      </h-row>
      <h-row class="line">
        <h-col :span="6" class="leftPanel">分享标题</h-col>
        <h-col :span="12">{{ share_info.share_title || '--' }}
        </h-col>
      </h-row>
      <h-row class="line">
        <h-col :span="6" class="leftPanel">分享内容</h-col>
        <h-col :span="12">{{ share_info.share_content || '--' }}
        </h-col>
      </h-row>
      <h-row class="line">
        <h-col :span="6" class="leftPanel">分享URL地址</h-col>
        <h-col :span="12">{{ share_info.share_page_url || '--' }}
        </h-col>
      </h-row>
    </spn>
    <h-row class="line bottomLine">
      <h-col :span="6" class="leftPanel">作品有效期: </h-col>
      <h-col :span="18">
        <div v-if="begin_valid_date_time && end_valid_date_time" style="font-size:12px;">
          {{begin_valid_date_time}} - {{end_valid_date_time}}
        </div>
        <div v-else>
          长期有效
        </div>
      </h-col>
    </h-row>
  </div>
</template>

<script>
import { getWorksLinkExpiration } from '@Apis/works.js'
import titleBar from '@Components/titleBar'
import { copyText, dateTimeFormat } from '@Utils/utils'

export default {
  props: ['worksInfo', 'qrcodeUrl'],
  components: {
    titleBar
  },
  name: '',
  data() {
    return {
      showShareInfo: window.CMS_CONFIG.SHOW_SHARE && window.CMS_CONFIG.SHOW_SHARE == 'true',
      begin_valid_date_time: null,
      end_valid_date_time: null,
      isTest: '',
      contentInfo: '',
      share_info:{
        share_img_url:'',
        share_title:'',
        share_content:'',
        share_page_url:''
      }
    }
  },
  watch: {
    worksInfo(value) {
      this.setShareInfo(value.works_content)
      this.isTest = value.works_status !== 'D'
      if (value.begin_valid_date_time != 0 && value.end_valid_date_time != 0) {
        this.begin_valid_date_time = dateTimeFormat(parseInt(value.begin_valid_date_time), '.')
        this.end_valid_date_time = dateTimeFormat(parseInt(value.end_valid_date_time), '.')
      }
      if (!value.works_id) {
        this.$hMessage.error('作品id不存在，信息存在缺少')
        return false
      }
      if (value.works_status !== 'D') {
        getWorksLinkExpiration({ works_id: value.works_id }).then(res => {
          this.contentInfo = `${res.data.date_validdate}小时内会失效。请勿对外分享。`
        })
      }
    }
  },
  methods: {
    // 复制文本
    copyText(text) {
      copyText(text)
    },
    // 生成二维码
    genQRCodeUrl() {
      const url = worksInfo.link_url
      QRCode.toDataURL(url, (err, url) => {
        if (err) console.log(err)
        this.qrcodeUrl = url
      })
    },
    setShareInfo(data){
      if(data){
        let worksContent = JSON.parse(data).works || {}
        this.share_info = {
          share_img_url:worksContent.share_img_url && worksContent.share_img_url!==' ' ? worksContent.share_img_url : '',
          share_title:worksContent.share_title && worksContent.share_title!==' ' ? worksContent.share_title : '',
          share_content:worksContent.share_content && worksContent.share_content!==' ' ? worksContent.share_content : '',
          share_page_url:worksContent.share_page_url && worksContent.share_page_url!== ' ' ? worksContent.share_page_url : ''
        }
      }
    }
  },
  created() {
    this.setShareInfo(this.worksInfo.works_content)
    this.begin_valid_date_time = dateTimeFormat(parseInt(this.worksInfo.begin_valid_date_time), '.')
    this.end_valid_date_time = dateTimeFormat(parseInt(this.worksInfo.end_valid_date_time), '.')
    this.isTest = this.worksInfo.works_status !== 'D'
    if (!this.worksInfo.works_id) {
      this.$hMessage.error('作品id不存在，信息存在缺少')
      return false
    }
    if (this.worksInfo.works_status !== 'D') {
      getWorksLinkExpiration({ works_id: this.worksInfo.works_id }).then(res => {
        this.contentInfo = `${res.data.date_validdate}小时内会失效。请勿对外分享。`
      })
    }
  }
}
</script>

<style scoped lang="scss">
.preview_info{
  height: 710px;
  overflow: auto;
}

.line {
  margin-bottom: 20px;
  margin-left: 30px;
  font-size: 14px;
  .leftPanel {
    background: #f7f7f7;
  }
}
.warn {
  margin-top: -10px;
  padding-left: 31%;
  margin-bottom: 20px;
}
.shareImg{
  height: 200px;
  width: 200px;
}
.bottomLine{
  margin-bottom: 0;
}

</style>
