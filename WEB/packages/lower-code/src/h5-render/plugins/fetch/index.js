import {
  addFetchInterceptors,
  createFetch
} from './api'

function createUcpFetch (config = {}) {
  let fetch = createFetch(config)
  addFetchInterceptors(fetch, config)
  return fetch
}

export default {
  createUcpFetch,
  createFetch,
  fetch: createUcpFetch(),
  addFetchInterceptors
}
