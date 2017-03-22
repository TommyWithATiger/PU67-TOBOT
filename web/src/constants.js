export const DOMAIN_URL = process.env.NODE_ENV === 'development' ? 'http://localhost:5032' : window.location.origin
export const API_URL = `${DOMAIN_URL}/api/v1`
