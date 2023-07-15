<template>
  <div class="action-panel-set right-panel-container">
    <h-form label-position="left">
      <h-input
        size="small"
        :min="0"
        v-model="pagesNameProxy.name"
        :filterRE="/[<>]/g"
        placeholder="页面名称"
        :maxlength="30"
      />
      <collapse-wrap name="页面尺寸">
      <h-row :style="{'margin-top': '8px'}" class="common-style-panel">
        <h-col :span="18">
          <h-input-number
            size="small"
            :min="0"
            v-model="pagesProxy.width"
            readonly
          >
            <div slot="append" class="slot-append-text">W</div>
          </h-input-number>
        </h-col>
        <h-col :span="6">
          <h-input-number
            style="margin-left: 3px;width:90px"
            size="small"
            :min="812"
            :max="10000"
            v-model="pagesProxy.height"
          >
            <span slot="append" class="slot-append-text">H</span>
          </h-input-number>
        </h-col>
      </h-row>
      </collapse-wrap>
      <collapse-wrap name="背景设置">
        <h-row>
          <img-select
            :size="5120"
            description="支持png、jpg、jpeg、gif格式，图标大小不超过5MB"
            :accept="['png', 'jpg', 'jpeg', 'gif']"
            v-model="pagesProxy['background_image']"
            @input="getImgUrl"
            type="background"
          ></img-select>
        </h-row>
        <h-row>
          <div :style="{'text-align': 'center', 'margin-bottom': '20px'}">背景图片</div>
        </h-row>
        <h-row >
          <h-col class-name="gride-edit-col-label" :span="8">背景色与透明度</h-col>
          <h-col :span="15">
            <ColorSelect
              padding="3px 5px"
              icon="plus-round"
              :selectedColor="pagesProxy['background_color']"
              :preColorList="['#418BF0', '#F0B442', '#48D93F', '#FF0000']"
              preHeight="20px"
              preWidth="20px"
              :needTransparency="true"
              @updateColor="setEleprops('background_color', $event, true)"
              @resetColor="setEleprops('background_color', '#ffffff')"
            />
          </h-col>
          <!-- <el-color-picker size="mini" color-format="hex" show-alpha v-model="pagesProxy['background_color']" @active-change="activeChange" @change="colorChange"></el-color-picker> -->
          <!-- <chrome-picker v-model="pagesProxy['background_color']" style="width: 334px" />
          <div class="color-picker-item" @click="setEleprops('background_color', '#418BF0', 1)"><div class="item item1"><div class="select-point"></div></div></div>
          <div class="color-picker-item" @click="setEleprops('background_color', '#F0B442', 2)"><div class="item item2"><div class="select-point"></div></div></div>
          <div class="color-picker-item" @click="setEleprops('background_color', '#48D93F', 3)"><div class="item item3"><div class="select-point"></div></div></div>
          <div class="color-picker-item" @click="setEleprops('background_color', '#FF0000', 4)"><div class="item item4"><div class="select-point"></div></div></div>
          <div class="color-picker-item" @click="setEleprops('background_color', '#0030FF', 5)"><div class="item item5"><div class="select-point"></div></div></div>
          <h-button class="clearColor" @click.prevent="setEleprops('background_color', '#fff')">清空</h-button> -->
        </h-row>
        <!-- <h-row>
          <div :style="{'text-align': 'center'}">背景色与透明度</div>
        </h-row> -->
      </collapse-wrap>
    </h-form>
  </div>
</template>
<script>

import { mapState } from 'vuex'

import CollapseWrap from '@Components/collapseWrap'
import ImgSelect from '@Components/ImgSelect'
import DatePickerInt from '@Components/DatePickerInt'
import ColorSelect from '@Components/ColorSelect'

import { findIndex } from 'lodash'
// import {
//   Chrome
// } from 'vue-color'

export default {
  components: {
    DatePickerInt,
    ImgSelect,
    CollapseWrap,
    ColorSelect
    // 'chrome-picker': Chrome
  },
  data() {
    return {
      activeColor: ''
    }
  },
  computed: {
    pagesProxy() {
      let index = findIndex(this.pages.items, { uuid: this.selectedPage })
      index = index === -1 ? 0 : index
      return new Proxy(this.pages.items[index]['style'], {
        get: (target, name) => {
          if (name === 'name') {
            return this.pages.items[index].name
          } else {
            return this.pages.items[index]['style'][name] || ''
          }
        },
        set: (target, name, value) => {
          if (name === 'background_color' && value) {
            value = `rgba(${value.rgba.r}, ${value.rgba.g}, ${value.rgba.b}, ${value.rgba.a})`
            // value = `rgba(${value.rgba.r}, ${value.rgba.g}, ${value.rgba.b}, ${value.rgba.a})`
          }
          if (name === 'background_color' && !value) {
            value = ''
          }
          this.updatePages(name, value)
          return true
        }
      })
    },
    pagesNameProxy() {
      let index = findIndex(this.pages.items, { uuid: this.selectedPage })
      index = index === -1 ? 0 : index
      return new Proxy(this.pages.items[index], {
        get: (target, name) => {
          return this.pages.items[index].name
        },
        set: (target, name, value) => {
          this.updatePages(name, value)
          return true
        }
      })
    },
    ...mapState('cms/works', {
      works: state => state
    }),
    ...mapState('cms/pages', {
      pages: state => state
    }),
    ...mapState('cms/editState', {
      selectedPage: state => state.selectedPage
    })
  },
  methods: {
    activeChange(item) {
      if (item.includes('NaN')) {
        return
      }
      this.activeColor = item
    },
    async updatePages(key, value) {
      await this.$store.dispatch('cms/pages/updatePages', Object.assign({ selectedPage: this.selectedPage }, {
        [key]: value
      }))
    },
    setEleprops(key, value, index) {
      // const divs = document.getElementsByClassName('color-picker-item')
      // for (let i = 0; i < divs.length; i++) {
      //   const selectPoint = document.getElementsByClassName('select-point')[i]
      //   if (i == index - 1) {
      //     divs[i].classList.add('active' + (i + 1))
      //     selectPoint.style.display = 'block'
      //   } else {
      //     divs[i].classList.remove('active' + (i + 1))
      //     selectPoint.style.display = 'none'
      //   }
      // }
      this.updatePages(key, value)
    },
    async getImgUrl(url) {
      // const that = this
      // const image = new Image()
      // image.src = url + '?v=' + Math.random() // 处理缓存
      // image.crossOrigin = '*' // 支持跨域图片
      // image.onload = function () {
      //   const base64 = that.drawBase64Image(image)
      //   that.updatePages('background_image', base64)
      // }
      let height = await this.getImageHeight(url)
      // 1、小于原画布高度时，不改变
      if (height > this.pagesProxy.height) {
        // 2、大于原画布高度时，改变
        if (height > 10000) {
          // 2.1、大于画布最大高度时，按照最高高度来
          height = 10000
        }
        this.updatePages('height', height)
      }
      this.updatePages('background_image', url)
    },
    getImageHeight(url) {
      return new Promise((resolve, reject) => {
        let img = new Image()
        img.onload = function() {
          let height = parseInt(img.height * (375 / img.width))
          resolve(height)
        }
        img.onerror = function() {
          reject('812')
        }
        img.src = url
      })
    },
    drawBase64Image (img) {
      const canvas = document.createElement('canvas')
      canvas.width = img.width
      canvas.height = img.height
      const ctx = canvas.getContext('2d')
      ctx.drawImage(img, 0, 0, img.width, img.height)
      const dataURL = canvas.toDataURL('image/png')
      return dataURL
    }
  }
}
</script>
<style scoped lang="scss">
.common-style-panel /deep/ .h-input-number-input {
  padding: 0;
}
/deep/ .h-input-number-append{
  width: 25px;
  padding: 5px 8px;
}
.color-picker-item {
  margin-top: 20px;
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
.item1 {
  background: #418BF0;
}
.item2 {
  background: #F0B442;
}
.item3 {
  background: #48D93F;
}
.item4 {
  background: #FF0000;
}
.item5 {
  background: #0030FF;
}
.active1 {
  border-color: #418BF0;
}
.active2 {
  border-color: #F0B442;
}
.active3 {
  border-color: #48D93F;
}
.active4 {
  border-color: #FF0000;
}
.active5 {
  border-color: #0030FF;
}
.clearColor {
  margin-bottom: 23px;
}
.select-point {
  width: 4px;
  height: 4px;
  border-radius: 2px;
  background: #fff;
  position: relative;
  top: 20px;
  left: 20px;
  display: none;
}
/deep/ .vc-chrome .vc-chrome-toggle-btn {
  display: none;
}
</style>
