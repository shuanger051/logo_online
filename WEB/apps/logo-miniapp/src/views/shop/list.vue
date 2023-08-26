<template>
  <div class="page-wrap">
    <template v-if="list.length">
      <van-panel v-for="item in list" :key="item.id">
        <van-cell
          title="行业类型"
          :value="item.industryType | dict(DictIndustryType)"
        ></van-cell>
        <van-cell
          title="营业年限"
          :value="item.bizYears | dict(DictBizYears)"
        ></van-cell>
        <van-cell
          title="店铺属性"
          :value="item.shopsType | dict(DictShopsType)"
        ></van-cell>
        <van-cell title="商铺地址" :value="item.address"></van-cell>
        <van-cell title="备注" :value="item.remark"></van-cell>
        <template slot="footer">
          <van-button plain size="small" :to="`/shop/detail?shopId=${item.id}`"
            >信息变更</van-button
          >
          <van-button
            plain
            size="small"
            :to="`/signboard/editSelect?shopId=${item.id}`"
            >店招设计</van-button
          >
        </template>
      </van-panel>
    </template>
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
import { mapDictObject } from "@/store/helpers";
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
      // 行业类别
      DictIndustryType: mapDictObject("industryType"),
      // 营业念想
      DictBizYears: mapDictObject("bizYears"),
      // 商铺属性
      DictShopsType: mapDictObject("shopsType"),
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
  :deep(.van-empty) {
    &__bottom {
      .van-button {
        padding-left: 18px;
        padding-right: 18px;
      }
    }
  }
  :deep(.van-panel) {
    &:not(:last-child) {
      margin-bottom: 12px;
    }
    &__footer {
      text-align: right;
      .van-button {
        &:not(:last-child) {
          margin-right: 8px;
        }
      }
    }
  }
}
</style>
