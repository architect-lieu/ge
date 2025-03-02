import { request } from "@/utils/service"

/** 考试信息详情 */
export function modifyExamInfoDetail(data:number) {
  return request({
    url: `/admin/api/project/exam-info/detail?id=${data}`,
    method: "get",
  })
}
/** 添加考试信息 */
export function addExamInfo(data:any) {
  return request({
    url: "/admin/api/project/exam-info/add",
    method: "post",
    data
  })
}
/** 获取科目下的考试信息*/
export function getCategoryInfo(id:number) {
  return request({
    url: `/admin/api/project/exam-info/detail-by-subject?catgoryId=${id}`,
    method: "get",

  })
}
/** 分页考试信息列表*/
export function getExamInfoList(data:any) {
  return request({
    url: `/admin/api/project/exam-info/list`,
    method: "post",

  })
}
/** 修改考试信息*/
export function modifyExamInfo(data:any) {
  return request({
    url: "/admin/api/project/exam-info/modify",
    method: "post",
    data
  })
}
