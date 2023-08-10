<template>
  <div class="page-wrap">
    <template v-if="list.length">
      <van-cell v-for="item in list" :key="item.id">
        {{ item.name }}
      </van-cell>
    </template>
    <van-empty v-else description="暂无关联商铺">
      <van-button
        round
        block
        plain
        type="primary"
        icon="plus"
        size="small"
        to="/shop/detail"
        >新增商铺</van-button
      >
    </van-empty>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { shopService } from "@/apis";
export default {
  data() {
    return {
      list: [],
    };
  },
  computed: {
    ...mapState({
      // 用户信息
      userInfo: (state) => state.user.profiles,
    }),
  },
  created() {
    this.queryShopList();
  },
  methods: {
    // 查询商铺信息
    queryShopList() {
      const { customerName } = this.userInfo;
      shopService.getCustomerInfoByUserNameAPI({ customerName }).then((res) => {
        console.log(res);
        this.list = res.data.shopsList;
      });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap{
  :deep(.van-empty){
    &__bottom{
      .van-button{
        padding-left: 18px;
        padding-right: 18px;
      }
    }
  }
}
</style>
