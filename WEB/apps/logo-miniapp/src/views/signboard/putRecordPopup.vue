<template>
  <van-popup
    class="popup-login"
    v-model="show"
    :closeable="true"
    position="bottom"
    :close-on-click-overlay="false"
  >
    <van-nav-bar title="登录" />
    <van-panel>
      <van-cell
        title="行业类型"
        :value="shopData.industryType | dict(DictIndustryType)"
      ></van-cell>
      <van-cell
        title="营业年限"
        :value="shopData.bizYears | dict(DictBizYears)"
      ></van-cell>
      <van-cell
        title="店铺属性"
        :value="shopData.shopsType | dict(DictShopsType)"
      ></van-cell>
      <van-cell title="商铺地址" :value="shopData.address"></van-cell>
      <van-cell title="备注" :value="shopData.remark"></van-cell>
      <van-cell title="图片">
        <template #label>
          <van-image
            v-for="item in imageList"
            :key="`imag-${item.id}`"
            @click="showImage(item)"
            width="100"
            height="100"
            fit="contain"
            style="margin-right: 5px"
            :src="item.url"
          />
        </template>
      </van-cell>
      <van-form @submit="onSubmit" style="padding: 0 10px">
        <van-checkbox v-model="form.checked" fit="contain" required>
          我已仔细阅读《杭州市户外广告设施和招牌指示牌管理条例》和《户外招牌设置管理规范》，并按“条例”和“规范”要求开展店招店牌菜单式服务设计。本人承诺所提交信息真实、无误，如有信息不实，本人愿承担所有责任
        </van-checkbox>
        <div style="margin: 16px">
          <van-button round block type="info" native-type="submit"
            >确认</van-button
          >
        </div>
      </van-form>
    </van-panel>
  </van-popup>
</template>
<script>
import store from "@/store";
import {
  appGetLogoInfoByShopsId,
  appUpdateShopsFilingsStatusAPI,
  appGetShopsInfoByIdAPI,
} from "core/api";
import { ImagePreview } from "vant";
import { mapDictObject } from "@/store/helpers";
import { mapState } from "vuex";
import { resolveImgUrl } from "core/support/imgUrl";
import { Notify } from "vant";

export default {
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
                url: resolveImgUrl(el.compressUrlPath || el.urlPath),
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
            url: resolveImgUrl(data.compressUrlPath || data.urlPath),
            id: "2",
          });
          this.imageList.sort((a, b) => (a.id > b.id ? 1 : -1));
        });
    },
    showImage(item) {
      ImagePreview([item.url]);
    },
    async onSubmit() {
      await appUpdateShopsFilingsStatusAPI({
        id: this.$route.query.shopId,
        isFilings: 1,
      });
      Notify({ type: "success", message: "备案成功" });
      setTimeout(() => {
        this.$router.push({
          name: "Home",
        });
      }, 1000);
    },
    open() {
      this.form = {};
      this.shopData = {};
      this.imageList = [];
      this.show = true;
      this.queryShopList();
    },
  },
};
</script>
<style lang="scss" scoped></style>
