<template>
  <div class="intelligence-design">
    <div class="intelligence-design__title">
      <h2>智能设计</h2>
      <p style="width: 600px;">
        智能设计是系统依据您的店铺类型、位置等信息，采用人工智能技术自动设计生成店铺店招，您可以在输入您的店铺名称后，方便快捷的选择系统生成的店铺店招。
      </p>
    </div>
    <a-form-model
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="labelCol"
      :label-align = "'left'" 
      :wrapper-col="wrapperCol"
    >
      <a-form-model-item label="店招名称" prop="name">
        <a-input placeholder="店招名称" v-model="form.name" />
      </a-form-model-item>

      <div style="margin: 16px">
        <a-button type="primary" @click.prevent="onSubmit">确定</a-button>
      </div>
    </a-form-model>
  </div>
</template>
<script>
export default {
  data() {
    return {
      form: {
        name: "",
      },
      rules: {
        name: [{ required: true, message: "请填写店招名称", trigger: "blur" }],
      },
      labelCol: { span: 3 },
      wrapperCol: { span: 14 },
    };
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate((v) => {
        if (v) {
          this.success()
        }
      });
    },
    success() {
      let tp = {
        name: this.form.name,
      }
      this.$router.push({
        name: "intelligenceTemplate",
        query: {
          ...this.$route.query,
          tp: encodeURIComponent(JSON.stringify(tp)),
        },
      });
    }
  },
};
</script>
<style lang="less" scoped>
.intelligence-design__title {
  padding: 20px 0;
}
.intelligence-design {
  width: 1000px;
  margin: 0 auto;
  padding: 0 20px

}
</style>
