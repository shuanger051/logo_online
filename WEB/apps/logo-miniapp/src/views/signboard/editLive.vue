<template>
  <div>
    <van-image
      width="100vw"
      :src="currentLivePic"
    />
  </div>
</template>
<script>
import store from "core/store/mobileIndex";
import { mapState } from "vuex";

export default {
  store,
  computed: {
    ...mapState("editor", {
      currentLivePic: (state) => state.mobile.currentLivePic,
    }),
    methods: {},
    
  },
};
</script>
