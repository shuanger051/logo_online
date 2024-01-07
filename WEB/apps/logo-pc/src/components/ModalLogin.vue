<template>
  <a-modal
    class="popup-login"
    v-model="show"
    title="登录"
  >
    <a-form-model :model="formData" @submit="onSubmit">
      <a-form-model-item
        required
        name="customerName"
        label="手机号"
        :rules="rules.customerName"
      >
        <a-input
          v-model="formData.customerName"
          placeholder="请输入"
          maxlength="11"
        />
      </a-form-model-item>
      <a-form-model-item
        required
        name="password"
        label="密码"
        :rules="rules.password"
      >
        <a-input
          v-model="formData.password"
          placeholder="请输入"
          type="password"
        />
      </a-form-model-item>
      <a-form-model-item>
        <van-button slot="input" block round type="primary">登录</van-button>
      </a-form-model-item>
    </a-form-model>
  </a-modal>
</template>
<script>
import qs from "qs";
import store from "@/store";
import eventBus from "@/core/eventBus";
import { commonService, accountService } from "@/apis";
import { runPromiseInSequence } from "@/utils/util";
export default {
  data() {
    return {
      show: false,
      loginType: "1", // 1-本地，2-浙里办授权登录
      formData: {
        customerName: "",
        password: "",
      },
    };
  },
  computed: {
    rules() {
      return {
        customerName: [{ required: true, message: "请输入" }],
        password: [{ required: true, message: "请输入" }],
      };
    },
  },
  created() {
    // 从vue-router中获取查询参数
    let { accesstoken } = this.$route.query;
    // 从location.sreach中获取查询参数
    if (!accesstoken) {
      const query = qs.parse(location.search.slice(1));
      accesstoken = query.accesstoken;
    }
    // 浙里办授权登录
    if (accesstoken) {
      this.loginType = "2";
      this.zlbAuthLogin(accesstoken);
    }
    // 未登录提示
    eventBus.$on("login", () => {
      // 本地登录
      if (this.loginType == "1") this.show = true;
      // 第三方登录
      else {
        this.$dialog
          .alert({
            title: "提示",
            message: "未登录，请登入后再试！",
          })
          .then(() => {
            this.$router.replace({ path: "/" });
          });
      }
    });
  },
  methods: {
    // 浙里办统一登录
    zlbAuthLogin(accessToken) {
      return accountService
        .getZLBTokenAPI({
          accessToken,
        })
        .then((res) => {
          const { token, customerInfo } = res.data;
          store.commit("user/setToken", token);
          store.commit("user/setUserInfo", customerInfo);
        })
        .catch((err) => {
          this.$toast.fail("登录失败，请稍后再试");
        });
    },
    // event：登录
    onSubmit() {
      const { customerName, password } = this.formData;
      runPromiseInSequence([
        // 获取秘钥
        this.getPublicKey,
        // 加密
        this.encrypt,
        // 登录
        this.login,
      ])({
        customerName,
        password,
      })
        // 失败提示
        .catch((err) => {
          console.log(err);
          this.$toast.fail("登录失败：" + err.msg);
        });
    },
    // 登录
    login(ctx) {
      return accountService
        .getTokenAPI({
          customerName: ctx.customerName,
          password: ctx.password,
        })
        .then((res) => {
          this.show = false;
          const { token, customerInfo } = res.data;
          store.commit("user/setToken", token);
          store.commit("user/setUserInfo", customerInfo);
        })
        .catch((err) => {
          this.$toast.fail("登录失败，请稍后再试");
        });
    },
    // 获取公钥
    getPublicKey(ctx) {
      return commonService
        .getPublicKey()
        .then((res) => (ctx.publicKey = res.data.publicKey));
    },
    // 加密
    encrypt(ctx) {
      return commonService
        .encrypt({
          param: ctx.password,
          publicKey: ctx.publicKey,
        })
        .then((res) => {
          ctx.password = res.data.sign;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.popup-login {
  width: 300px;
  border-radius: 6px;
  :deep(.van-form) {
    padding: 24px 12px 12px;
    .van-field:last-child {
      margin-top: 12px;
    }
  }
}
</style>
