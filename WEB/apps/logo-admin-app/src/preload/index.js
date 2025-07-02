const { contextBridge, ipcRenderer } = require('electron')
contextBridge.exposeInMainWorld('electronAPI', {
  getOssAssert: (name, isover) => ipcRenderer.invoke('getOssAssert', name, isover),
  saveOssAssert: (name, data, isUpload) => ipcRenderer.invoke('saveOssAssert', name, data, isUpload),
  getPath: () => ipcRenderer.invoke('getPath'),
  openDir: (path) => ipcRenderer.send('openDir', path)
})
