<template>
  <div class="page-wrap">
    <van-divider>材质</van-divider>
    <van-checkbox-group v-model="formData.material" direction="horizontal">
      <van-checkbox
        v-for="item in attrs.material"
        :key="item.value"
        :name="item.value"
      >
        {{ item.label }}
      </van-checkbox>
    </van-checkbox-group>
    <van-divider>类型</van-divider>
    <van-checkbox-group v-model="formData.styles" direction="horizontal">
      <van-checkbox
        v-for="item in attrs.styles"
        :key="item.value"
        :name="item.value"
      >
        {{ item.label }}
      </van-checkbox>
    </van-checkbox-group>
    <van-divider>街道类型</van-divider>
    <van-checkbox-group v-model="formData.streetType" direction="horizontal">
      <van-checkbox
        v-for="item in attrs.streetType"
        :key="item.value"
        :name="item.value"
      >
        {{ item.label }}
      </van-checkbox>
    </van-checkbox-group>
    <submit-bar>
      <van-button block type="primary" @click="onNext">下一步</van-button>
    </submit-bar>
  </div>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
import { appGetItemsByDictKeyInDB } from "core/api";
export default {
  components: { SubmitBar },
  data() {
    return {
      formData: {},
      // 属性列表
      attrs: {
        styles: [
        ],
        material: [],
        streetType: []
      },
    };
  },
  created() {
    appGetItemsByDictKeyInDB({dictKey: 'style'}).then(({data}) => {
      this.attrs.styles = data.map((item) =>{return {
        value: item.itemKey,
        label: item.itemValue
      }})
    })
    appGetItemsByDictKeyInDB({dictKey: 'material'}).then(({data}) => {
      this.attrs.material = data.map((item) =>{return {
        value: item.itemKey,
        label: item.itemValue
      }})
    })
    appGetItemsByDictKeyInDB({dictKey: 'streetType'}).then(({data}) => {
      this.attrs.streetType = data.map((item) =>{return {
        value: item.itemKey,
        label: item.itemValue
      }})
    })
  },
  methods: {
    onNext() {
      const { formData } = this;
      let query = Object.assign({}, this.$route.query);
      Object.keys(formData).forEach((key) => {
        query[key] = formData[key].join(",");
      });
      this.$router.push({ path: "/signboard/template", query });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px 60px;
  :deep(.van-checkbox) {
    margin-bottom: 8px;
  }
}
</style>
