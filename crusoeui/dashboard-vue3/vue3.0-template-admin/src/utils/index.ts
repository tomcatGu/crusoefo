import { generateColors } from './color'
import { writeNewStyle } from './writeNewStyle'
import { getStyleTemplate } from './getStyleTemplate'

export { generateColors, writeNewStyle, getStyleTemplate }

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time: string | number | Date, cFormat: string) {
    if (arguments.length === 0) {
      return null
    }
    const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
    let date
    if (typeof time === 'object') {
      date = time
    } else {
      if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
        time = parseInt(time)
      }
      if ((typeof time === 'number') && (time.toString().length === 10)) {
        time = time * 1000
      }
      date = new Date(time)
    }
    const formatObj = {
      y: date.getFullYear(),
      m: date.getMonth() + 1,
      d: date.getDate(),
      h: date.getHours(),
      i: date.getMinutes(),
      s: date.getSeconds(),
      a: date.getDay()
    }
    type keyType = keyof typeof formatObj
    const time_str = format.replace(/{([ymdhisa])+}/g, (result, key:keyType) => {
      const value = formatObj[key]      // Note: getDay() returns 0 on Sunday
      if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value ] }
      return value.toString().padStart(2, '0')
    })
    return time_str
  }
  
  /**
   * @param {number} time
   * @param {string} option
   * @returns {string}
   */
  export function formatTime(time: any, option: string) {
    if (('' + time).length === 10) {
      time = parseInt(time) * 1000
    } else {
      time = +time
    }
    const d = new Date(time)
    const now = Date.now()
  
    const diff = (now - d) / 1000
  
    if (diff < 30) {
      return '刚刚'
    } else if (diff < 3600) {
      // less 1 hour
      return Math.ceil(diff / 60) + '分钟前'
    } else if (diff < 3600 * 24) {
      return Math.ceil(diff / 3600) + '小时前'
    } else if (diff < 3600 * 24 * 2) {
      return '1天前'
    }
    if (option) {
      return parseTime(time, option)
    } else {
      return (
        d.getMonth() +
        1 +
        '月' +
        d.getDate() +
        '日' +
        d.getHours() +
        '时' +
        d.getMinutes() +
        '分'
      )
    }
  }
  
  /**
   * @param {string} url
   * @returns {Object}
   */
  export function param2Obj(url: string) {
    const search = url.split('?')[1]
    if (!search) {
      return {}
    }
    return JSON.parse(
      '{"' +
        decodeURIComponent(search)
          .replace(/"/g, '\\"')
          .replace(/&/g, '","')
          .replace(/=/g, '":"')
          .replace(/\+/g, ' ') +
        '"}'
    )
  }
  /**
   * like eval()
   * @param {string} fn
   * @returns {Object}
   */
  export function evil(fn: string) {
    // 一个变量指向Function，防止有些前端编译工具报错
    const Fn = Function
    return new Fn('return ' + fn)()
  }
  /**
   *
   * @param {Object} _obj
   */
  export function obj2String(_obj: any):any {
    var t = typeof (_obj)
    if (t !== 'object' || _obj === null) {
      // simple data type
      if (t === 'string') {
        _obj = '"' + _obj + '"'
      }
      return String(_obj)
    } else {
      if (_obj instanceof Date) {
        return _obj.toLocaleString()
      }
      // recurse array or object
      var n; var v; var json = []; var arr = (_obj && _obj.constructor === Array)
      for (n in _obj) {
        v = _obj[n]
        t = typeof (v)
        if (t === 'string') {
          v = '"' + v + '"'
        } else if (t === 'object' && v !== null) {
          v = obj2String(v)
        }
        json.push((arr ? '' : '"' + n + '":') + String(v))
      }
      return (arr ? '[' : '{') + String(json) + (arr ? ']' : '}')
    }
  }
  