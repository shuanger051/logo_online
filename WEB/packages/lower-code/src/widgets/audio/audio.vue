<template>
  <div>
    <div v-if="context.mode === 'edit'" class="audio-box">
      <div class="bg-music-wrapper" style="display: block;" :style="{
          ...objProperty
        }">
        <div
          class="bg-music-btn rotate"
          :style="{
            'animation-play-state': isPlaying ? 'running' : 'paused'
          }"
          :disabled="disabled"
        >
          <audio :src="objProperty['audioSrc']" :autoplay="false" preload :loop="objProperty['loop']" ref='bgAudio'>
            您的浏览器不支持 音频 元素
          </audio>
        </div>
      </div>
    </div>
    <div v-else class="audio-box">
      <div class="bg-music-wrapper" style="display: block;" :style="{...objProperty}">
        <div
          class="bg-music-btn rotate"
          :style="{
            'animation-play-state': isPlaying ? 'running' : 'paused'
          }"
          @click="toggle"
          :disabled="disabled"
        >
          <audio @ended="ended" :id="objProperty['uuid']" :src="objProperty['audioSrc']" :autoplay="false" preload :loop="objProperty['loop']" ref='bgAudio'>
            您的浏览器不支持 音频 元素
          </audio>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import defaultImg from '@Root/assets/images/default.png'
import { mapValues } from 'lodash'
import { trackFunc } from '@Utils/appSDK'

export default {
  props: ['name', 'context', 'property', 'style'],
  data() {
    return {
      src: '',
      defaultImg: defaultImg,
      mode: '',
      objProperty: '',
      isPlaying: false
    }
  },
  created() {
    this.mode = this.context.mode
    this.objProperty = mapValues(this.property, (value, key) => {
      return value
    })
    // console.log(this.objProperty)
    // Object.assign(this.objProperty, this.style)
  },
  watch: {
    property: {
      handler(val) {
        this.objProperty = mapValues(val, (value, key) => {
          return value
        })
        // console.log(this.objProperty)
        // Object.assign(this.objProperty, this.style)
      },
      deep: true
    }
  },
  methods: {
    toggle () {
      let bgAudio = this.$refs.bgAudio
      if (!bgAudio) return
      this.isPlaying ? bgAudio.pause() : bgAudio.play()
      this.isPlaying = !this.isPlaying
      if (this.isPlaying) {
        if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
          let page_icon_section = '音频播放'
          trackFunc('Operactmainpage_icon_click_hs', {
            page_icon_section,
            audioName: this.objProperty.audioName,
            audioSrc: this.objProperty.audioSrc
          })
        }
      }
    },
    ended() {
      this.isPlaying = false
    }
  }
}
</script>
<style scoped lang="scss">
.audio-box {
  position: relative;
  width: 100%;
  height: 100%;
  audio {
    width: 100%;
    height: 100%;
  }
}
.circle {
  width: 23px;
  height: 23px;
  background: #959595;
  border-radius: 23px;
}
@keyframes rotating {
  0% {
    transform: rotate(0deg)
  }

  to {
    transform: rotate(1turn)
  }
}

@mixin bg-music-icon {
  position: absolute;
  width: 30px;
  height: 30px
}

.bg-music-wrapper {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  @include bg-music-icon;

  .bg-music-btn {
    @include bg-music-icon;

    right: 0;
    top: 0;
    border-radius: 15px;
    background-image: url(~@Assets/images/bg-music.svg);
    background-size: contain;
    background-repeat: no-repeat;

    &.rotate {
      animation: rotating 1.2s linear infinite;
    }
  }
}

</style>
