<template>
  <div class="page-wrap">
    <!-- 街区选择 -->
    <van-tabs @click="(name) => (streetType = name)">
      <van-tab
        v-for="item in streetTypeArr"
        :title="item.label"
        :name="item.value"
        :key="item.value"
      >
        <van-radio-group v-model="street">
          <van-cell-group>
            <van-cell
              v-for="item2 in item.streetArr"
              clickable
              :title="item2.name"
              :key="item2.uid"
              @click="street = item2.uid"
            >
              <template #right-icon>
                <van-radio :name="item2.uid" />
              </template>
            </van-cell>
          </van-cell-group>
        </van-radio-group>
      </van-tab>
    </van-tabs>
    <submit-bar>
      <van-button type="primary" block @click="onNext">下一步</van-button>
    </submit-bar>
  </div>
</template>
<script>
import { appGetItemsByDictKeyInDB } from "core/api";
export default {
  data() {
    return {
      street: null,
      streetType: null,
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
    this.streetType = data[0].value;
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
      streetArr.push({ name: "其他道路", uid: "99999999" });
      return {
        value: item.value,
        label: item.label,
        streetArr,
      };
    });
  },
  methods: {
    onNext() {
      const { streetType, street } = this;
      if (!street) this.$notify({ type: "warning", message: "请选择街区道路" });
      else
        this.$router.push({
          path: "/signboard/sample",
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
  box-sizing: border-box;
  padding: 24px 12px 64px;
  background-color: @gray-2;
  :deep(.van-panel) {
    margin-bottom: 12px;
    border-radius: 8px;
    overflow: hidden;
    &__header {
      position: relative;
      line-height: 24px;
      font-size: 16px;
      &::before {
        content: "";
        display: inline-block;
        margin-right: 8px;
        transform: translateY(5px);
        width: 4px;
        height: 14px;
        background-color: @blue;
      }
    }
    &__content {
      padding: 12px 0;
      .van-col {
        box-sizing: border-box;
        padding: 12px 24px;
      }
      .van-radio {
        font-size: 14px;
      }
    }
  }
}
</style>
