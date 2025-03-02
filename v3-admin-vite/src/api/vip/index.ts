import { request } from "@/utils/service"

/** vip详情 */
export function vipDetail(data:any) {
  return request({
    url: "/admin/api/project/vip-config/detail",
    method: "get",
    data
  })
}
/** 添加配置 */
export function addVipSetting(data:any) {
  return request({
    url: "/admin/api/project/vip-config/add",
    method: "post",
    data
  })
}
/** 分页查询vip配置*/
export function getVipList(data:any) {
  return request({
    url: "/admin/api/project/vip-config/list",
    method: "post",
    data
  })
}
/** 更新vip配置*/
export function modifyVip(data:any) {
  return request({
    url: "/admin/api/project/vip-config/modify",
    method: "post",
    data
  })
}
