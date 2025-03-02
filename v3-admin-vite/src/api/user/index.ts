import { request } from "@/utils/service"
import type * as User from "./types/user"

/** 用户列表 */
export function createCategoryDataApi(data: any) {
  return request({
    url: "/admin/api/project/admnin/list",
    method: "post",
    data
  })
}
/** 用户详情 */
export function updateCategoryDataApi() {
  return request({
    url: "/admin/api/project/admnin/detail",
    method: "get"
  })
}
export function userInfoExport(data:any) {
  return request({
    url: "/admin/api/project/admnin/export",
    method: "post",
    data,
    responseType: 'blob'
  })
}
