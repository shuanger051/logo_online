<template>
  <div class="page-wrap">
    <van-form @submit="onSubmit">
      <!-- 商铺信息 -->
      <van-panel title="商铺信息">
        <van-field
          required
          label="商铺名称"
          placeholder="请输入"
          name="shopName"
          v-model="formData.shopName"
          :rules="rules.shopName"
        />
        <field-picker
          required
          label="营业类型"
          placeholder="请选择"
          name="industryType"
          v-model="formData.industryType"
          :columns="DictIndustryTypeArr"
          :rules="rules.industryType"
        />
        <van-field
          required
          label="商铺地址"
          placeholder="请输入"
          name="address"
          v-model="formData.address"
          :rules="rules.address"
        />
        <field-picker
          required
          label="营业年限"
          placeholder="请选择"
          name="bizYears"
          v-model="formData.bizYears"
          :columns="DictBizYearsArr"
          :rules="rules.bizYears"
        />
        <field-picker
          required
          label="店铺属性"
          placeholder="请选择"
          name="shopsType"
          v-model="formData.shopsType"
          :columns="DictShopsTypeArr"
          :rules="rules.shopsType"
        />
        <van-field
          label="备注"
          placeholder="请输入"
          name="remark"
          v-model="formData.remark"
        />
      </van-panel>
      <!-- 经办人信息 -->
      <van-panel title="经办人信息">
        <van-field
          required
          label="姓名"
          placeholder="请输入"
          name="handledByName"
          v-model="formData.handledByName"
          :rules="rules.handledByName"
        />
        <van-field
          required
          label="身份证号"
          placeholder="请输入"
          name="handledByIdCard"
          v-model="formData.handledByIdCard"
          :rules="rules.handledByIdCard"
        />
        <van-field
          required
          label="联系电话"
          placeholder="请输入"
          name="handledByPhone"
          v-model="formData.handledByPhone"
          :rules="rules.handledByPhone"
        />
      </van-panel>
      <!-- 材料上传 -->
      <van-panel>
        <van-cell
          class="van-panel__header"
          slot="header"
          title="材料上传"
          value="*图片大小请控制在500KB~5M以内"
        />
        <van-field
          label="身份证正面"
          required
          :rules="rules.handledByPhotoFront"
        >
          <van-uploader
            v-model="formData.handledByPhotoFront"
            slot="input"
            multiline
            :max-count="1"
            :after-read="(evt) => doAfterRead(evt)"
          />
        </van-field>
        <van-field
          label="身份证反面"
          required
          :rules="rules.handledByPhotoOpposite"
        >
          <van-uploader
            v-model="formData.handledByPhotoOpposite"
            slot="input"
            multiline
            :max-count="1"
            :after-read="(evt) => doAfterRead(evt)"
          />
        </van-field>
        <van-field label="营业执照" required :rules="rules.attachmentType2">
          <van-uploader
            v-model="attachmentType[2]"
            slot="input"
            :max-count="1"
            :after-read="(evt) => doAfterRead(evt, 2)"
          />
        </van-field>
        <van-field label="租赁合同" required :rules="rules.attachmentType3">
          <van-uploader
            v-model="attachmentType[3]"
            slot="input"
            :max-count="3"
            :after-read="(evt) => doAfterRead(evt, 3)"
          />
        </van-field>
        <van-field label="商铺正面照" required :rules="rules.attachmentType1">
          <van-uploader
            v-model="attachmentType[1]"
            slot="input"
            :max-count="1"
            :after-read="(evt) => doAfterRead(evt, 1)"
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
import { shopService } from "@/apis";
import { resolveImgUrl } from "core/support/imgUrl";
import { mapDictOptions } from "@/store/helpers";
import * as validator from "@/utils/validator";

const PREFIX_IMG_JPG = "data:image/jpeg;base64,";
const BASE64_REGX = /^data(.+)base64,/;

export default {
  data() {
    return {
      formData: {},
      attachmentType: {
        // 商铺正面照
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
      // 行业类别
      DictIndustryTypeArr: mapDictOptions("industryType"),
      // 营业念想
      DictBizYearsArr: mapDictOptions("bizYears"),
      // 商铺属性
      DictShopsTypeArr: mapDictOptions("shopsType"),
    }),
    // 校验规则
    rules() {
      return {
        shopName: [{ required: true, message: "请输入" }],
        industryType: [{ required: true, message: "请输入" }],
        address: [{ required: true, message: "请输入" }],
        bizYears: [{ required: true, message: "请选择" }],
        shopsType: [{ required: true, message: "请选择" }],
        handledByName: [{ required: true, message: "请输入" }],
        handledByIdCard: [
          { required: true, message: "请输入" },
          { validator: validator.checkIdCard, message: "身份证格式不正确" },
        ],
        handledByPhone: [
          { required: true, message: "请输入" },
          { validator: validator.checkMobile, message: "手机号格式不正确" },
        ],
        handledByPhotoFront: [
          { required: true, message: "请上传身份证正面照" },
        ],
        handledByPhotoOpposite: [
          { required: true, message: "请上传身份证反面照" },
        ],
        attachmentType1: [{ required: true, message: "请上传商铺正面照" }],
        attachmentType2: [{ required: true, message: "请上传营业执照" }],
        attachmentType3: [{ required: true, message: "请上传租赁合同" }],
      };
    },
  },
  created() {
    const { shopId } = this.$route.query;
    // 如果穿了shopId则查询详情
    if (shopId) this.queryShopInfo(shopId);
    // 查询字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["bizYears", "industryType", "shopsType"],
    });
  },
  methods: {
    onSubmit() {
      const { formData, attachmentType, merchantInfo } = this;
      // 合并档案列表
      const list = Object.keys(attachmentType).reduce((list, key) => {
        const arr = attachmentType[key];
        // 组装档案数据
        arr.forEach((item) => {
          list.push(
            _.omit(item, [
              "isImage",
              "content",
              "message",
              "status",
              "file",
              "url",
            ])
          );
        });
        return list;
      }, []);
      // 组装提交数据报文
      const payload = Object.assign({}, formData, {
        list, // 档案材料
        merchantId: merchantInfo.id, // 商户id
      });

      // 身份证数据
      ["handledByPhotoFront", "handledByPhotoOpposite"].forEach((key) => {
        const data = _.get(formData, [key, 0, "content"]);
        payload[key] = data.replace(BASE64_REGX, "");
      });

      // 存在id则为更新
      if (formData.id) this.doUpdate(payload);
      // 其他为新增
      else this.doAdd(payload);
    },
    // 新增
    doAdd(payload) {
      const load = this.$toast.loading("请稍等...");
      // 保存商铺信息
      shopService
        .saveShopsInfoAPI(payload)
        // 关闭加载中
        .finally(load.clear)
        // 保存成功
        .then((res) => {
          this.$toast.success({
            message: "保存成功",
            onClose: () => {
              this.$router.push({ path: "/" });
            },
          });
        })
        .catch(() => this.$toast.fail("保存失败"));
    },
    // 更新
    doUpdate(payload) {
      const load = this.$toast.loading("请稍等...");
      shopService
        .updateShopsInfoAPI(payload)
        // 关闭加载中
        .finally(load.clear)
        // 保存成功
        .then((res) => {
          this.$toast.success({
            message: "保存成功",
            onClose: () => {
              this.$router.push({ path: "/" });
            },
          });
        })
        .catch(() => this.$toast.fail("保存失败"));
    },
    // 查询商铺信息
    queryShopInfo(shopsId) {
      shopService
        .getShopsInfoByIdAPI({ shopsId })
        // 获取商铺信息
        .then((res) => {
          let item = res.data;
          // 身份证图片数据
          ["handledByPhotoFront", "handledByPhotoOpposite"].forEach((key) => {
            if (item[key]) {
              const data = PREFIX_IMG_JPG + item[key];
              item[key] = [
                {
                  url: data,
                  content: data,
                  isImage: true,
                },
              ];
            } else item[key] = [];
          });

          // 设置商铺信息
          Object.keys(item).forEach((key) =>
            this.$set(this.formData, key, item[key])
          );

          // 档案数据分类
          this.attachmentType = item.list.reduce((dtm, item) => {
            const { attachmentType: key } = item;
            // 存在key则保存
            if (key) {
              item.isImage = true;
              item.url = resolveImgUrl(item.urlPath);
              if (!dtm[key]) dtm[key] = [item];
              else dtm[key].push(item);
            }
            return dtm;
          }, {});
        });
    },
    // 上传
    doAfterRead(file, type) {
      const formData = new FormData();
      formData.append("file", file.file);
      formData.append("attachmentType", type);
      // formData.append("shopsId", this.$route.query.shopId);

      file.status = "uploading";
      shopService
        .uploadShopsAttachmentAPI(formData)
        // 上传成功
        .then((res) => {
          Object.assign(file, res.data, {
            status: "done",
            shopsId: this.formData.id,
          });
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
    &__header {
      font-weight: 700;
      .van-cell {
        &__title {
          flex: 0 1 auto;
        }
        &__value {
          font-weight: normal;
          color: @red;
        }
      }
    }
    &:not(:last-child) {
      margin-bottom: 12px;
    }
  }
}
</style>
