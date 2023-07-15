<template>
    <div :class="[showInfo ?'preview-page-wrap':'preview-page-wrap1']" :style="isOverflow ? 'overflow-y: overlay;': ''" ref="previewWrap">
      <!-- <e-phone-single-page v-if="loading" :page=pages[0] :elements=elements[pages[0].uuid]||[]
        :events=events[pages[0].uuid]||[] :works=works :runtimeContext=runtimeContext :style="stylePage" /> -->
      <iframe :src="linkUrl" frameborder="0" class="h5-iframe" name="h5-iframe" id="h5-iframe" scrolling="no" width="375"
        height="812"></iframe>
    </div>
</template>

<script>
// import ePhoneSinglePage from '@h5Designer/components/e-phone-single-page'
// import { getPreviewRuntimeContext } from '@h5Designer/core/runtime_context'

// import { cloneDeep } from 'lodash'

export default {
  name: 'preview',
  props: ['worksContent', 'linkUrl', 'showInfo'],
  // components: { ePhoneSinglePage },
  data() {
    return {
      loading: false,
      isOverflow: false,
      stylePage: {},
      frameSrc: ''
    }
  },
  watch: {
    // worksContent: {
    //   handler(val) {
    //     this.$store.dispatch('cms/initWorksState')
    //   },
    //   deep: true
    // }
  },
  created() {
    // this.stylePage = {
    //   'background-color': this.pages[0].style['background_color']
    // }
    // Object.assign(this.stylePage, { height: this.pages[0].style.height + 'px' })
    // let background_image = this.pages[0].style['background_image']
    // if (background_image && background_image !== 'none') {
    //   Object.assign(this.stylePage, {
    //     'background': `url(${this.pages[0].style['background_image']}) ${this.pages[0]['style']['background_color']} no-repeat center center / cover`,
    //     'object-fit': 'contain'
    //   })
    // } else {
    //   this.stylePage['background-color'] = this.pages[0].style['background_color']
    // }
    // this.runtimeContext = getPreviewRuntimeContext()
    // this.runtimeContext
    //   .initComponents()
    //   .then(() => {
    //     this.loading = true
    //   })
  },
  // beforeDestroy() {
  //   let { cms } = this.$store.state
  //   let editContent = cloneDeep(cms)
  //   this.$store.dispatch('cms/setWorksState', editContent)
  // },
  methods: {
    reflesh() {
      window.parent.document.getElementById('h5-iframe').contentWindow.location.reload(true)
    }
  },
  destroyed() {
    // this.runtimeContext.destroy()
  },
  mounted() {
    let iframe = document.getElementById('h5-iframe')
    iframe.onload = () => {
      if (iframe.contentWindow.document.body.clientHeight > 812) {
        this.isOverflow = true
        iframe.setAttribute('height', iframe.contentWindow.document.body.clientHeight + 'px')
      }
    }
  }
}
</script>
<style scoped lang="scss">
.preview-wrap {
  background: url(../../../../assets/images/mobile-wrapper.png) no-repeat center
    center;
  position: relative;
}
.preview-page-wrap {
  width: 375px;
  height: 812px;
  overflow: hidden;
  transform: scale(0.7);
  overflow: hidden;
  position: relative;
  top: -30px;
  left: -4px;
}
.preview-page-wrap1 {
  width: 375px;
  height: 812px;
  overflow: hidden;
  transform: scale(0.7);
  overflow: hidden;
  position: relative;
  top: -30px;
  left: 0px;
}

/deep/ .h-modal-content {
  width: 800px !important;
  left: 24% !important;
}
.h5-iframe {
  html {
    overflow-x: hidden;
  }
}
</style>
