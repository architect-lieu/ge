import { request } from "@/utils/service"

/** 消息详情 */
export function getMessageDetail(data:number) {
  return request({
    url: "/admin/api/project/message/detail",
    method: "get",
    data
  })
}
/** 添加消息推送 */
export function addMessage(data:any) {
  return request({
    url: "/admin/api/project/message/add",
    method: "post",
    data
  })
}
/** 分页查询消息列表*/
export function getMessageList(data:any) {
  return request({
    url: `/admin/api/project/message/list?page=${data.page}&size=${data.size}`,
    method: "get",

  })
}
/** 修改消息*/
export function modifyMessage(data:any) {
  return request({
    url: "/admin/api/project/message/modify",
    method: "post",
    data
  })
}
