export const resolveImgUrl = (url) => {
  let isDev = process.env.NODE_ENV === "development";
  let prefix = process.env.VUE_APP_API_BASE_URL;
  const reg = /^(http|\/\/:|\/static)/
  if (reg.test(url)) {
    return url
  }
  return isDev ? '/api/logo'+url : prefix + '/logo/'+url

}