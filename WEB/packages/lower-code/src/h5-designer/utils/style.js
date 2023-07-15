export const resolveEditShapeStyle = (style) => {
  return {
    'width': style.width + 'px',
    'height': style.height + 'px',
    'top': style.top !== 'auto' ? style.top + 'px' : 'auto',
    'left': style.left + 'px',
    'bottom': style.bottom !== 'auto' ? style.bottom + 'px' : 'auto'
  }
}

export const resolvePreviewShapeStyle = (style) => {
  // let top = 667 - style.height
  if (style.top !== 'auto') {
    return {
      'width': style.width + 'px',
      'height': style.height + 'px',
      'top': style.top + 'px',
      'left': style.left + 'px',
      'position': 'absolute',
      // 'background-color': style['background-color'] ? style['background-color'] : 'transparent',
      'z-index': style['z-index']
      // 'background': `url(${style['background-image']}) ${style['background-color']} no-repeat center center`
    }
  } else {
    return {
      'width': style.width + 'px',
      'height': style.height + 'px',
      'bottom': '0',
      'left': style.left + 'px',
      'position': 'fixed',
      // 'background-color': style['background-color'] ? style['background-color'] : 'transparent',
      'z-index': style['z-index']
      // 'background': `url(${style['background-image']}) ${style['background-color']} no-repeat center center`
    }
  }
}

export const resolveEditElementStyle = (style) => {
  return {
    'width': '100%',
    'height': '100%',
    'border-color': style['border-color'] ? style['border-color'] : '#fff',
    'border-width': style['border-width'] + 'px',
    'border-top-style': style['border-top-style'],
    'border-bottom-style': style['border-bottom-style'],
    'border-left-style': style['border-left-style'],
    'border-right-style': style['border-right-style'],
    'border-radius': style['border-radius'] + 'px',
    'background-color': style['background-color'] ? style['background-color'] : 'transparent',
    'overflow': style['overflow'] || 'hidden'
  }
}

export const resolvePreviewElementStyle = (style) => {
  return {
    'position': 'static',
    'width': '100%',
    'height': '100%',
    'border-color': style['border-color'],
    'border-width': style['border-width'] + 'px',
    'border-top-style': style['border-top-style'],
    'border-bottom-style': style['border-bottom-style'],
    'border-left-style': style['border-left-style'],
    'border-right-style': style['border-right-style'],
    'border-radius': style['border-radius'] + 'px',
    'background-color': style['background-color'] ? style['background-color'] : 'transparent',
    'overflow': style['overflow'] || 'hidden'
    // 'background': `url(${style['background-image']}) ${style['background-color']} no-repeat center center`
  }
}
