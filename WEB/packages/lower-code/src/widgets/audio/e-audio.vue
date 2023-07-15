<template>
  <div>
    <collapse-wrap name="音频设置">
      <div>音频源</div>
      <h-row style="margin-top: 5px;">
        <audio-select
          :fileType="['MP3', 'AAC', 'WAV']"
          :fileSize="200"
          accept=".mp3,.aac,.wav"
          @fileObj="fileObj"
          @input="setEleprops('audioName', '')"
          v-model="value.audioSrc"
        ></audio-select>
      </h-row>
      <h-row style="margin-top: 5px;">
        <h-col :span="21">是否循环播放</h-col>
        <h-col :span="3">
          <h-switch
            style="margin-left: 2px"
            v-model="value['loop']"
            size="small"
            @on-change="setEleprops('loop', $event)"
          ></h-switch>
        </h-col>
      </h-row>
      <h-row style="margin-top: 5px;" v-if="value.audioSrc">
        <h-col :span="6">音频文件名</h-col>
        <h-col :span="18" >
          {{value['audioName']}}
        </h-col>
      </h-row>
    </collapse-wrap>
    <collapse-wrap name="图标样式">
      <div class="color-panel">
        <ColorSelect
          padding="5px 7px"
          icon="plus-round"
          emptyHeight="26"
          emptyWidth="26"
          placement="right"
          :isReset="true"
          :selectedColor="propertyProxy['background']"
          :preColorList="['#418BF0', '#F0B442', '#48D93F', '#FF0000', '#0030FF']"
          @updateColor="setEleprops('background', $event)"
          @resetColor="setEleprops('background', 'rgba(255,255,255,1)')"
        />
      </div>
    </collapse-wrap>
  </div>
</template>

<script>
import CollapseWrap from '@Components/collapseWrap'
import AudioSelect from '@Components/AudioSelect'
import ImgUpload from '@Components/ImgUpload'
import errorImg from '@Root/assets/images/upload-error.png'
import ColorSelect from '@Components/ColorSelect'
export default {
  name: 'eAudioPropsConfig',
  components: {
    CollapseWrap,
    ImgUpload,
    AudioSelect,
    ColorSelect
  },
  props: ['context', 'selectedElementData'],
  data() {
    return {
      errorImg,
      v: null,
      c: null,
      imgbox: null,
      value: null,
      delay: 100 // 截取封面的延迟
    }
  },
  created() {
    this.value = this.selectedElementData.property
  },
  computed: {
    propertyProxy() {
      return new Proxy(this.selectedElementData.property, {
        get: (target, name) => {
          return this.selectedElementData.property[name] || ''
        },
        set: (target, name, value) => {
          if (name === 'background' && value) {
            value = `rgba(${value.rgba.r}, ${value.rgba.g}, ${value.rgba.b}, ${value.rgba.a})`
          }
          if (name === 'background' && !value) {
            value = ''
          }
          let { updateElementProperty } = this.context
          updateElementProperty({ [name]: value })
          return true
        }
      })
    }
  },
  watch: {
    selectedElementData: {
      handler(val) {
        this.value = val.property
      },
      deep: true
    }
  },
  methods: {
    // 设置选中元素属性
    setEleprops(key, value, type) {
      // 单选
      let { updateElementProperty } = this.context
      updateElementProperty({ [key]: value })
    },
    fileObj(item) {
      this.setEleprops('audioName', item.fileObj.fileName)
      this.setEleprops('audioSrc', item.fileObj.fileUrl)
    }
  }
}
</script>

<style lang="scss" scoped>
.color-picker-item {
  display: inline-block;
  width: 30px;
  height: 30px;
  border-radius: 2px;
  padding: 1px;
  border: 1px solid #ddd;
  .item {
    width: 26px;
    height: 26px;
    border-radius: 2px;
  }
}
.color-panel {
  position: relative;
  height: 30px;
  .color-picker {
    position: absolute;
    left: 2%;
    top: 31%;
  }
  .pre-color {
    position: absolute;
    left: 16%;
    top: 10%;
  }
}
.item1 {
  background: #418bf0;
}
.item2 {
  background: #f0b442;
}
.item3 {
  background: #48d93f;
}
.item4 {
  background: #ff0000;
}
.item5 {
  background: #0030ff;
}
/deep/ .vc-chrome .vc-chrome-toggle-btn {
  display: none;
}
</style>
