<template>
  <div>
    <div v-if="context.mode==='edit'" class="video-box">
      <div v-if="objProperty.videoSourceType === '1'" class="video-img-box">
        <img :src="objProperty['poster'] ? objProperty['poster'] : defaultVideo" alt=""  @dblclick="showVideoUpload"/>
        <!-- 上传视频 -->
        <videos-upload
            v-show="showupload"
            ref="videosUploadRef"
            class="video-upload"
            :fileSize="200"
            :fileType="['mp4']"
            accept=".mp4"
            v-model="objProperty['videoSrc']"
            @fileObj="updateFunc"
            :alwaysUpload="true"
        ></videos-upload>
      </div>
      <div class="video-img-box" v-else>
        <img :src="defaultVideo2" alt=""/>
      </div>
    </div>
    <div v-else class="video-box">
      <!-- <video
        ref="videoId"
        :id="objProperty['uuid']"
        :autoplay="false"
        controls
        webkit-playsinline
        playsinline
        x5-playsinline
        x5-video-player-type='h5'
        x5-video-player-fullscreen="true"
        :src="objProperty['videoSrc']"
        :loop="objProperty['loop']"
        :poster="objProperty['poster']"
        :style="{...objProperty}"
        preload
      >
      </video> -->
      <div
        class="video-box-contents"
        :style="{...objProperty}"
        @click="playClick"
        v-if="objProperty.videoSourceType === '1'"
      >
        <span class="video-play" v-if="showPlayBtn && showBg"></span>
        <div
          v-if="showBg"
          class="video-poster"
          :style="objProperty['poster'] ? `background: url(${objProperty['poster']}) no-repeat 0 0/100% 100%;` : 'background: #000'"
        ></div>
        <video
          ref="videoRef"
          width="100%"
          height="100%"
          controls
          playsinline
          x5-playsinline
          x5-video-player-type='h5'
          x5-video-player-fullscreen="true"
          webkit-playsinline="true"
          :loop="objProperty['loop']"
          :style="{
            opacity: (showBg ? 0 : 1)
          }"
        >
          <source type="video/mp4" :src="objProperty['videoSrc']">
        </video>
      </div>
      <div class="iframe-box" v-if="objProperty.videoSourceType === '2'">
        <div v-if="objProperty.videoOutSrc" v-html="/<\/iframe>/g.test(objProperty.videoOutSrc) ? objProperty.videoOutSrc : decodeURIComponent(objProperty.videoOutSrc)"></div>
        <div v-else style="height: 100%" @click="playClick">
          <span class="video-play" v-if="showPlayBtn && showBg"></span>
          <div
            v-if="showBg"
            class="video-poster"
            :style="objProperty['poster'] ? `background: url(${objProperty['poster']}) no-repeat 0 0/100% 100%;` : 'background: #000'"
          ></div>
          <video
            ref="videoRef"
            width="100%"
            height="100%"
            controls
            playsinline
            x5-playsinline
            x5-video-player-type='h5'
            x5-video-player-fullscreen="true"
            webkit-playsinline="true"
            :loop="objProperty['loop']"
            :style="{
            opacity: (showBg ? 0 : 1)
          }"
          >
            <source type="video/mp4" :src="objProperty['videoOutSrc']">
          </video>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import defaultVideo from '@Root/assets/images/defaultVideo.png'
import defaultVideo2 from '@Root/assets/images/defaultVideo2.png'
import { mapValues } from 'lodash'
import VideosUpload from '@Components/VideoUpload'
import { trackFunc } from '@Utils/appSDK'

export default {
  props: ['name', 'property', 'context', 'style', 'active'],
  components: {
    VideosUpload
  },
  data () {
    return {
      src: '',
      defaultVideo: defaultVideo,
      defaultVideo2: defaultVideo2,
      objProperty: {},
      showPlayBtn: true, // 是否展示播放按钮
      showBg: true, // 展示封面
      showupload: false // 展示上传
    }
  },
  created() {
    this.mode = this.context.mode
    this.init(this.property)
  },
  watch: {
    property: {
      handler(val) {
        this.init(val)
      },
      deep: true
    },
    active: {
      handler(val) {
        // 本组建选中取消
        if (val && this.showupload) {
          this.showupload = false
        }
      },
      deep: true
    }
  },
  methods: {
    // 初始化数据
    init(val) {
      this.objProperty = mapValues(val, (value, key) => {
        return value
      })
      Object.assign(this.objProperty, this.style)
    },
    // 点击播放
    playClick() {
      if (this.showPlayBtn && this.showBg) {
        this.showPlayBtn = false
        this.showBg = false
        this.$refs.videoRef.play()
        if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
          let page_icon_section = '视频播放'
          trackFunc('Operactmainpage_icon_click_hs', {
            page_icon_section,
            videoSourceType: this.objProperty.videoSourceType,
            poster: this.objProperty.poster,
            videoName: this.objProperty.videoName,
            videoSrc: this.objProperty.videoSrc
          })
        }
      }
    },
    // 更新视频
    updateFunc(data) {
      this.showupload = false
      const nameSrc = 'videoSrc'
      const valueSrc = data.fileObj.fileUrl || ''
      const name = 'videoName'
      const value = data.fileObj.fileName || ''
      let { updateElementProperty } = this.context
      updateElementProperty({
        [name]: value,
        [nameSrc]: valueSrc
      })
    },
    // 双击唤醒上传
    showVideoUpload() {
      this.showupload = true
      this.$refs.videosUploadRef.uploadFunc()
    }
  }
}
</script>
<style scoped lang="scss">
  .video-box {
    position: relative;
    width: 100%;
    height: 100%;
    .video-img-box{
      width: 100%;
      height: 100%;
    }

    img {
      width: 100%;
      height: 100%;
    }
    .iframe-box {
      height: 100%;
      .video-play {
        position: absolute;
        top: 50%;
        left: 50%;
        width: 36px;
        height: 36px;
        transform: translate(-50%, -50%);
        z-index: 2;
        background: url('~@Root/assets/images/icon-play.png') no-repeat;
        background-size: 100% 100%;
      }

      .video-poster {
        position: absolute;
        z-index: 1;
        pointer-events: none;
        width: 100%;
        height: 100%;
        background-size: 100% 100%;
      }
      /deep/ iframe {
        height: 100%;
        width: 100%;
      }
    }
  }

  // 视频播放
  .video-box-contents {
    .video-play {
      position: absolute;
      top: 50%;
      left: 50%;
      width: 36px;
      height: 36px;
      transform: translate(-50%, -50%);
      z-index: 2;
      background: url('~@Root/assets/images/icon-play.png') no-repeat;
      background-size: 100% 100%;
    }

    .video-poster {
      position: absolute;
      z-index: 1;
      pointer-events: none;
      width: 100%;
      height: 100%;
      background-size: 100% 100%;
    }
  }
  // 视频上传
  .video-upload {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2;
    width: 100%;
    height: 100%;
    opacity: 0;

    /deep/ .img-upload-wrap {
      width: 100%;
      height: 100%;
    }

    /deep/ .file-upload {
      width: 100%;
      height: 100%;
    }
  }
</style>
