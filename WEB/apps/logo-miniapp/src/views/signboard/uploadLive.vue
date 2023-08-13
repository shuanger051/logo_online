<template>
  <div>
    <van-uploader :after-read="afterRead">
        <Icon icon="entypo:upload" color="#00bcf9" />
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
      setLivePic(URL.createObjectURL(file.file))
      this.$router.push('editLive')
    }
  }
}
</script>