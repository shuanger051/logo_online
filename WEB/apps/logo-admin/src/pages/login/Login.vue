<template>
  <common-layout>
    <div class="top">
      <div class="header">
        <img alt="logo" class="logo" src="@/assets/img/logo.png" />
        <span class="title">{{ systemName }}</span>
      </div>
      <div class="desc">Ant Design 是西湖区最具影响力的 Web 设计规范</div>
    </div>
    <div class="login">
      <a-form @submit="onSubmit" :form="form">
        <a-alert
          type="error"
          :closable="true"
          v-if="error"
          :message="error"
          @close="onClose"
          showIcon
          style="margin-bottom: 24px"
        />
        <a-form-item>
          <a-input
            autocomplete="autocomplete"
            size="large"
            placeholder="请输入账户名"
            v-decorator="[
              'userName',
              {
                rules: [
                  { required: true, message: '请输入账户名', whitespace: true },
                ],
              },
            ]"
          >
            <a-icon slot="prefix" type="user" />
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-input
            size="large"
            placeholder="请输入密码"
            autocomplete="autocomplete"
            type="password"
            v-decorator="[
              'password',
              {
                rules: [
                  { required: true, message: '请输入密码', whitespace: true },
                ],
              },
            ]"
          >
            <a-icon slot="prefix" type="lock" />
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-input
            size="large"
            placeholder="请输入验证码"
            autocomplete="autocomplete"
            v-decorator="[
              'captchaCode',
              {
                rules: [
                  { required: true, message: '请输入验证码', whitespace: true },
                ],
              },
            ]"
          >
            <a-icon slot="prefix" type="picture" />
            <img slot="suffix" :src="imgCode" />
          </a-input>
        </a-form-item>
        <div>
          <a-checkbox :checked="true">自动登录</a-checkbox>
          <a style="float: right">忘记密码</a>
        </div>
        <a-form-item>
          <a-button
            :loading="logging"
            style="width: 100%; margin-top: 24px"
            size="large"
            htmlType="submit"
            type="primary"
            >登录</a-button
          >
        </a-form-item>
        <div>
          其他登录方式
          <a-icon class="icon" type="alipay-circle" />
          <a-icon class="icon" type="taobao-circle" />
          <a-icon class="icon" type="weibo-circle" />
          <router-link style="float: right" to="/dashboard/workplace"
            >注册账户</router-link
          >
        </div>
      </a-form>
    </div>
  </common-layout>
</template>

<script>
import CommonLayout from "@/layouts/CommonLayout";
import { getRoutesConfig } from "@/services/user";
import { setAuthorization } from "@/utils/request";
import { loadRoutes } from "@/utils/routerUtil";
import { mapMutations } from "vuex";
import { appService } from "@/services";

export default {
  name: "Login",
  components: { CommonLayout },
  data() {
    return {
      logging: false,
      error: "",
      form: this.$form.createForm(this),
    };
  },
  computed: {
    systemName() {
      return this.$store.state.setting.systemName;
    },
    // 图形验证码
    imgCode() {
      return (
        process.env.VUE_APP_API_PREFIX +
        "/logo/sys/img-code/kaptcha?uid=" +
        Math.random()
      );
    },
  },
  methods: {
    ...mapMutations("account", ["setUser", "setPermissions", "setRoles"]),
    onSubmit(e) {
      e.preventDefault();
      console.log(appService);
      this.form.validateFields((err) => {
        if (!err) {
          this.logging = true;
          const userName = this.form.getFieldValue("userName");
          const password = this.form.getFieldValue("password");
          const captchaCode = this.form.getFieldValue("captchaCode");
          // 获取秘钥
          this.getPublicKey()
            //  密码加密
            .then((publicKey) =>
              this.getEncryptSign({ param: password, publicKey })
            )
            // 登录
            .then((password) =>
              appService.login({
                userName,
                password,
                captchaCode,
              })
            )
            .finally(() => {
              this.logging = false;
              this.$router.push({ path: "/home" });
            })
            .catch((err) => {
              this.$message.alert("登录失败", err);
            });
          // login(name, password).then(this.afterLogin);
        }
      });
    },
    // 获取公钥
    getPublicKey() {
      const { rsaPublicKey: key } = this;
      if (key) return Promise.resolve(key);
      return appService
        .getPublicKey()
        .then((res) => {
          console.log(res);
          const key = _.get(res, "data.publicKey");
          this.rsaPublicKey = key;
          return key;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 获取加密串
    getEncryptSign(data) {
      console.log(data);
      return appService
        .encrypt(data)
        .then((res) => _.get(res, "data.sign"))
        .catch((err) => {
          console.warn("rsa加密失败", err);
        });
    },
    afterLogin(res) {
      this.logging = false;
      const loginRes = res.data;
      if (loginRes.code >= 0) {
        const { user, permissions, roles } = loginRes.data;
        this.setUser(user);
        this.setPermissions(permissions);
        this.setRoles(roles);
        setAuthorization({
          token: loginRes.data.token,
          expireAt: new Date(loginRes.data.expireAt),
        });
        // 获取路由配置
        getRoutesConfig().then((result) => {
          const routesConfig = result.data.data;
          loadRoutes(routesConfig);
          this.$router.push("/demo");
          this.$message.success(loginRes.message, 3);
        });
      } else {
        this.error = loginRes.message;
      }
    },
    onClose() {
      this.error = false;
    },
  },
};
</script>

<style lang="less" scoped>
.common-layout {
  .top {
    text-align: center;
    .header {
      height: 44px;
      line-height: 44px;
      a {
        text-decoration: none;
      }
      .logo {
        height: 44px;
        vertical-align: top;
        margin-right: 16px;
      }
      .title {
        font-size: 33px;
        color: @title-color;
        font-family: "Myriad Pro", "Helvetica Neue", Arial, Helvetica,
          sans-serif;
        font-weight: 600;
        position: relative;
        top: 2px;
      }
    }
    .desc {
      font-size: 14px;
      color: @text-color-second;
      margin-top: 12px;
      margin-bottom: 40px;
    }
  }
  .login {
    width: 368px;
    margin: 0 auto;
    @media screen and (max-width: 576px) {
      width: 95%;
    }
    @media screen and (max-width: 320px) {
      .captcha-button {
        font-size: 14px;
      }
    }
    .icon {
      font-size: 24px;
      color: @text-color-second;
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: @primary-color;
      }
    }
  }
}
</style>
