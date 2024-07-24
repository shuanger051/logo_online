<template>
  <div class="page-wrap">
    <a-tabs
      v-model="formData.streetType"
      :animated="false"
      @change="onTabsChange"
    >
      <a-tab-pane
        v-for="item in streetTypeArr"
        :tab="item.label"
        :key="item.value"
      >
        <a-radio-group v-model="formData.street">
          <a-list :data-source="item.streetArr" :border="false">
            <a-list-item slot="renderItem" slot-scope="item2" :key="item2.uid">
              <a-radio :value="item2.uid">
                {{ item2.name }}
              </a-radio>
            </a-list-item>
          </a-list>
        </a-radio-group>
      </a-tab-pane>
    </a-tabs>

    <div class="action-bar">
      <a-button type="primary" @click="onNext">下一步</a-button>
    </div>
  </div>
</template>
<script>
// import { appGetItemsByDictKeyInDB } from "core/api";
export default {
  data() {
    return {
      formData: {
        street: null,
        streetType: null,
      },
      // 街区列表
      streetTypeArr: [],
    };
  },
  created() {
    const list = window.pageContentJson.streetView;
    // 街区类型
    const data = [
      {
        value: "1,2",
        label: "商业街区",
      },
      {
        value: "3",
        label: "非商业街区",
      },
    ];
    // 设置tab默认值
    this.formData.streetType = data[0].value;
    this.streetTypeArr = data.map((item) => {
      // 道路列表
      const streetArr = list.reduce((arr, item2) => {
        if (item.value.split(",").includes(item2.id)) {
          // 生成道路唯一id
          const list = item2.street.map((s) => {
            s.uid = [item2.id, s.id].join("_");
            return s;
          });
          return arr.concat(...list);
        }
        return arr;
      }, []);
      // 添加其他街道
      streetArr.push({ name: "其他道路", id: "99999999" });
      return {
        value: item.value,
        label: item.label,
        streetArr,
      };
    });
  },
  methods: {
    onTabsChange(evt) {
      this.formData.street = null;
    },
    onNext() {
      const { streetType, street } = this.formData;
      if (!street) this.$message.warn("请选择街区道路");
      // 特色街区-进入一街一景(@todo 进入素材参考)
      else
        this.$router.push({
          path: "/signboard/streetSelect",
          query: {
            streetType,
            street,
          },
        });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px 60px;
  max-width: 1000px;
  margin: 0 auto;
  margin-top: 24px;
  border-radius: 4px;
  background-color: #fff;
  :deep(.ant-checkbox-group) {
    margin-bottom: 12px;
    &-item > span {
      font-size: 15px;
    }
  }
  :deep(.ant-divider) {
    &-inner-text {
      font-size: 15px;
      color: #444;
    }
  }
  .action-bar {
    margin-top: 24px;
  }
}
</style>
