<template>
  <van-popup
    class="popup-login"
    v-model="show"
    :closeable="true"
    :close-on-click-overlay="false"
  >
    <van-nav-bar title="登录" />
    <van-form @submit="onSubmit">
      <van-field
        required
        name="customerName"
        v-model="formData.customerName"
        maxlength="11"
        label-width="4em"
        label="手机号"
        placeholder="请输入"
        :rules="rules.customerName"
      />
      <van-field
        required
        name="password"
        v-model="formData.password"
        label-width="4em"
        label="密码"
        type="password"
        placeholder="请输入"
        :rules="rules.password"
      />
      <van-field>
        <van-button slot="input" block round type="primary">登录</van-button>
      </van-field>
    </van-form>
  </van-popup>
</template>
<script>
import store from "@/store";
import eventBus from "@/core/eventBus";
import { commonService, accountService } from "@/apis";
import { runPromiseInSequence } from "@/utils/util";
export default {
  data() {
    return {
      show: false,
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
    const { accesstoken } = this.$route.query;
    // 浙里办授权登录
    if (accesstoken) {
      this.zlbAuthLogin(accesstoken);
    }
    eventBus.$on("login", () => (this.show = true));
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
