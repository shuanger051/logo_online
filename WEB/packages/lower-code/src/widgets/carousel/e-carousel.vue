<template>
  <div>
    <collapse-wrap name="切换方式">
      <h-radio-group v-model="propertyProxy.auto_play" type="button" size="small">
        <h-radio label="2">手动</h-radio>
        <h-radio label="1" :style="{'margin-left': '8px', 'border-left': '1px solid #d7dde4'}">自动</h-radio>
      </h-radio-group>
      <div v-if="propertyProxy.auto_play == 1">
        切换时间
      </div>
      <div v-if="propertyProxy.auto_play==1">
        <h-slider v-model="propertyProxy.switch_time" :min="1" :max="15" :tip-format="format"></h-slider>
      </div>
    </collapse-wrap>
    <collapse-wrap name="图片比例">
      <h-radio-group v-model="propertyProxy.scale_rate" @on-change="changeHeight" size="small">
        <h-radio v-for="i in scaleList" :key="i.value" :label="i.value"> {{ i.label }}</h-radio>
      </h-radio-group>
    </collapse-wrap>
    <collapse-wrap name="图片配置">
      <div style="display:flex; margin: 6px 0">
        <h-icon name="minus-round icon-minus-round" @on-click="deleteImg" class="minus-button"
          style="margin-right: 10px"></h-icon>
        <el-radio-group v-model="propertyProxy['activeIndex']" size="small">
          <el-radio-button v-for="(i, index) in propertyProxy.imgList" :key="i.uuid" :label="index+1">{{index + 1}}
          </el-radio-button>
        </el-radio-group>
        <h-icon name="plus-round sicon-plus-round" @on-click="addImg" class="plus-button" style="margin-left: 10px">
        </h-icon>
      </div>
      <div v-for="(item, index) in imgList" :key="item.uuid">
        <template v-if="(index + 1) == propertyProxy['activeIndex']">
          <img-select v-model="item.src" @input="getImgUrl" :class="`index${index}`" :size="5120"
            description="支持png、jpg、jpeg、gif格式，图标大小不超过5MB" :accept="['png', 'jpg', 'jpeg', 'gif']"></img-select>
          <div>
            点击动作
          </div>
          <div>
            <h-radio-group size="small" @on-change="changeActionType" v-model="item.action_type">
              <h-radio v-for="i in clickEventList" :key="i.type" :label="i.type">{{i.name}}</h-radio>
            </h-radio-group>
          </div>
          <template v-if="item.action_type === 'skip'">
            <div class="event-cancel-wrap">
              <div class="action-name floatLeft">跳转链接</div>
            </div>
            <div class="props-wrapper">
              <h-input placeholder="http://" :filterRE="/[<>]/g" v-model="item.out_url"></h-input>
            </div>
          </template>
          <template v-if="item.action_type === 'download'">
            <div class="event-cancel-wrap">
              <div class="action-name floatLeft">跳转APP页面</div>
            </div>
            <div class="props-wrapper">
              <h-input placeholder="android跳转地址" :filterRE="/[<>]/g" v-model="item.android_jump_url"></h-input>
            </div>
            <div class="props-wrapper">
              <h-input placeholder="android下载地址" :filterRE="/[<>]/g" v-model="item.android_download_url"></h-input>
            </div>
            <div class="props-wrapper">
              <h-input placeholder="ios跳转链接" :filterRE="/[<>]/g" v-model="item.ios_jump_url"></h-input>
            </div>
            <div class="props-wrapper">
              <h-input placeholder="ios跳转链接" :filterRE="/[<>]/g" v-model="item.ios_download_url"></h-input>
            </div>
          </template>
        </template>
      </div>
    </collapse-wrap>
  </div>
</template>
<script>
import ImgSelect from '@Components/ImgSelect'
import CollapseWrap from '@Components/collapseWrap'
import { generateUID } from '@h5Designer/utils'

export default {
  name: 'eCarouselConfig',
  props: [
    'context',
    'selectedElementData'
  ],
  components: {
    ImgSelect,
    CollapseWrap
  },
  computed: {
    imgList: {
      get: function () {
        return this.selectedElementData.property.imgList
      },
      set: function (val) {
        let { updateElementProperty } = this.context
        updateElementProperty({ 'imgList': val })
        return true
      }
    },
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
  data() {
    return {
      // imgList: [],
      scaleList: [
        {
          label: '2:1',
          value: 0.5
        },
        {
          label: '4:3',
          value: 0.75
        },
        {
          label: '16:9',
          value: 0.5625
        },
        {
          label: '自定义',
          value: 1
        }
      ],
      clickEventList: [
        {
          type: 'skip',
          name: '跳转链接'
        },
        {
          type: 'download',
          name: '跳转APP页面'
        },
        {
          type: 'none',
          name: '无'
        }
      ]
    }
  },
  watch: {
    // 'selectedElementData': {
    //   handler(newVal) {
    //     console.log(newVal)
    //   },
    //   deep: true
    // },
    imgList: {
      handler(newVal) {
        // console.log(newVal)
        // let { updateElementProperty } = this.context
        // updateElementProperty({'imgList': newVal})
      },
      deep: true
    }
  },
  created() {
    // this.imgList = this.propertyProxy.imgList
  },
  methods: {
    format (val) {
      return val + 's'
    },
    getImgUrl(url) {
      let index = this.propertyProxy.activeIndex - 1
      this.imgList[index].src = url
      this.updateImgList()
    },
    changeActionType(e) {
      let index = this.propertyProxy.activeIndex - 1
      if (e === 'none') {
        this.imgList[index].android_download_url = ''
        this.imgList[index].android_jump_url = ''
        this.imgList[index].ios_jump_url = ''
        this.imgList[index].ios_download_url = ''
        this.imgList[index].out_url = ''
      } else if (e === 'skip') {
        this.imgList[index].android_download_url = ''
        this.imgList[index].android_jump_url = ''
        this.imgList[index].ios_jump_url = ''
        this.imgList[index].ios_download_url = ''
      } else if (e === 'download') {
        this.imgList[index].out_url = ''
      }
      this.updateImgList()
    },
    changeHeight(value) {
      if (value === 1) {

      } else {
        let { updateElementStyle } = this.context
        updateElementStyle({
          height: this.selectedElementData.style.width * value
        })
      }
    },
    deleteImg() {
      if (this.imgList.length > 1) {
        let size = this.imgList.length
        this.imgList.splice(this.propertyProxy.activeIndex - 1, 1)
        if (this.propertyProxy.activeIndex === size) {
          this.context.updateElementProperty({
            'imgList': this.imgList,
            activeIndex: size - 1
          })
        } else {
          this.updateImgList()
        }
      } else {
        this.$hMessage.info('图片至少有一张')
        return false
      }
    },
    addImg() {
      if (this.imgList.length < 5) {
        this.imgList.push({
          uuid: generateUID(),
          src: '',
          out_url: '',
          android_download_url: '',
          android_jump_url: '',
          ios_jump_url: '',
          ios_download_url: '',
          action_type: 'none'
        })
        this.updateImgList()
      } else {
        this.$hMessage.info('图片不能超过5张')
      }
    },
    updateImgList() {
      let { updateElementProperty } = this.context
      updateElementProperty({ 'imgList': this.imgList })
    }
  }
}
</script>
<style scoped lang="scss">
.minus-button {
  cursor: pointer;
}
.plus-button {
  cursor: pointer;
}
/deep/ .h-tooltip-popper {
  width: 43px;
}
/deep/ .h-tooltip-content {
  text-align: center;
}
/deep/ .h-tooltip-inner {
  display: inline-block;
  white-space: nowrap;
}
</style>
