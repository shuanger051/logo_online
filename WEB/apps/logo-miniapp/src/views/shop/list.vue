<template>
  <div class="page-wrap">
    <van-collapse v-if="list.length" v-model="activeId" :accordion="true">
      <van-collapse-item
        v-for="item in list"
        :key="item.id"
        :title="item.shopName"
        :name="item.id"
      >
        <shop-detail :detail="item" />
      </van-collapse-item>
    </van-collapse>
    <van-empty v-else description="暂无关联商铺">
      <van-button round block plain icon="plus" size="small" to="/shop/detail"
        >新增商铺</van-button
      >
    </van-empty>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { shopService } from "@/apis";
import ShopDetail from "./components/ShopDetail";
export default {
  components: { ShopDetail },
  data() {
    return {
      list: [],
      activeId: [],
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
    // 查询字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["bizYears", "industryType", "shopsType"],
    });
  },
  methods: {
    // 查询商铺信息
    queryShopList() {
      const { customerName } = this.userInfo;
      shopService
        .getCustomerInfoByUserNameAPI({ customerName })
        // 保存商户信息
        .then((res) => {
          // 商户信息
          const { shopsList, merchant } = res.data;
          this.$store.commit("user/setMerchantInfo", merchant);
          // 商铺信息
          this.list = shopsList;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 0;
  background-color: @gray-2;
  min-height: 100%;
  box-sizing: border-box;
  :deep(.van-empty) {
    &__bottom {
      .van-button {
        padding-left: 18px;
        padding-right: 18px;
      }
    }
  }
  :deep(.van-collapse-item) {
    &__title {
      &.van-cell--clickable:active {
        background-color: #fff !important;
      }
    }
    &__content {
      padding: 0;
      &::after {
        content: "";
        display: block;
        height: 12px;
        background-color: @gray-2;
      }
      .van-cell {
        &__title {
          color: @gray-6;
        }
        &__value {
          color: @gray-8;
        }
        .van-button {
          &:not(:last-child) {
            margin-right: 8px;
          }
        }
      }
    }
  }
}
</style>
