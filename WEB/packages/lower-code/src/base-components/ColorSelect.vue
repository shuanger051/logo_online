<template>
  <div class="color-select" v-clickoutside="clickOutSide">
    <!-- <h-poptip :placement="placement" :width="width" class="color-picker">
      <div class="emptyColor" v-if="preColorList.length === 0 && selectedColor === ''">
        <img src="@Root/assets/images/empty-color.png" :style="`height:${emptyHeight}px; width: ${emptyWidth}px`" alt>
      </div>
      <h-button
        type="ghost"
        :style="{ background: selectedColor, padding: padding }"
        :icon="icon"
        v-else
        class="select-btn"
      ></h-button>
      <div class="api" slot="content">
        <sketch-picker
          v-model="selectedColor"
          @input="updateColor"
          :preset-colors="colorList"
        />
        <div class="more-btn">
          更多颜色...
          <input
            class="more-input"
            type="color"
            ref="html5Color"
            v-model="selectedColor"
          >
        </div>
      </div>
    </h-poptip> -->
    <div class="select-btn-box">
      <div class="emptyColor" v-if="preColorList.length === 0 && selectedColor === ''" @click="openColor=!openColor">
        <img src="@Root/assets/images/empty-color.png" :style="`height:${emptyHeight}px; width: ${emptyWidth}px`" alt>
      </div>
      <h-button type="ghost" :style="{ background: selectedColor, padding: padding }" :icon="icon" v-else
        class="select-btn" @click="openColor=!openColor"></h-button>
      <div v-if="openColor" :class="[
          'select-color-box',
          placement === placement ? placement : ''
        ]" :style="{'width': `${width}px`}">
        <sketch-picker v-model="selectedColor" @input="updateColor" :preset-colors="colorList" />
        <div v-if="isReset" class="reset-btn" @click="onClickResetColor">重置</div>
        <div class="more-btn">
          更多颜色...
          <!-- 用以激活HTML5颜色面板 -->
          <input class="more-input" type="color" ref="html5Color" v-model="inputColor">
        </div>
      </div>
    </div>
    <div class="pre-color" v-if="preColorList.length !== 0">
      <div :class="`pre-item item${index + 1}`" v-for="(item, index) in preColorList" :key="index">
        <div class="item" :style="`background: ${item}; height: ${preHeight}; width: ${preWidth}`"
          @click="updateColor(item)"></div>
      </div>
    </div>
    <div class="transparency" v-if="needTransparency">
      <h-typefield :value="transparency" type="money" :suffixNum="0" placeholder="透明度" @on-blur="onChangeTransparency"
        :nonNegative="true" :max="100" class="common-input">
        <span slot="append">%</span>
      </h-typefield>
    </div>
  </div>
</template>

<script>
import { Sketch } from 'vue-color'
import { isString } from 'lodash'
export default {
  name: 'ColorSelect',
  props: {
    width: {
      type: Number,
      default: () => 266
    }, // 弹框的宽度
    padding: {
      type: String,
      default: () => ''
    }, // 外部显示的按钮的大小
    emptyHeight: {
      type: String,
      default: () => '23'
    }, // 默认颜色为空时的马赛克的高度
    emptyWidth: {
      type: String,
      default: () => '23'
    }, // 默认颜色为空时的马赛克的宽度
    placement: {
      type: String,
      default: () => 'top'
    }, // 默认显示的位置，同popTip,目前支持[top, bottom, left, right, left-start, left-end, right-start, right-end]
    icon: {
      type: String,
      default: () => ''
    }, // 内部的图标
    selectedColor: {
      type: String,
      default: () => '#fff'
    }, // 显示的颜色
    preColorList: {
      type: Array,
      default: () => []
    }, // 预设的颜色列表
    preHeight: {
      type: String,
      default: () => '26px'
    }, // 预设颜色的高度
    preWidth: {
      type: String,
      default: () => '26px'
    }, // 预设颜色的宽度
    needTransparency: {
      type: Boolean,
      default: () => false
    }, // 是否需要外边输入透明度
    isReset: {
      type: Boolean,
      default: () => false
    } // 是否需要重置
  },
  data() {
    return {
      transparency: 100,
      html5Color: '',
      openColor: false,
      inputColor: '',
      colorList: ['transparent', '#418BF0', '#F0B442', '#48D93F', '#FF0000', '#A3C1FF', '#FCEFD3', '#B4DB6F', '#EFFAD3', '#a8b7d0']
    }
  },
  components: {
    'sketch-picker': Sketch
  },
  watch: {
    selectedColor: {
      handler(val) {
        if (isString(val) && val.indexOf('rgba') === 0) {
          let colorArr = val.slice(5, -1).split(',')
          this.transparency = colorArr[3] * 100
        } else {
          this.transparency = 100
        }
      },
      deep: true
    },
    inputColor: {
      handler(val) {
        this.updateColor(val)
      },
      deep: true
    }
  },
  created() {
    if (this.selectedColor.indexOf('rgba') === 0) {
      let colorArr = this.selectedColor.slice(5, -1).split(',')
      this.transparency = colorArr[3] * 100
    } else {
      this.transparency = 100
    }
  },
  methods: {
    // 颜色重置 @resetColor
    onClickResetColor() {
      this.$emit('resetColor')
    },
    // 输入修改透明度
    onChangeTransparency(e) {
      let color
      if (typeof this.selectedColor === 'object') {
        color = this.selectedColor[0]
      } else {
        color = this.selectedColor
      }
      if (color.indexOf('rgb') === -1) {
        // 为HEX格式的颜色
        color = this.hexToRGBA(color, e.target._value / 100)
      } else if (color.indexOf('rgba') === -1) {
        // 为rgb格式的颜色
        let colorArr = color.slice(4, -1).split(',')
        color = `rgba(${colorArr[0]},${colorArr[1]},${colorArr[2]},${e.target._value / 100})`
      } else {
        // 为rgba格式的颜色
        let colorArr = color.slice(5, -1).split(',')
        color = `rgba(${colorArr[0]},${colorArr[1]},${colorArr[2]},${e.target._value / 100})`
      }
      this.updateColor(color)
    },
    // 颜色更新 @updateColor
    updateColor(value) {
      if (typeof value === 'string') {
        this.$emit('updateColor', value)
      } else {
        value = `rgba(${value.rgba.r},${value.rgba.g},${value.rgba.b},${value.rgba.a})`
        this.$emit('updateColor', value)
      }
    },
    hexToRGBA(_color, _opacity) {
      let sColor = _color.toLowerCase()
      // 十六进制颜色值的正则表达式
      const reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/
      // 如果是16进制颜色
      if (sColor && reg.test(sColor)) {
        if (sColor.length === 4) {
          let sColorNew = '#'
          for (let i = 1; i < 4; i += 1) {
            sColorNew += sColor.slice(i, i + 1).concat(sColor.slice(i, i + 1))
          }
          sColor = sColorNew
        }
        // 处理六位的颜色值
        const sColorChange = []
        for (let i = 1; i < 7; i += 2) {
          sColorChange.push(parseInt('0x' + sColor.slice(i, i + 2)))
        }
        return 'rgba(' + sColorChange.join(',') + ',' + _opacity + ')'
      }
      return sColor
    },
    clickOutSide() {
      this.openColor = false
    }
  }
}
</script>

<style scoped lang="scss">
.color-select {
  display: flex;
  align-items: center;

  .color-picker {
    display: flex;
    align-items: center;

    .emptyColor {
      display: flex;
      align-items: center;
      cursor: pointer;
    }

    .select-btn {
      color: #999;
    }

    /deep/ .h-poptip-body {
      padding: 0;
    }

    /deep/ .h-poptip-popper {
      padding: 0;
    }
  }

  .select-btn-box {
    position: relative;
  }

  .select-color-box {
    position: absolute;
    background-color: #fff;
    box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
    z-index: 101;
  }

  .top {
    top: 0;
    left: 50%;
    transform: translate(-50%, -100%);
  }

  .bottom {
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
  }

  .left {
    top: 50%;
    left: 0;
    transform: translate(-100%, -50%);
  }

  .right {
    top: 50%;
    left: 100%;
    transform: translateY(-50%);
  }

  .left-start {
    top: 0;
    left: 0;
    transform: translateX(-100%);
  }

  .left-end {
    bottom: 0;
    left: 0;
    transform: translateX(-100%);
  }

  .right-start {
    top: 0;
    right: 0;
    transform: translateX(100%);
  }

  .right-end {
    bottom: 0;
    right: 0;
    transform: translateX(100%);
  }

  .reset-btn {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 3px 0;
    margin: 0 16px;
    font-size: 12px;
    color: #495060;
    border-radius: 2px;
    background-color: #fff;
    border: 1px solid #d7dde4;
    cursor: pointer;
  }

  .more-btn {
    position: relative;
    height: 27px;
    margin-bottom: 10px;
    margin-left: 16px;
    line-height: 27px;
    text-align: left;
    cursor: pointer;

    .more-input {
      position: absolute;
      top: 0;
      left: 0;
      opacity: 0;
    }
  }

  .transparency {
    margin-left: 5px;
    height: 23px;
  }

  .pre-color {
    display: flex;
    align-items: center;
    margin-left: 1px;

    .pre-item {
      border-radius: 2px;
      padding: 1px;
      border: 1px solid #ddd;
      margin-left: 3px;

      .item {
        width: 26px;
        height: 26px;
        cursor: pointer;
      }
    }
  }
}

// 输入框默认样式修改
.common-input {
  height: 100%;
  line-height: 0;

  /deep/ input {
    height: 100% !important;
    line-height: 0;
  }
  /deep/ .h-typefield-group-append {
    padding: 2px;
    border-radius: 5px;
    background-color: transparent;
  }
  /deep/ .h-typefield-no-right-radius {
    border-right: 0;
  }
}

// 颜色选择器默认样式修改
/deep/ .vc-sketch {
  width: calc(100% - 32px);
  padding: 16px 16px 0;
  box-shadow: none;
}

/deep/ .vc-sketch-saturation-wrap {
  padding-bottom: 55%;
  border-radius: 4px;
}

/deep/ .vc-sketch-presets {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  width: 100%;
  margin: 0;
  padding: 8px 0 0;
  border: none;
}

/deep/ .vc-sketch-sliders .vc-hue,
/deep/ .vc-sketch-sliders .vc-alpha-gradient,
/deep/ .vc-sketch-alpha-wrap {
  border-radius: 5px;
}

/deep/ .vc-sketch-active-color {
  border-radius: 4px;
}

/deep/ .vc-sketch-presets-color {
  width: 18px;
  height: 18px;
  margin: 0 8px 12px 0;
}

/deep/ .vc-sketch-presets-color:nth-child(9n) {
  margin-right: 0;
}

/deep/ .vc-sketch-field .vc-input__input {
  width: 100%;
  padding: 3px 0;
  text-align: center;
  border-radius: 4px;
  border: 1px solid #eee;
  box-shadow: none;
}

/deep/ .vc-sketch-field .vc-input__label {
  color: rgb(153, 153, 153);
}
</style>
