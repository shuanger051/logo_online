<template>
  <div class="intelligence-design">
    <van-panel
      title="智能设计"
      desc="智能设计是系统依据您的店铺类型、位置等信息，采用人工智能技术自动设计生成店铺店招，您可以在输入您的店铺名称后，方便快捷的选择系统生成的店铺店招。"
    >
    </van-panel>
    <van-form @submit="onSubmit">
      <van-field
        v-model="name"
        name="店招名称"
        label="店招名称"
        placeholder="店招名称"
        :rules="[{ required: true, message: '请填写店招名称' }]"
      />
      <div style="margin: 16px">
        <van-button round block type="info" native-type="submit"
          >确定</van-button
        >
      </div>
    </van-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      name: ''
    }
  },
  methods: {
    onSubmit() {
      this.$router.push({
        name: "intelligenceTemplate",
        query: {
          shopId: this.$route.query.shopId,
          name: this.name
        }
      })
    }
  }
};
</script>
<style lang="less" scoped>
</style>
