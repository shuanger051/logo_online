<template>
  <div class="page-wrap">
    <h3>{{ detail.title }}</h3>
    <div class=""></div>
    <!-- 文章内容 -->
    <div class="content"></div>
    <!-- 附件列表 -->
    <div class=""></div>
  </div>
</template>
<script>
import { articleService } from "@/apis";
export default {
  data() {
    return {
      detail: {},
    };
  },
  created() {
    this.getDetail()
  },
  methods: {
    // 获取文章详情
    getDetail() {
      const { pid } = this.$route.query;
      return articleService
        .getContentByIDAPI({
          id: pid,
        })
        .then((res) => {
          console.log(res);
        });
    },
  },
};
</script>
