<template>
  <div class="upload-live">
    <van-uploader :after-read="afterRead">
        <icon-fa icon="entypo:upload" color="#00bcf9" width="200" height="200"/>
        <span>照片上传</span>
     </van-uploader>
  </div>

</template>
<script>
import store from 'core/store/mobileIndex'
import { mapActions } from "vuex";

export default {
  store,
  methods: {
    ...mapActions("editor", ["setLivePic"]),

    afterRead(file) {
      this.$store.editor
      console.log(file.file, 999)
      this.setLivePic(URL.createObjectURL(file.file))
      this.$router.push({
        name: 'editLive',
        query: {
          shopId: this.$route.query.shopId
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.upload-live {
  display: flex;
  height: 80vh;
  justify-content: center;
  align-items: center;
  :deep(.van-uploader__wrapper) {
    display: flex;
    justify-content: center;
  }
  :deep(.van-uploader__input-wrapper) {
    display: flex;
    flex-direction: column;
    align-items: center;
    > span {
      font-size: 20px;
    }
  }
}
</style>