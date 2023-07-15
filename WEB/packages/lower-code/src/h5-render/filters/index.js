/**
 *
 * @description 定义过滤器模块
 */

import Vue from 'vue'
import { formatImgUrlFunc } from '@Utils/utils'

/**
 * 链接拼接
 * @param data 图片链接
 * @return
 */
export function imgUrlReplace(data) {
  if (data) {
    return formatImgUrlFunc(data) // 将图片路径转成标准路径
  } else {
    return ''
  }
}

// register global utility filters.
export const filters = {
  imgUrlReplace
}

Object.keys(filters).forEach((key) => {
  Vue.filter(key, filters[key])
})
