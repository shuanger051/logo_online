<template>
  <h-date-picker type="datetimerange" :placement="placement" :transfer="transfer" :disabled="disabled" :editable="false"
    :format="format" :options="options" :value="pickerDate" @on-change="changeDate" :placeholder="placeholder">
  </h-date-picker>
</template>

<script>
// 输入输出都为Int类型数组
import { dateTimeFormat, dateTimeStr2Int } from '@Utils/utils'
export default {
  name: 'DateTimePickerInt',
  props: {
    options: {
      type: Object,
      default: () => { }
    },
    placeholder: String,
    format: {
      type: String,
      default: 'yyyy-MM-dd HH:mm:ss'
    },
    // type: {
    //   type: String,
    //   default: 'datetimerange'
    // },
    start: {
      type: [String, Number],
      default: ''
    },
    end: {
      type: [String, Number],
      default: ''
    },
    date: {
      default: ['', ''] // [20190909000000, 20210909240000]
    },
    disabled: {
      type: Boolean,
      default: false
    },
    transfer: {
      type: Boolean,
      default: true
    },
    placement: {
      type: String,
      default: 'bottom-start'
    }
  },
  data() {
    return {
      pickerDate: ['', '']
    }
  },
  watch: {
    start(val) {
      this.setStartValue(val)
    },
    end(val) {
      this.setEndValue(val)
    },
    date(val) {
      if (!val) {
        this.$emit('update:start', '')
        this.$emit('update:end', '')
        this.pickerDate = ['', '']
      }
      if (Array.isArray(val) && val.length) {
        this.$emit('update:start', val[0])
        this.$emit('update:end', val[1])
        this.pickerDate = [dateTimeFormat(val[0], '-'), dateTimeFormat(val[1], '-')]
      }
    }
  },
  created() {
    this.pickerDate = [dateTimeFormat(this.date[0], '-'), dateTimeFormat(this.date[1], '-')]
  },
  methods: {
    setStartValue(val) {
      if (!val) return
      this.pickerDate = [dateTimeFormat(val, '-'), dateTimeFormat(this.end, '-')]
    },
    setEndValue(val) {
      if (!val) return
      this.pickerDate = [dateTimeFormat(this.start, '-'), dateTimeFormat(val, '-')]
    },
    changeDate(val = []) {
      if (val.length) {
        // console.log(val)
        const dates = {
          start_date: dateTimeStr2Int(val[0]) || '',
          end_date: dateTimeStr2Int(val[1]) || ''
        }
        this.$emit('update:start', dates.start_date)
        this.$emit('update:end', dates.end_date)

        this.$emit('update:date', Object.values(dates))
        this.$emit('onChange', val)
        this.pickerDate = [val[0], val[1]]
      } else {
        this.$emit('update:start', '')
        this.$emit('update:end', '')

        this.$emit('update:date', Object.values(''))
        this.$emit('onChange', [])
        this.pickerDate = ['', '']
      }
    }
  }
}
</script>
