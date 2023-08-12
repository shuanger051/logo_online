<template>
  <div class="page-wrap">
    <!-- 店招模版选择 -->
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="queryTemplate"
    >
      <van-cell v-for="item in list" :key="item.id" :title="item.name" />
    </van-list>
  </div>
</template>
<script>
import { signboardService } from "@/apis";
export default {
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      page: {
        size: 30,
        current: 0,
      },
    };
  },
  methods: {
    // 模版查询
    queryTemplate() {
      const { list: oldArr, page } = this;
      const { size, current } = page;
      const pageNum = current + 1;
      this.loading = true;
      signboardService
        .queryTemplateListPageAPI({
          pageNum,
          pageSize: size,
        })
        .finally(() => (this.loading = false))
        .then((res) => {
          page.current = pageNum;
          const { list } = res.data;
          this.list = oldArr.concat(list);
          this.finished = list.length < size;
        });
    },
  },
};
</script>
