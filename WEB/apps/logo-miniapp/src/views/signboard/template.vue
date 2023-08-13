<template>
  <div class="page-wrap">
    <!-- 店招模版选择 -->
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="queryTemplate"
    >
      <div v-for="item in list" :key="item.id" @click="go(item.id)">
        <van-image
          v-if="item.url"
          width="100%"
          height="100px"
          fit="contain"
          :src="item.url"
        />
        <van-empty v-else description="" />
      </div>
    </van-list>
  </div>
</template>
<script>
import { signboardService } from "@/apis";
import {resolveImgUrl} from 'core/support/imgUrl'

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
    resolveList(lists) {
      return lists.map((item) => {
        const ret = {
          id: item.id,
        };
        try {
          const { domItem } = item;
          const data = JSON.parse(domItem);
          ret.url =resolveImgUrl(data.cover_image_url);
        } catch (e) {
          console.log(e)
          ret.url = null;
        }
        return ret;
      });
    },
    go(id) {
      this.$router.push(`/editSignboard/${id}?shopId=${this.$route.query.shopId}`)
    },
    // 模版查询
    queryTemplate() {
      const { styles } = this.$route.query;
      const { list: oldArr, page } = this;
      const { size, current } = page;
      const pageNum = current + 1;
      this.loading = true;
      signboardService
        .queryTemplateListPageAPI({
          pageNum,
          pageSize: size,
          style: styles,
        })
        .then((res) => {
          page.current = pageNum;
          const { list } = res.data;
          this.list = oldArr.concat(this.resolveList(list));
          this.finished = list.length < size;
        })
        .finally(() => (this.loading = false));
    },
  },
};
</script>
