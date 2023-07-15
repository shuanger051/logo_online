<template>
  <div>
    <collapse-wrap name="视频设置">
      <div>视频配置方式</div>
      <h-row style="margin-top: 5px;">
        <h-radio-group v-model="propertyProxy.videoSourceType" @on-change="videoSourceTypeChange">
        <h-radio label="1">
            <span>上传视频</span>
          </h-radio>
          <h-radio label="2">
            <span>外链视频</span>
            <h-tooltip placement="left-start" transfer>
              <h-icon name="feedback_fill"></h-icon>
              <div slot="content" style="width: 240px">
                <div style="white-space: normal;margin-bottom: 10px">进入视频播放页，找到视频左下角<span style="color: #F14C5D;">分享</span>，点击<span style="color: #F14C5D">嵌入代码</span></div>
                <img :src="videoGuide" alt="" style="width: 100%">
              </div>
            </h-tooltip>
          </h-radio>
        </h-radio-group>
      </h-row>
      <div style="margin-top: 5px;">视频源</div>
      <h-row style="margin-top: 5px;" v-if="propertyProxy.videoSourceType === '1'">
        <videos-select
          :height="300"
          :fileSize="200"
          :fileType="['mp4']"
          accept=".mp4"
          @fileObj="fileObj"
          v-model="propertyProxy.videoSrc"
        ></videos-select>
      </h-row>
      <h-row style="margin-top: 5px;" v-if="propertyProxy.videoSourceType === '2'">
        <h-input v-model="propertyProxy.videoOutSrc" type="textarea" :rows="4" :canResize="false" placeholder="请复制【视频通用代码】至框中"></h-input>
      </h-row>
      <h-row style="margin-top: 5px;" v-if="propertyProxy.videoSourceType === '1'">
        <h-col :span="21">是否循环播放</h-col>
        <h-col :span="3" >
          <h-switch
            style="margin-left: 2px"
            v-model="value['loop']"
            size="small"
            @on-change="setEleprops('loop', $event)"
          ></h-switch>
        </h-col>
      </h-row>
      <h-row style="margin-top: 5px;" v-if="propertyProxy.videoSourceType === '1' && propertyProxy.videoSrc">
        <h-col :span="6">视频文件名</h-col>
        <h-col :span="18" >
          {{value['videoName']}}
        </h-col>
      </h-row>
    </collapse-wrap>
    <collapse-wrap name="视频封面" v-if="propertyProxy.videoSourceType === '1'">
      <h-row class="video-box">
        <img-select
          :size="5120"
          description="支持png、jpg、jpeg、gif格式，图片大小不超过5MB"
          :accept="['png', 'jpg', 'jpeg', 'gif']"
          :value="coverImgSrc"
          @input="getImgUrl"
          v-model="propertyProxy.poster"
        ></img-select>
      </h-row>
    </collapse-wrap>
  </div>
</template>

<script>
import CollapseWrap from '@Components/collapseWrap'
import ImgSelect from '@Components/ImgSelect'
import errorImg from '@Root/assets/images/upload-error.png'
import videoGuide from '@Root/assets/images/video-guide.png'

import VideosSelect from '@Components/VideoSelect'

export default {
  name: 'eVideoPropsConfig',
  components: {
    CollapseWrap,
    ImgSelect,
    // VideoUpload,
    VideosSelect
  },
  props: ['context', 'selectedElementData'],
  data() {
    return {
      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], // 播放速度
        autoplay: false, // 如果true,浏览器准备好时开始回放。
        muted: false, // 默认情况下将会消除任何音频。
        loop: false, // 导致视频一结束就重新开始。
        preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [{
          type: '', // 这里的种类支持很多种：基本视频格式、直播、流媒体等，具体可以参看git网址项目
          src: '' // url地址
        }],
        poster: '../../static/images/test.jpg', // 你的封面地址
        // width: document.documentElement.clientWidth, //播放器宽度
        notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true // 全屏按钮
        }
      },
      editorPropsConfig: this.context.editorPropsConfig,
      videoSrc: '',
      errorImg,
      videoGuide,
      v: null,
      c: null,
      imgbox: null,
      coverImgSrc: '',
      videoUrl: '',
      value: null
    }
  },
  created () {
    this.value = this.selectedElementData.property
  },
  watch: {
    selectedElementData: {
      handler(val) {
        this.value = val.property
      },
      deep: true
    }
  },
  computed: {
    propertyProxy() {
      return new Proxy(this.selectedElementData.property, {
        get: (target, name) => {
          return this.selectedElementData.property[name] || ''
        },
        set: (target, name, value) => {
          let { updateElementProperty } = this.context
          updateElementProperty({ [name]: value })
          return true
        }
      })
    }
  },
  methods: {
    // 上传返回视频地址
    fileObj(item) {
      this.setEleprops('videoName', item.fileObj.fileName)
      this.setEleprops('videoSrc', item.fileObj.fileUrl)
    },
    // 设置选中元素属性
    setEleprops(key, value, type) {
      // 单选
      let { updateElementProperty } = this.context
      updateElementProperty({ [key]: value })
    },
    getImgUrl(url) {
      // console.log(url)
      this.setEleprops('poster', url)
    },
    videoSourceTypeChange(val) {
      if (val === '2') {
        this.propertyProxy.loop = false
        this.propertyProxy.videoSrc = ''
        this.propertyProxy.videoName = ''
        this.propertyProxy.poster = ''
      }
      if (val === '1') {
        this.propertyProxy.videoOutSrc = ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.video-box {
  height: 200px;
}
</style>
