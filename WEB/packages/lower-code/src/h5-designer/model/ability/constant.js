export const direction = {
  forbidden: {
    t: 0, l: 0, r: 0, b: 0
  },
  horizontal: {
    t: 0, b: 0, l: 1, r: 1
  },
  vertical: {
    l: 0, r: 0, t: 1, b: 1
  },
  left: {
    r: 0, t: 0, b: 0, l: 1
  },
  right: {
    l: 0, t: 0, b: 0, r: 1
  },
  top: {
    b: 0, l: 0, r: 0, t: 1
  },
  bottom: {
    t: 0, l: 0, r: 0, b: 1
  },
  all: {
    t: 1, l: 1, r: 1, b: 1
  }
}

export const positionPoint = {
  lt: direction.all,
  rt: direction.all,
  lb: direction.all,
  rb: direction.all,
  l: direction.horizontal,
  r: direction.horizontal,
  t: direction.vertical,
  b: direction.vertical
}

const getPositionPoint = () => {
  return Object.keys(positionPoint).map((key) => {
    return {
      key,
      direction: {
        ...positionPoint[key]
      }
    }
  })
}

export const positionPointArray = getPositionPoint()

export const getPositionPointByKey = (key) => {

}
