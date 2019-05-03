export function formatDateFromTimestampMilli(value) {
  return new Date(parseInt(value)).toLocaleString()
}

export function formatDateFromISO8601(value) {
  return new Date(value).toLocaleString()
}
