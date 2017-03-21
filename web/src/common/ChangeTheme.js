import { Format } from 'common/Format'

export class ChangeTheme {
  static change (dark = false, hue = -1) {
    if (window.CSS.supports('--a', 0)) {
      // let styles = window.getComputedStyle(document.body)

      try {
        dark = localStorage.getItem('theme') === 'dark'
        if (hue < 0) {
          hue = localStorage.getItem('hue') || hue
          localStorage.setItem('hue', hue)
        }
      } catch (err) {}

      for (let i = hue, n = 1; i >= hue / 2; i -= hue / 16, n++) {
        document.body.style.setProperty('--p-color-' + n, '#' + Format.digits(Math.floor(i).toString(16), 6))
      }

      let max = 256 * 256 * 256 - 1
      for (let i = max, n = 1; i >= 0x888888; i -= 0x111111, n++) {
        document.body.style.setProperty(`--n${dark ? 'n' : ''}-color-${n}`, '#' + Format.digits(i.toString(16), 6))
      }

      for (let i = 0x333333, n = 1; i <= 0x777777; i += 0x111111, n++) {
        document.body.style.setProperty(`--n${dark ? '' : 'n'}-color-${n}`, '#' + Format.digits(i.toString(16), 6))
      }
    } else {
      console.log('Browser does not support CSS variables.')
    }
  }
}
