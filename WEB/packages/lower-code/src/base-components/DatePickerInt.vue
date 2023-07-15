<template>
  <h-date-picker :type="type" :placement="placement" :transfer="transfer" :disabled="disabled" :editable="false"
    :format="format" :options="options" :value="pickerDate" @on-change="changeDate" :placeholder="placeholder">
  </h-date-picker>
</template>

<script>
// 输入输出都为Int类型数组
import { dateFormat } from '@Utils/utils'
export default {
  name: 'DatePickerInt',
  props: {
    options: {
      type: Object,
      default: () => { }
    },
    placeholder: String,
    format: {
      type: String,
      default: 'yyyy/MM/dd'
    },
    type: {
      type: String,
      default: 'daterange'
    },
    start: {
      type: [String, Number],
      default: ''
    },
    end: {
      type: [String, Number],
      default: ''
    },
    date: {
      default: ['', ''] // [20190909, 91021111]
    },
    disabled: {
      type: Boolean,
      default: false
    },
    transfer: {
      type: Boolean,
      default: false
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
        this.pickerDate = [dateFormat(val[0], '', '/'), dateFormat(val[1], '', '/')]
      }
    }
  },
  created() {
    this.pickerDate = [dateFormat(this.date[0], '', '/'), dateFormat(this.date[1], '', '/')]
  },
  methods: {
    setStartValue(val) {
      if (!val) return
      this.pickerDate = [dateFormat(val, '', '/'), dateFormat(this.end, '', '/')]
    },
    setEndValue(val) {
      if (!val) return
      this.pickerDate = [dateFormat(this.start, '', '/'), dateFormat(val, '', '/')]
    },
    changeDate(val = []) {
      if (val.length) {
        const dates = {
          start_date: +val[0].replace(/\//g, '') || '',
          end_date: +val[1].replace(/\//g, '') || ''
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
