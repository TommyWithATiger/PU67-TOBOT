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

      let r = Math.floor(hue / 256 / 256)
      let g = Math.floor((hue - r * 256 * 256) / 256)
      let b = hue - g * 256

      for (let i = 256, n = 1; i >= 256 / 2; i -= 256 / 16, n++) {
        let rf = Format.digits(Math.floor(r).toString(16), 2)
        let gf = Format.digits(Math.floor(g).toString(16), 2)
        let bf = Format.digits(Math.floor(b).toString(16), 2)
        document.body.style.setProperty('--p-color-' + n, '#' + rf + gf + bf)
        r *= 15 / 16
        g *= 15 / 16
        b *= 15 / 16
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
