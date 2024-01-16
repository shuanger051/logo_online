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
          v-explain
          v-model="formData.shopName"
          :rules="rules.shopName"
        />
        <field-picker
          required
          label="营业类型"
          placeholder="请选择"
          name="industryType"
          v-explain
          v-model="formData.industryType"
          :columns="DictIndustryTypeArr"
          :rules="rules.industryType"
        />
        <field-picker
          required
          label="所属地区"
          placeholder="请选择"
          name="address"
          v-explain
          v-model="formData.address"
          :columns="area"
          :rules="rules.address"
        />
        <van-field
          required
          label="详细地址"
          placeholder="请输入精确到道路和门牌号"
          name="addressDetail"
          v-explain
          v-model="formData.addressDetail"
          :rules="rules.addressDetail"
        />
        <field-picker
          required
          label="营业年限"
          placeholder="请选择"
          name="bizYears"
          v-explain
          v-model="formData.bizYears"
          :columns="DictBizYearsArr"
          :rules="rules.bizYears"
        />
        <field-picker
          required
          label="店铺属性"
          placeholder="请选择"
          name="shopsType"
          v-explain
          v-model="formData.shopsType"
          :columns="DictShopsTypeArr"
          :rules="rules.shopsType"
        />
        <van-field
          label="备注"
          placeholder="请输入"
          name="remark"
          v-explain
          v-model="formData.remark"
        />
      </van-panel>
      <!-- 店招信息 -->
      <van-panel title="店招信息">
        <van-field
          required
          label="店招名称"
          placeholder="请输入"
          name="logoName"
          v-explain
          v-model="formData.logoName"
          :rules="rules.logoName"
        />
        <van-field
          required
          label="店招长度"
          type="number"
          placeholder="请输入"
          name="logoHeight"
          v-explain
          v-model="formData.logoHeight"
          :rules="rules.logoHeight"
        >
          <template slot="extra">米</template>
        </van-field>
        <van-field
          required
          label="店招宽度"
          type="number"
          placeholder="请输入"
          name="logoWidth"
          v-explain
          v-model="formData.logoWidth"
          :rules="rules.logoWidth"
        >
          <template slot="extra">米</template>
        </van-field>
        <field-picker
          required
          label="店招材质"
          placeholder="请选择"
          name="material"
          v-explain
          v-model="formData.material"
          :columns="DictMaterialArr"
          :rules="rules.material"
        />
        <van-field
          required
          label="店招数量"
          type="number"
          placeholder="请输入"
          name="logoNum"
          v-explain
          v-model="formData.logoNum"
          :rules="rules.logoNum"
        />
      </van-panel>
      <!-- 经办人信息 -->
      <van-panel title="经办人信息">
        <van-field
          required
          label="姓名"
          placeholder="请输入"
          name="handledByName"
          v-explain
          v-model="formData.handledByName"
          :rules="rules.handledByName"
        />
        <van-field
          required
          label="身份证号"
          placeholder="请输入"
          name="handledByIdCard"
          v-explain
          v-model="formData.handledByIdCard"
          :rules="rules.handledByIdCard"
        />
        <van-field
          required
          label="联系电话"
          placeholder="请输入"
          name="handledByPhone"
          v-explain
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
          name="handledByPhotoFront"
          v-explain
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
          name="handledByPhotoOpposite"
          v-explain
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
        <van-field
          label="营业执照"
          required
          name="attachmentType2"
          v-explain
          :rules="rules.attachmentType2"
        >
          <van-uploader
            v-model="attachmentType[2]"
            slot="input"
            :max-count="1"
            :after-read="(evt) => doAfterRead(evt, 2)"
          />
        </van-field>
        <van-field
          label="租赁合同"
          required
          name="attachmentType3"
          v-explain
          :rules="rules.attachmentType3"
        >
          <van-uploader
            v-model="attachmentType[3]"
            slot="input"
            :max-count="3"
            :after-read="(evt) => doAfterRead(evt, 3)"
          />
        </van-field>
        <van-field
          label="商铺正面照"
          required
          name="attachmentType1"
          v-explain
          :rules="rules.attachmentType1"
        >
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
import { mapDictOptions } from "@/store/helpers";
import { runPromiseInSequence } from "@/utils/util";
import * as validator from "@/utils/validator";

const PREFIX_IMG_JPG = "data:image/jpeg;base64,";
const BASE64_REGX = /^data(.+)base64,/;

export default {
  data() {
    const { fieldExplain } = window.miniAppConfig;
    return {
      formData: {
        logoNum: 1,
      },
      // 字段说明
      fieldExplain,
      // 材料档案
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
      // 店招材质
      DictMaterialArr: mapDictOptions("material"),
    }),
    // 校验规则
    rules() {
      return {
        shopName: [{ required: true, message: "请输入" }],
        industryType: [{ required: true, message: "请选择" }],
        address: [{ required: true, message: "请选择" }],
        addressDetail: [{ required: true, message: "请输入" }],
        bizYears: [{ required: true, message: "请选择" }],
        shopsType: [{ required: true, message: "请选择" }],
        logoHeight: [{ required: true, message: "请输入" }],
        logoWidth: [{ required: true, message: "请输入" }],
        logoNum: [{ required: true, message: "请输入" }],
        logoName: [{ required: true, message: "请输入" }],
        material: [{ required: true, message: "请选择" }],
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
    // 所属地区
    area() {
      return [
        {
          text: "上城区",
          value: "上城区",
          children: [
            { text: "高银街" },
            { text: "民心路" },
            { text: "太平门直街" },
          ],
        },
        {
          text: "拱墅区",
          value: "拱墅区",
          children: [
            { text: "龙游路" },
            { text: "武林路" },
            { text: "横长寿路" },
          ],
        },
        {
          text: "西湖区",
          value: "西湖区",
          children: [
            { text: "西溪路" },
            { text: "文三路" },
            { text: "马塍路" },
            { text: "阔石板" },
          ],
        },
        {
          text: "滨江区",
          value: "滨江区",
          children: [
            { text: "滨盛路" },
            { text: "江汉路" },
            { text: "江南大道" },
          ],
        },
        {
          text: "萧山区",
          value: "萧山区",
          children: [
            { text: "河上老街" },
            { text: "湘湖金融小镇慢生活街" },
            { text: "金城路" },
          ],
        },
        {
          text: "余杭区",
          value: "余杭区",
          children: [
            { text: "仓兴街" },
            { text: "联创街" },
            { text: "海鸥路" },
          ],
        },
        {
          text: "临平区",
          value: "临平区",
          children: [
            { text: "东大街" },
            { text: "北大街" },
            { text: "西大街" },
          ],
        },
        {
          text: "钱塘区",
          value: "钱塘区",
          children: [
            { text: "金沙湖休闲特色街区" },
            { text: "金沙大道" },
            { text: "福雷德支路" },
          ],
        },
        {
          text: "富阳区",
          value: "富阳区",
          children: [
            { text: "文教路" },
            { text: "文居街" },
            { text: "花坞南路" },
          ],
        },
        {
          text: "临安区",
          value: "临安区",
          children: [
            { text: "衣锦街" },
            { text: "苕溪南街文化商业街" },
            { text: "万马路" },
          ],
        },
        {
          text: "建德市",
          value: "建德市",
          children: [
            { text: "双江街" },
            { text: "美好广场" },
            { text: "新安东路" },
          ],
        },
        {
          text: "桐庐县",
          value: "桐庐县",
          children: [
            { text: "梅林路" },
            { text: "迎春路" },
            { text: "瑶琳路" },
          ],
        },
        {
          text: "淳安县",
          value: "淳安县",
          children: [
            { text: "骑龙巷" },
            { text: "环湖北路" },
            { text: "珍珠七路" },
          ],
        },
      ];
    },
  },
  created() {
    const { shopId } = this.$route.query;
    // 查询商户信息
    this.queryMerchantInfo();
    // 如果穿了shopId则查询详情
    if (shopId) this.queryShopInfo(shopId);
    // 查询字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["bizYears", "industryType", "shopsType", "material"],
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
        isFilings: "1", // 变更为审核中
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
        .catch((err) => this.$toast.fail(err.msg || "保存失败"));
    },
    // 更新
    doUpdate(payload) {
      const load = this.$toast.loading("请稍等...");
      runPromiseInSequence([
        // 更新商铺信息
        () => shopService.updateShopsInfoAPI(payload),
        // 更新审核状态
        () =>
          shopService.updateShopsFilingsStatusAPI({
            id: payload.id,
            isFilings: "0",
            checkInfo: "",
          }),
      ])()
        // 关闭加载中
        .finally(load.clear)
        // 保存成功
        .then((res) => {
          // 变更审核状态
          this.$toast.success({
            message: "保存成功",
            onClose: () => {
              this.$router.push({ path: "/" });
            },
          });
        })
        .catch((err) => this.$toast.fail(err.msg || "保存失败"));
    },
    // 查询商户信息
    queryMerchantInfo() {
      const { customerName } = this.userInfo;
      shopService
        .getCustomerInfoByUserNameAPI({ customerName })
        // 获取商户信息
        .then((res) => {
          const { merchant } = res.data;
          // 更新本地商户信息
          this.$store.commit("user/setMerchantInfo", merchant);
        });
    },
    // 查询商铺信息
    queryShopInfo(shopsId) {
      shopService
        .getShopsInfoByIdAPI({ shopsId })
        // 获取商铺信息
        .then((res) => {
          let { data } = res;
          // 获取证件照原图
          shopService
            .getShopsIdCardByIdAPI({ id: data.id })
            .then((res) => {
              ["handledByPhotoFront", "handledByPhotoOpposite"].forEach(
                (key) => {
                  const content = PREFIX_IMG_JPG + _.get(res, ["data", key]);
                  const list = [
                    {
                      content,
                      url: content,
                      isImage: true,
                    },
                  ];
                  this.$set(this.formData, key, list);
                }
              );
            })
            .catch(() => {
              this.$toast.fail("证件照获取失败，请重新上传");
            });

          // 设置商铺信息
          Object.keys(data).forEach((key) =>
            this.$set(this.formData, key, data[key])
          );

          // 档案数据分类
          this.attachmentType = data.list.reduce((dtm, item) => {
            const { attachmentType: key } = item;
            // 存在key则保存
            if (key) {
              item.isImage = true;
              item.url = item.urlPath;
              if (!dtm[key]) dtm[key] = [item];
              else dtm[key].push(item);
            }
            return dtm;
          }, {});
        });
    },
    // 获取证件照原图
    getShopsIdCard() {
      const { id } = this.formData;
      shopService.getShopsInfoByIdAPI({ id }).then((res) => {
        return res.data;
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
            attachmentType: type
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
