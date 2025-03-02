import { request } from "@/utils/service"

/** vip详情 */
export function codeDetail(data:number) {
  return request({
    url: "/admin/api/project/organization/detail",
    method: "get",
    data
  })
}
/** 添加配置 */
export function addCode(data:any) {
  return request({
    url: "/admin/api/project/organization/add",
    method: "post",
    data
  })
}
/** 分页查询vip配置*/
export function getCodeList(data:any) {
  return request({
    url: "/admin/api/project/organization/list",
    method: "post",
    data
  })
}
/** 更新vip配置*/
export function modifyCode(data:any) {
  return request({
    url: "/admin/api/project/organization/modify",
    method: "post",
    data
  })
}
