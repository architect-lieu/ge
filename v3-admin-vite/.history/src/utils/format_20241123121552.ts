import dayjs from "dayjs"

export const formatDateTime = (time: string | number | Date | undefined): string => {
  if (!time) return "-"
  return dayjs(time).format("YYYY-MM-DD HH:mm:ss")
}
