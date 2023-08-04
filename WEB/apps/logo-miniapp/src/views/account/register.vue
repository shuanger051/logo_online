<template>
  <div class="page-wrap">
    <!-- 注册页 -->
    <van-form @submit="onSubmit">
      <van-field label="姓名" v-model="formData.nickName" />
      <van-field label="证件号码" v-model="formData.idCard" />
      <van-field label="性别" v-model="formData.gender" />
      <van-field label="密码" v-model="formData.password" />
      <van-field label="手机号" v-model="formData.mobile" />
      <!-- 提交栏 -->
      <submit-bar>
        <var-button block type="primary">确认并注册</var-button>
      </submit-bar>
    </van-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      formData: {},
    };
  },
  methods: {
    onSubmit() {},
  },
};
</script>
