<!-- <template>
  <div>
    <van-uploader v-model="fileList" :after-read="afterRead" :max-count="1"  />

  </div>
</template>
<script>
  import { uploadMaterialAttachment } from 'core/api/'
  import {resolveImgUrl} from 'core/support/imgUrl'

  export default {
    props: ['value', 'label', 'options'],
    data(){
      return {
        fileList: [
        {
          url: resolveImgUrl(this.value),
        }]
      }
    },
    methods: {
      async afterRead(file) {
        const res = await uploadMaterialAttachment(file)
        if (res.data?.urlPath) {
          this.$emit('input', res.data.urlPath)
          this.fileList[0].url =  resolveImgUrl(res.data.urlPath)
        }
      }
    },
    mounted() {
    }
  }
</script>
<style scoped lang="scss">
</style> -->
<template>
  <van-button color="#7232dd" plain @click="show = true" class="btn"
    >更换图片</van-button
  >
  <van-dialog v-model="show" title="标题" show-cancel-button>
    <div>
    <van-image
      :src="resolveImgUrl(item)"
      fit="contain"
      v-for="item in imageList"
    />
    </div>
    <van-pagination v-model="page.current" :total-items="page.total" :items-per-page="5" />
  </van-dialog>
</template>
<script>
import { resolveImgUrl } from "core/support/imgUrl";

export default {
  data() {
    return {
      show: false,
      imageList: [
        'https://www.baidu.com/img/flexible/logo/pc/result@2.png'
      ],
      page: {
        current: 1
      }
    };
  },
};
</script>
