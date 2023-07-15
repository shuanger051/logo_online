<template>
  <div>
    <!-- 编辑模式 -->
    <template v-if="context.mode==='edit'">
      <template v-for="(item, index) in imgList">
        <img :class="`index${index}`" :src="item.src ? item.src : defaultImg" alt=""
          v-if="index + 1 == property.activeIndex" :key="item.uuid" width="100%" height="100%" />
      </template>
    </template>

    <!-- 查看模式 -->
    <div v-else :style="{...objProperty}">
      <swipe style="width:100%;height:100%;" :loop="true" :show-indicators="true" :ref="'mySwiper_' + uuid"
        :autoplay="property.auto_play==1 ? property.switch_time * 1000 : ''" :duration="500" indicator-color="#1989fa">
        <swipe-item v-for="(item, index) in imgList" :key="index" @click="clickAction(item)">
          <img :src="item.src || defaultImg" style="width:100%;height:100%;" />
        </swipe-item>
      </swipe>
    </div>
    <!-- <swiper
      :style="{...objProperty}"
      ref="mySwiper"
      :autoPlay="property.auto_play==1" :showIndicator="true" :interval="property.switch_time* 1000" :duration="500"
    >
      <slide v-for="item in imgList" :key="item.uuid" @click="clickAction(item)" style="overflow: hidden;">
        <img :src="item.src || defaultImg" :width="style.width" :height="style.height">
      </slide>
    </swiper> -->
  </div>
</template>

<script>
import { mapValues } from 'lodash'
import defaultImg from '@Root/assets/images/default.png'
import Swipe from 'vant/lib/swipe'
import SwipeItem from 'vant/lib/swipe-item'
import store from '@h5Render/store/h5State'
import { trackFunc } from '@Utils/appSDK'

import 'vant/lib/swipe/style'
import 'vant/lib/swipe-item/style'

const u = navigator.userAgent
const isWeixin = u.toLowerCase().indexOf('micromessenger') !== -1
const isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1
const isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)
const hidden = () => {
  let hidden =
    window.document.hidden ||
    window.document.mozHidden ||
    window.document.msHidden ||
    window.document.webkitHidden

  if (typeof hidden === 'undefined' || hidden === false) {
    return true
  }
}

export default {
  props: ['name', 'context', 'property', 'style', 'uuid'],
  data() {
    return {
      defaultImg: defaultImg,
      objProperty: ''
    }
  },
  components: {
    Swipe,
    SwipeItem
  },
  computed: {
    imgList() {
      return this.property.imgList
    },
    getState() {
      return store.state
    }
  },
  created() {
    this.objProperty = mapValues(this.property, (value, key) => {
      return value
    })
    Object.assign(this.objProperty, this.style)
  },
  // mounted() {
  //   let _this = this
  //   // 选择需要观察变动的节点
  //   const targetNode = document.getElementById(this.uuid)
  //   // 观察器的配置（需要观察什么变动）
  //   const config = { attributes: true }
  //   // 当观察到变动时执行的回调函数
  //   const callback = function (mutationsList, observer) {
  //     // Use traditional 'for loops' for IE 11
  //     for (let mutation of mutationsList) {
  //       if (mutation.type === 'attributes') {
  //         // console.log('The ' + mutation.attributeName + ' attribute was modified.')
  //         let display = mutation.target.style.display
  //         if (display === 'block') {
  //           _this.$refs['mySwiper_' + _this.uuid].resize()
  //         }
  //       }
  //     }
  //   }

  //   // 创建一个观察器实例并传入回调函数
  //   const observer = new MutationObserver(callback)

  //   // 以上述配置开始观察目标节点
  //   observer.observe(targetNode, config)
  // },
  watch: {
    property: {
      handler(newVal) {
        this.objProperty = mapValues(this.property, (value, key) => {
          return value
        })
        Object.assign(this.objProperty, this.style)
      },
      deep: true
    },
    'getState.curShowElement': {
      handler(val) {
        console.log(val)
        if (val.indexOf(this.uuid) !== -1 && this.$refs['mySwiper_' + this.uuid]) {
          this.$refs['mySwiper_' + this.uuid].resize()
          console.log(this.uuid)
        }
      },
      deep: true
    }
  },
  methods: {
    jump (params, type) {
      if (params.jumpUrl !== '' || params.downloadUrl !== '') {
        if (window.self == window.top) {
          if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
            let page_icon_section = '轮播图'
            trackFunc('Operactmainpage_icon_click_hs', {
              To_url: type === 'jump' ? params.jumpUrl : params.downloadUrl,
              page_icon_section,
              uuid: params.uuid,
              src: params.src
            })
          }
          if (type === 'jump') {
            if (params.jumpUrl !== '') {
              window.location.href = params.jumpUrl
            } else {
              return false
            }
          } else {
            window.location.href = params.downloadUrl
          }
        }
      }
    },
    jumpOrDownLoad(params) {
      this.jump(params, 'jump')
      // 等待，下载应用
      let timer = setTimeout(() => {
        if (hidden()) {
          this.jump(params, 'dowload')
        }
        clearTimeout(timer)
      }, 6000)
    },
    clickAction(item) {
      if (item.action_type === 'download') {
        if (isWeixin) {
          alert('请在浏览器上打开')
        } else {
          // android端
          if (isAndroid) {
            this.jumpOrDownLoad({
              jumpUrl: item.android_jump_url,
              downloadUrl: item.android_download_url,
              ...item
            })
          }
          if (isIOS) {
            this.jumpOrDownLoad({
              jumpUrl: item.ios_jump_url,
              downloadUrl: item.ios_download_url,
              ...item
            })
          }
        }
      } else if (item.action_type === 'skip' && item.out_url) {
        if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
          let page_icon_section = '轮播图'
          trackFunc('Operactmainpage_icon_click_hs', {
            To_url: item.out_url,
            page_icon_section,
            uuid: item.uuid,
            src: item.src
          })
        }
        window.location.href = item.out_url
      }
    }
  }
}
</script>
<style>
.my-swipe .van-swipe-item {
  color: #fff;
  font-size: 20px;
  line-height: 150px;
  text-align: center;
  background-color: #39a9ed;
}
</style>
