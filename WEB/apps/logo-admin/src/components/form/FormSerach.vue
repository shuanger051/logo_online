<template>
  <a-form class="form-serach" :form="form">
    <a-row :gutter="24">
      <a-col v-for="item in __fields" :key="item.name" :span="6">
        <a-form-item :label="item.label">
          <component
            :is="item.component"
            :name="item.name"
            :allowClear="true"
            v-decorator="[item.name]"
            v-bind="item.props"
          />
        </a-form-item>
      </a-col>
      <!-- 搜索按钮 -->
      <a-col class="serach-action-bar" :span="24">
        <!-- 查询重置 -->
        <a-space>
          <a-button type="primary" @click="onSerach">查询</a-button>
          <a-button type="danger" @click="onReset">重置</a-button>
        </a-space>
        <!-- 其他按钮 -->
        <a-space>
          <slot></slot>
        </a-space>
      </a-col>
    </a-row>
  </a-form>
</template>
<script>
import { Select } from "ant-design-vue";
export default {
  props: {
    // 查询字段
    fields: {
      type: Array,
      required: true,
    },
  },
  computed: {
    // 查询字段添加默认值
    __fields() {
      return this.fields.map((item) => {
        // 组件
        switch (item.component) {
          case "select":
            item.component = Select;
            break;
          default:
            if (!item.component) item.component = "a-input";
            break;
        }
        return item;
      });
    },
  },
  data() {
    // 查询条件
    const form = this.$form.createForm(this, { name: "condition" });
    return {
      form,
    };
  },
  methods: {
    // 重置
    onReset() {
      this.form.resetFields();
    },
    // 查询
    onSerach() {
      const data = this.form.getFieldsValue();
      this.$emit("serach", data);
    },
  },
};
</script>
<style lang="less" scoped>
.form-serach {
  :deep(.ant-form-item) {
    display: flex;
    margin-bottom: 8px;
    &-control-wrapper {
      flex: 1;
    }
  }
  .serach-action-bar {
    padding: 12px 0;
    margin-bottom: 12px;
    & > .ant-space {
      &:not(:last-child) {
        margin-right: 18px;
      }
    }
  }
}
</style>
