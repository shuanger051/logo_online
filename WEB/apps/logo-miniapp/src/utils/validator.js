/**
 * 手机号校验
 * @param {*} val
 * @returns
 */
export function checkMobile(val) {
  return /^1[3-9]\d{9}$/.test(val);
}

/**
 * 身份证号校验
 * @param {*} val 
 * @returns 
 */
export function checkIdCard(val) {
  const city =
    "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91"; // 身份证归属地列表
  const ci = ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"]; // 校验码
  const wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]; // 加权因子

  // x替换
  val = val.replace(/x/, "X");

  // 证件格式有误
  if (!/^\d{17}(\d|X)$/.test(val)) return false;

  // 归属地不正确
  if (city.indexOf(val.slice(0, 2)) < 0) return false;
  
  // 校验码
  const str = val.substring(0, 17); // 获证件取前17位
  const sum = wi.reduce((sum, w, i) => sum + w * str[i], 0); // 计算加权因子之和
  const ckVal = sum % 11; // 取模
  return ci[ckVal] == val[17];
}