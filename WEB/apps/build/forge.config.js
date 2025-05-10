
module.exports = {
  packagerConfig: {
    icon: 'favicon/favicon' // no file extension required
  },
  makers: [
    {
      name: '@electron-forge/maker-dmg',
      config: {
        background: 'favicon/favicon.png',
        format: 'ULFO'
      }
    },
    {
      "name": "@electron-forge/maker-zip",
      "platforms": ["win32"] // 仅针对 Windows
    }
  ]
};