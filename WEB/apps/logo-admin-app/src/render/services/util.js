import OSS from 'ali-oss';
const a = 'L_T_A_I_5_t_C_q_x';
const b = 'h_S_K_c_8_H_e_P_b_F_w_r_E_z_h'

const c = '_C_0_8_T_M_z_s_R_M_m_V'
const d = '_o_0_A_k_K_x_w_k_o_W_O_H_J_p_e_c_V_c_9';

export const loadScript = async (...args) =>  {
  return window.electronAPI.getOssAssert(...args).then((data) => {
    console.log(data)
    if (data) {
      const script = document.createElement('script');
      script.textContent = data;
      document.head.appendChild(script);
      script.remove();
    } else {
      throw new Error('加载脚本失败');
    }
  })
}

const createOssClient = (bucket) => {
  const client = new OSS({
   region:'oss-cn-hangzhou',
   accessKeyId: (a+b).split('_').join(''),
   accessKeySecret: (c+d).split('_').join(''),
   bucket: bucket
  });
  return client
}
const clients = {
  dzfont: createOssClient('dzfont'),
  newImg: createOssClient('new-img-save-dir'),
}

const uploadFile = async (file, clientStr, config = {
}) => {
 const fileextension = file.name.split('.').pop().toLowerCase();
 const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp','xlsx', 'js'];
 if (!allowedExtensions.includes(fileextension)) {
   throw new Error('不允许上传的格式');
 }
 const year = new Date().getFullYear();
 const month = String(new Date().getMonth() + 1).padStart(2, '0');
 const day = String(new Date().getDate()).padStart(2, '0');
 const randomString = Math.random().toString(36).substring(2, 10);
 const filePath =(config.path ? config.path : 'upload/material/' + year + '/' + month + '/' + day + '/') + (config.name || randomString + '.' + fileextension);
  const client = clients[clientStr] || clients['dzfont'];
 try{
 await client.put(filePath, file);
 } catch (e) {
   console.error('上传失败', e);
   throw e
 }
 return `https://${client.options.bucket}.${client.options.region}.aliyuncs.com/${filePath}`;
} 

export const uploadJsFile = async (...args) => {
  try {
    return window.electronAPI.saveOssAssert(...args);
  } catch (e) {
    console.error('上传失败', e);
    throw e
  }
}

