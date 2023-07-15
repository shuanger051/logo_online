import { apiPost, baseCCMUrl, isMPaasPackage, baseMUrl } from '../utils/request'
import { ismPaaSOS } from '@winner-fed/mpaas-jsapi'

const urlFilter = (baseUrl, api) => {
  let base = isMPaasPackage === 'true' ? (ismPaaSOS() ? `com.hundsun.${baseUrl.split('/g/')[1].replace('/v', '')}.${api}` : `${baseMUrl}/m/com.hundsun.${baseUrl.split('/g/')[1].replace('/v', '')}.${api}`) : `${baseUrl}/${api}`
  return base
}

// 获取作品基本信息
export function getMobileWorksContent(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileWorksContent'))
}

// 获取作品基本信息-不鉴权
export function getMobileWorksContentNoAuth(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileWorksContentNoAuth'))
}

// 获取作品页面基本信息
export function getMobileWorksPageContent(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileWorksPageContent'))
}

// 保存作品访问记录
export function postTrackWorksVisit(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'postTrackWorksVisit'))
}

// 保存作品访问记录
export function postTrackWorksStayTime(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'postTrackWorksStayTime'))
}

// 获取公众号信息
export function getMobileAuthorityInfo(params) {
  return apiPost(params, `${urlFilter(baseCCMUrl, 'getMobileAuthorityInfo')}?url=${params.url}`)
}

// 提交表单数据
export function postMobileFormSubmitWithReturn(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'postMobileFormSubmitWithReturn'))
}

// 提交表单数据
export function getMobileCheckUserAuth(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileCheckUserAuth'))
}

export function getCombData(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getCombData'))
}

export function getInaInfo(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getInaInfo'))
}

//获取手机号验证码
export function getMobileSmsVerificationCode(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileSmsVerificationCode'))
}

//产品列表-根据基金数据列表获取最新数据
export function getMobileComponentProduct (params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileComponentProduct'))
}

//产品列表-自动获取
export function getMobileComponentRecProduct (params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileComponentRecProduct'))
}

// 产品推荐信息
export function getMobileRecommendPrdFund(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getMobileRecommendPrdFund'))
}
// 基金收益率组件-基金详情
export function getFundDetails(params) {
  return apiPost(params, urlFilter(baseCCMUrl, 'getFundFieldRateDetails'))
}

// 优惠券基本信息
export function getMobileCouponInfo(params) {
  return apiPost(params, urlFilter(baseCCMUrl, '/getMobileCouponInfo'))
}

// 优惠券领取
export function postMobileCouponReceive(params) {
  return apiPost(params, urlFilter(baseCCMUrl, '/postMobileCouponReceive'))
}