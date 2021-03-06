
const days = [
  'Mon',
  'Tue',
  'Wed',
  'Thu',
  'Fri',
  'Sat',
  'Sun'
]

const months = [
  'Jan',
  'Feb',
  'Mar',
  'Apr',
  'May',
  'Jun',
  'Jul',
  'Aug',
  'Sep',
  'Oct',
  'Nov',
  'Dec'
]

export class Format {
  /**
   * Format a date.
   * @param {string} date The date to format in string format. YYYY-MM-DD
   * @param {string} format Optional format for the date input. Use: day, mnd, DD, MM or YYYY - Max 4 digits.
   */
  static date (date, format = 'DD/MM YYYY') {
    if (!date.length) return ''

    if (typeof date === 'string') {
      let d = new Date(date.replace(/-/g, '/').replace(/T/g, ' ').slice(0, 19))
      let formatted = format
        .replace(new RegExp('da[gy]', 'ig'), days[d.getDay()])
        .replace(new RegExp('mnd', 'ig'), months[d.getMonth()])

      for (let i = 4; i > 0; i--) {
        formatted = formatted
          .replace(new RegExp(`(^|\\W)Y{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getFullYear(), i)}$2`)
          .replace(new RegExp(`(^|\\W)M{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getMonth() + 1, i)}$2`)
          .replace(new RegExp(`(^|\\W)D{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getDate(), i)}$2`)
      }

      return formatted
    }

    return ''
  }

  /**
   * Format time.
   * @param {string} time The time to format in string format. hh:mm:ss
   * @param {string} format Optional format for the date input. Use: HH, mm or SS - Max 4 digits.
   */
  static time (time, format = 'HH:mm') {
    if (!time.length) return ''

    if (typeof time === 'string') {
      let d = time.slice(0, 8).split(':')
      let formatted = format

      for (let i = 4; i > 0; i--) {
        formatted = formatted
          .replace(new RegExp(`(^|\\W)H{${i},}($|\\W)`, 'g'), `$1${this.digits(d[0], i)}$2`)
          .replace(new RegExp(`(^|\\W)m{${i},}($|\\W)`, 'g'), `$1${this.digits(d[1], i)}$2`)
          .replace(new RegExp(`(^|\\W)S{${i},}($|\\W)`, 'g'), `$1${this.digits(d[2], i)}$2`)
      }

      return formatted
    }

    return ''
  }

  /**
   * Format datetime.
   * @param {string} datetime The datetime to format in string format. YYYY-MM-DD hh:mm:ss
   * @param {string} format Optional format for the datetime input. Use: day, mnd, DD, MM, YYYY, HH, mm or SS
   */
  static datetime (datetime, format = 'DD/MM YYYY (HH:mm)') {
    if (!datetime.length) return ''

    if (typeof datetime === 'string') {
      let d = new Date(datetime.replace(/-/g, '/').replace(/T/g, ' ').slice(0, 19))
      let formatted = format
        .replace(new RegExp('da[gy]', 'ig'), days[d.getDay()])
        .replace(new RegExp('mnd', 'ig'), months[d.getMonth()])

      for (let i = 4; i > 0; i--) {
        formatted = formatted
          .replace(new RegExp(`(^|\\W)Y{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getFullYear(), i)}$2`)
          .replace(new RegExp(`(^|\\W)M{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getMonth() + 1, i)}$2`)
          .replace(new RegExp(`(^|\\W)D{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getDate(), i)}$2`)
          .replace(new RegExp(`(^|\\W)H{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getHours(), i)}$2`)
          .replace(new RegExp(`(^|\\W)m{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getMinutes(), i)}$2`)
          .replace(new RegExp(`(^|\\W)S{${i},}($|\\W)`, 'g'), `$1${this.digits(d.getSeconds(), i)}$2`)
      }

      return formatted
    }

    return ''
  }

  /**
   * Forcing amount of digits by prepending zeroes in front of digits.
   * @param {number} value The digit to prepend to.
   * @param {number} places Minimun amount of digits.
   */
  static digits (value, places = 2, revert = false) {
    if (revert) return (value + Array(places).join('0')).slice(0, places)
    return (Array(places).join('0') + value).slice(-places)
  }
}
