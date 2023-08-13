<template>
  <div class="page-wrap">
    <van-form @submit="onSubmit">
      <!-- 商铺信息 -->
      <van-panel title="商铺信息">
        <van-field
          required
          label="行业类型"
          placeholder="请输入"
          v-model="formData.industryType"
        />
        <van-field
          required
          label="商铺地址"
          placeholder="请输入"
          v-model="formData.address"
        />
        <van-field
          required
          label="营业年限"
          placeholder="请输入"
          v-model="formData.bizYears"
        />
        <van-field
          required
          label="店铺属性"
          placeholder="请输入"
          v-model="formData.shopsType"
        />
        <van-field
          required
          label="备注"
          placeholder="请输入"
          v-model="formData.remark"
        />
      </van-panel>
      <!-- 材料上传 -->
      <van-panel title="材料上传">
        <van-field label="身份证" required>
          <van-uploader
            v-model="attachmentType[1]"
            slot="input"
            multiline
            :after-read="(evt) => doAfterRead(1, evt)"
          />
        </van-field>
        <van-field label="营业执照" required>
          <van-uploader
            v-model="attachmentType[2]"
            slot="input"
            :after-read="(evt) => doAfterRead(2, evt)"
          />
        </van-field>
        <van-field label="租赁合同" required>
          <van-uploader
            v-model="attachmentType[3]"
            slot="input"
            :after-read="(evt) => doAfterRead(3, evt)"
          />
        </van-field>
      </van-panel>
      <submit-bar>
        <van-button block type="primary">提交</van-button>
      </submit-bar>
    </van-form>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { shopService } from "../../apis";
export default {
  data() {
    return {
      formData: {},
      attachmentType: {
        // 身份证
        1: [],
        // 营业执照
        2: [],
        // 租赁合同
        3: [],
      },
    };
  },
  computed: {
    ...mapState({
      // 用户信息
      userInfo: (state) => state.user.profiles,
      // 商户信息
      merchantInfo: (state) => state.user.merchant,
    }),
  },
  created() {
    this.queryShopInfo();
  },
  methods: {
    onSubmit() {
      const { formData, attachmentType } = this;
      // 合并档案列表
      const list = Object.keys(attachmentType).reduce((list, key) => {
        const arr = attachmentType[key];
        // 组装档案数据
        arr.forEach((item) => {
          list.push({
            attachmentName: item.attachmentName,
            attachmentPath: item.attachmentPath,
            attachmentType: item.attachmentType,
            fileName: item.fileName,
            fileType: item.urlPath,
          });
        });
        return list;
      }, []);
      // 组装提交数据报文
      const payload = Object.assign({}, formData, { list });
      // 存在id则为更新
      if (formData.id) this.doUpdate(payload);
      // 其他为新增
      else this.doAdd(payload);
    },
    // 新增
    doAdd(payload) {
      // 保存商铺信息
      shopService
        .saveShopsInfoAPI(payload)
        // 保存成功
        .then((res) => {
          console.log(res);
        });
    },
    // 更新
    doUpdate(payload) {
      shopService
        .updateShopsInfoAPI(payload)
        // 保存成功
        .then((res) => {
          console.log(res);
        });
    },
    // 查询商铺信息
    queryShopInfo() {
      const { shopId } = this.$route.query;
      const { customerName } = this.userInfo;
      shopService
        .getCustomerInfoByUserNameAPI({ customerName })
        // 获取商铺信息
        .then((res) => {
          const { shopsList, merchant } = res.data;
          // 添加商户id
          this.formData.merchantId = merchant.id;
          // 存在shopid则为编辑
          if (shopId) {
            const item = shopsList.find((item) => (item.id = shopId));
            // 设置商铺信息
            Object.keys(item).forEach((key) =>
              this.$set(this.formData, key, item[key])
            );
            // 档案数据分类
            this.attachmentType = item.list.reduce((dtm, item) => {
              const { attachmentType: key } = item;
              if (!dtm[key]) dtm[key] = [item];
              else dtm[key].push(item);
            }, {});
          }
        });
    },
    // 上传
    doAfterRead(type, file) {
      const formData = new FormData();
      formData.append("file", file.file);
      formData.append("attachmentType", type);
      file.status = "uploading";
      shopService
        .uploadShopsAttachmentAPI(formData)
        // 上传成功
        .then((res) => {
          Object.assign(file, res.data, { status: "done" });
        })
        // 上传失败
        .catch(() => {
          file.status = "failed";
          file.message = "上传失败";
        });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 0 60px;
  background-color: @gray-2;
  :deep(.van-panel) {
    &:not(:last-child) {
      margin-bottom: 12px;
    }
  }
}
</style>
