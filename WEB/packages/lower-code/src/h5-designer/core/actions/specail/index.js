import DownloadAction from './downLoad/DownLoadAction'
import SkipAction from './skip/SkipAction'
import ShowHideAction from './showHide/ShowHideAction'
import SkipPageAction from './skipPage/SkipPageAction'
import CallNumberAction from './callNumber/CallNumberAction'
import WakeUpPopAction from './wakeUpPop/wakeUpPopAction'
import ShareEventAction from './shareEvent/ShareEventAction'

export default {
  skip: SkipAction,
  download: DownloadAction,
  showHide: ShowHideAction,
  skipPage: SkipPageAction,
  callNumber: CallNumberAction,
  wakeUpPop: WakeUpPopAction,
  shareEvent: ShareEventAction
}
