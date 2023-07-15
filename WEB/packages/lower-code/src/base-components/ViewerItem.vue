<template>
  <div class="viewer-item-wrap clearfix">
    <div class="viewer-item viewer-label" v-if="label">{{label}}</div>
    <div class="viewer-item viewer-content" v-if="normalText" :class="itemClass" :title="content || '--'">{{content || '--'}}</div>
    <!-- 富文本 -->
    <div class="viewer-item viewer-content" v-else-if="type === 'rtf'" :class="itemClass" v-html="content || '--'"></div>
    <!-- 图片图标 -->
    <div class="viewer-item viewer-content pic" v-else-if="type === 'pic'">
      <div class="viewer-img-content" v-for="(img, index) in content" :key="index">
        <div class="view-img-wrap">
          <img v-if="img.url" class="view-img" :class="{empty: !img.url}" :src="img.url" alt="" @error="loadErrorImg">
          <img v-else class="view-img" :src="defaultImg" alt="">
        </div>
        <div class="des">{{img.name}}</div>
      </div>
    </div>
    <!-- 任务对象 -->
    <div class="viewer-item viewer-content lo" v-else-if="type === 'lo'">
      <h-collapse v-model="collapse" v-if="content.length">
        <h-panel v-for="(item, index) in content" :key="index">
          {{item.name}}
          <p slot="content">
            <h-tag v-for="tag in item.value" :key="tag">{{tag}}</h-tag>
          </p>
        </h-panel>
      </h-collapse>
    </div>
    <!-- tags -->
    <div class="viewer-item viewer-content lo" v-else-if="type === 'tags'">
      <template v-if="content.length" >
        <h-tag v-for="(item_tag, index_tag) in content" :key="index_tag" :name="item_tag">
          {{ item_tag }}
        </h-tag>
      </template>
      <template v-if="!content.length">--</template>
    </div>
    <!-- 自定义slot -->
    <div class="viewer-item viewer-content pic" v-else-if="type === 'slot'">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import errorImg from '@Root/assets/images/upload-error.png'
import defaultImg from '@Root/assets/images/default.png'

export default {
  name: 'ViewerItem',
  data() {
    return {
      collapse: [0, 1, 2, 3]
    }
  },
  computed: {
    itemClass() {
      return {
        textarea: this.type === 'textarea' && this.content,
        rtf: this.type === 'rtf' && this.content
      }
    },
    normalText() {
      return this.type === 'text' || this.type === 'textarea'
    }
  },
  props: {
    type: {
      type: String,
      default: 'text' // text、textarea、lo-任务对象、rtf-富文本、pic-图片
    },
    label: {
      type: String,
      default: ''
    },
    content: String || Array // 图片为数组形式[{url: 'http://', name: ''}] // 任务对象为数组形式[{name: '会员', value: ['一级会员']]
  },
  methods: {
    // 请求网络图片或者接口图片为空、错误时，使用默认图片
    loadErrorImg(event) {
      if (event.type == 'error') {
        event.target.src = errorImg
      }
    }
  },
  created() {
    this.defaultImg = defaultImg
  }
}
</script>

<style lang="scss" scope>
.viewer-item-wrap {
  margin-bottom: 12px;

  .viewer-item {
    vertical-align: middle;
    font-size: 12px;
    color: #333;
    line-height: 28px;
    height: 28px;
    box-sizing: border-box;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;

    &.textarea {
      height: 56px;
      overflow-y: auto;
      white-space: normal;
      line-height: 28px;
    }

    &.rtf {
      height: auto;
      max-height: 120px;
      overflow-y: auto;
      white-space: normal;
    }

    &.pic {
      height: auto;
    }

    &.lo {
      height: auto;
      white-space: normal;
      .h-collapse-header {
        height: 28px;
        line-height: 28px;
        font-size: 12px;
        color: #666666;
        font-weight: normal;
        padding: 0 0 0 7px;
        i {
          font-size: 12px;
        }
      }
      .h-collapse-content {
        padding: 0 3px;
        background: #fafafa;
      }
      .h-collapse-content > .h-collapse-content-box {
        padding: 5px 0;
        background: #fafafa;
      }
    }
  }

  .viewer-label {
    width: 176px;
    text-align: right;
    float: left;
    background: #f7f7f7;
    padding-right: 8px;
  }

  .viewer-content {
    margin-left: 176px;
    text-align: left;
    padding-left: 8px;

    .viewer-img-content {
      position: relative;
      display: inline-block;
      margin-right: 24px;

      .view-img-wrap {
        width: 240px;
        height: 140px;
        display: flex;
        justify-content: center;
        align-items: center;
        background: #f7f7f7;
        border-radius: 2px;

        .view-img {
          display: block;
          max-width: 240px;
          max-height: 140px;
          &.empty {
            background-color: #ddd;
          }
        }
      }

      .des {
        text-align: center;
        font-size: 12px;
        line-height: 12px;
        color: #333;
        margin-top: 6px;
      }
    }
  }
}
</style>
