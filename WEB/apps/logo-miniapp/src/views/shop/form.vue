<template>
  <div class="page-wrap">
    <van-form @submit="onSubmit">
      <!-- 商铺信息 -->
      <van-panel title="商铺信息">
        <van-field
          required
          label="行业类型"
          placeholder="请输入"
          v-model="formData.type"
        />
        <van-field
          required
          label="商铺地址"
          placeholder="请输入"
          v-model="formData.address"
        />
        <van-field
          required
          label="营业状态"
          placeholder="请输入"
          v-model="formData.merchantStatus"
        />
        <van-field
          label="备注"
          placeholder="请输入"
          type="textarea"
          v-model="formData.remark"
        />
      </van-panel>
      <!-- 商户信息 -->
      <van-panel title="商户信息">
        <van-field
          required
          label="姓名"
          placeholder="请输入"
          v-model="formData.merchantName"
        />
        <van-field
          required
          label="性别"
          placeholder="请输入"
          v-model="formData.gender"
        />
        <van-field
          required
          label="身份证号"
          placeholder="请输入"
          v-model="formData.idCard"
        />
        <van-field
          required
          label="联系电话"
          placeholder="请输入"
          v-model="formData.phone"
        />
      </van-panel>
      <!-- 材料上传 -->
      <van-panel title="材料上传">
        <van-field label="身份证" required>
          <van-uploader slot="input" multiline />
        </van-field>
        <van-field label="营业执照" required>
          <van-uploader slot="input" />
        </van-field>
        <van-field label="租赁合同" required>
          <van-uploader slot="input" />
        </van-field>
      </van-panel>
      <submit-bar>
        <van-button block type="primary">提交</van-button>
      </submit-bar>
    </van-form>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { shopService } from "../../apis";
export default {
  data() {
    return {
      formData: {},
    };
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.user.profiles,
    }),
  },
  created() {
    this.queryShopInfo();
  },
  methods: {
    onSubmit() {
      const { formData } = this;
      shopService.saveMerchantAPI(formData).then((res) => {
        console.log(res);
      });
    },
    // 查询商铺信息
    queryShopInfo() {
      const { idCard, mobile, nickName, gender } = this.userInfo;
      Object.assign(this.formData, {
        idCard,
        phone: mobile,
        merchantName: nickName,
        gender,
      });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 0 60px;
  background-color: @gray-2;
  :deep(.van-panel) {
    &:not(:last-child) {
      margin-bottom: 12px;
    }
  }
}
</style>
