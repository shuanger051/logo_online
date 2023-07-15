<template>
    <div ref="myMarquee" class="join">
        <div class="join-content" ref="marqueeWrapper">
            <!-- ul和li的节点层级一定不能改变，ul下面的子节点必须是li，不然取不到offsetLeft的值 -->
            <ul class="join-content-wrap" ref="ul">
                <li class="join-content-item" v-for="(item, index) in [...messageList, ...messageList]" :key="index">
                    <div class="join-content-item-txt">
                        <span class="name">{{item.op_desc}}</span>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
export default {
  name: 'NoticeBar',
  props: {
    messageList: {
      default: () => [],
      type: [Array]
    },
    speed: {
      default: 2,
      type: Number
    },
    height: {
      default: 50,
      type: Number
    }
  },
  data() {
    return {
      wrapperElement: null,
      ulElement: null,
      timerId: null
    }
  },
  methods: {
    move(speed) {
      let that = this
      this.wrapperElement = this.$refs.marqueeWrapper
      this.ulElement = this.$refs.ul
      let liElementArr = this.$refs.ul.childNodes
      let resultNum
      for (let i = 0; i < liElementArr.length; i++) {
        resultNum += liElementArr.item(i).offsetWidth // 只能这样写才能拿到offsetwidth
      }
      this.ulElement.style.width = resultNum + 'px'
      // 向左滚动（speed为-）
      if (that.ulElement.offsetLeft < (-that.ulElement.offsetWidth / 2)) {
        that.ulElement.style.left = '0' // that.ulElement的左边框与div的左边框对齐
      }
      // 向右滚动（speed为+）
      if (that.ulElement.offsetLeft > 0) {
        that.ulElement.style.left = -(that.ulElement.offsetWidth / 2) + 'px' // that.ulElement的右边框与div的右边框对齐
      }
      that.ulElement.style.left = (that.ulElement.offsetLeft + speed) + 'px'
    }
  },
  mounted() {
    let that = this
    this.timerId = setInterval(function() {
      that.move(-that.speed)
    }, 30)
  },
  destroyed() {
    let that = this
    clearInterval(that.timerId)
  }
}
</script>

<style lang="scss" scoped>
    @mixin Hcenter {
        transform: translateX(-50%);
        left: 50%;
    } // 最新揭晓
    $avtar-size: 30px;
    $margin-top-bottom: 10px;
    $item-height: $avtar-size+2 * $margin-top-bottom;
    @keyframes movin {
        to {
            transform: translateX(-50%)
        }
    }
    .move {
        --animationMoveDuration: 120s;
        animation: movin var(--animationMoveDuration) linear infinite;
    }
    @keyframes movin-infinite {
        to {
            transform: translateX(50%)
        }
    }
    .movinInfinite {
        animation: movin-infinite linear;
    }
    .join {
        height: $item-height;
        &-content {
            white-space: nowrap;
            overflow-x: scroll;
            overflow-y: hidden;
            height: $item-height;
            // position: relative;
            &-wrap {
                // width: 100%;
                overflow: hidden;
                position: absolute;
                height: $item-height;
                z-index: 1;
            }
            &-item {
                height: $avtar-size;
                margin: $margin-top-bottom 10px $margin-top-bottom 375px;
                position: relative;
                display: inline-block;
                &-txt {
                    display: inline-block;
                    vertical-align: middle;
                    margin: 0 10px 0 10px;
                    line-height: $avtar-size;
                }
            }
        }
    }
</style>
