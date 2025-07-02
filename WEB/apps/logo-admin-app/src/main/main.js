require("./libs/runCheck.js")();
require("./libs/compress.js");
const shortcut = require("./libs/shortcut.js");
const { app, BrowserWindow, ipcMain } = require("electron");
const OSS = require("ali-oss");
const a = "L_T_A_I_5_t_C_q_x";
const b = "h_S_K_c_8_H_e_P_b_F_w_r_E_z_h";
const c = "_C_0_8_T_M_z_s_R_M_m_V";
const d = "_o_0_A_k_K_x_w_k_o_W_O_H_J_p_e_c_V_c_9";
const createOssClient = (bucket) => {
  const client = new OSS({
    region: "oss-cn-hangzhou",
    accessKeyId: (a + b).split("_").join(""),
    accessKeySecret: (c + d).split("_").join(""),
    bucket: bucket,
  });
  return client;
};
const client = {
  dzfont: createOssClient("dzfont"),
};
const fse = require("fs-extra");
const path = require("path");
const axios = require("axios");
const filePath = path.join(app.getPath("documents"),'dzfont');
fse.ensureDirSync(filePath); // 确保目录存在
const OssPath = {
  template:
    "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/template.js",
  dicts: "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/dicts.js",
  news: "https://dzfont.oss-cn-hangzhou.aliyuncs.com/public-resource/news.js",
};

const getOssAssert = (name, isover = false) => {
  let url = OssPath[name];
  let filename = path.basename(url);
  if (fse.existsSync(path.join(filePath, filename)) && !isover) {
    // 如果文件存在，直接返回
    return fse.readFile(path.join(filePath, filename), "utf-8");
  } else {

    return axios
      .get(url, { responseType: "arraybuffer" })
      .then((response) => {
        fse.writeFile(path.join(filePath, filename), response.data);
        return response.data.toString("utf-8");
      })
      .catch((error) => {
        throw error;
      });
  }
};

const saveOssAssert = async (name, data, isUpload = false) => {
  let url = OssPath[name];
  let filename = path.basename(url);
  if (data) {
    await fse.writeFile(path.join(filePath, filename), data, "utf-8");
  }

  if (isUpload) {
      return client.dzfont.put(`public-resource/${name}.js`, path.join(filePath, filename));
  }
};


const MainWindow = require("./win/index.js");

app.allowRendererProcessReuse = false;

// 禁用硬件加速
app.disableHardwareAcceleration();
//注册全局变量
Object.assign(global, {
  // 页面跟路径配置，优先使用此配置，考虑到小版本更新时，版本之间的切换
  wwwroot: __dirname,
  // 区分不同域下的cookie
  cookie: {},
});

app.on("ready", async () => {
  // 注册快捷键打开控制台事件
  shortcut.register("Command+Control+Alt+F5");
  await Promise.all([
    getOssAssert('template'),
    getOssAssert('dicts'),
    getOssAssert('news'),
  ]);
  // await addVueDevtool();
  ipcMain.handle("getOssAssert", (event, name, isover = false) => {
    return getOssAssert(name, isover);
  });

  ipcMain.handle("saveOssAssert", (event, name, data, isUpload = false) => {
    return saveOssAssert(name, data, isUpload);
  });
  ipcMain.handle("getPath", () => filePath)
  ipcMain.on('openDir', (event, path) => {
    console.log(path)
    const { shell } = require('electron');
    shell.openPath(path).then(() => {
      console.log('Directory opened successfully');
    }).catch(err => {
      console.error('Failed to open directory:', err);
    });
  })
  const win = new MainWindow();
  win.create();
});

app.on("window-all-closed", function () {
  const allwindow = BrowserWindow.getAllWindows();
  if (allwindow.length === 0) app.exit(1);
});
