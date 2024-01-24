<template>
  <div class="page-wrap">
    <a-form @submit="onSubmit">
      <a-checkbox v-model="form.checked" name="checked" fit="contain" required>
        我已仔细阅读
        <span class="agreement" @click="onRead('tiaoli')"
          >《杭州市户外广告设施和招牌指示牌管理条例》</span
        >和
        <span class="agreement" @click="onRead('guifang')"
          >《户外招牌设置管理规范》</span
        >，并按“条例”和“规范”要求开展店招店牌菜单式服务设计。本人承诺所提交信息真实、无误，如有信息不实，本人愿承担所有责任
      </a-checkbox>
      <div>
        <a-button type="primary">确认</a-button>
      </div>
      <agreement-popup ref="agree" />
    </a-form>
  </div>
</template>
<script>
import store from "@/store";
import {
  appGetLogoInfoByShopsId,
  appUpdateShopsFilingsStatusAPI,
  appGetShopsInfoByIdAPI,
} from "core/api";
import { mapState } from "vuex";
import { mapDictObject } from "@/store/helpers";
import { resolveImgUrl } from "core/support/imgUrl";
import agreementPopup from "./agreementPopup.vue";

export default {
  components: { agreementPopup },
  data() {
    return {
      show: false,
      shopData: {},
      form: {},
      imageList: [],
    };
  },
  store,
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
    // 查询字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["bizYears", "industryType", "shopsType"],
    });
    this.queryShopList();
  },
  methods: {
    // 查询商铺信息
    queryShopList() {
      appGetShopsInfoByIdAPI({
        shopsId: this.$route.query.shopId,
      })
        .then(({ data }) => {
          // 商户信息
          const items = [];
          data.list.forEach((el) => {
            if (el.attachmentType == "1" || el.attachmentType == "4") {
              items.push({
                url: resolveImgUrl(el.compressUrlPath || el.urlPath, true),
                id: el.attachmentType,
              });
            }
          });
          this.imageList.push(...items);
          this.shopData = data;
          return appGetLogoInfoByShopsId({
            shopsId: this.$route.query.shopId,
          });
        })
        .then(({ data }) => {
          this.imageList.push({
            url: resolveImgUrl(data.compressUrlPath || data.urlPath, true),
            id: "2",
          });
          this.imageList.sort((a, b) => (a.id > b.id ? 1 : -1));
        });
    },
    // showImage(item) {
    // ImagePreview([item.url]);
    // },
    async onSubmit() {
      await appUpdateShopsFilingsStatusAPI({
        id: this.$route.query.shopId,
        isFilings: 1,
      });
      this.$message.success("备案成功");
      setTimeout(() => {
        this.$router.push({
          name: "Home",
        });
      }, 1000);
    },
    onRead(type) {
      this.$refs.agree.onShow({ type });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 0 60px;
  max-width: 1000px;
  margin: 0 auto;
  min-height: 100%;
  box-sizing: border-box;
  .agreement {
    color: rgb(80, 112, 251);
  }
}
</style>
