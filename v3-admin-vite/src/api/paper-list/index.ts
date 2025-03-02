import { request } from "@/utils/service"
import type * as Paper from "./types/paper-list"
import { getToken } from "@/utils/cache/cookies"

/** 试题集列表 Token */
export function getPaperListApi(data: Paper.IPaperParams) {
  return request<Paper.GetPaperListResponse>({
    url: "/admin/api/project/paper/list",
    method: "post",
    data
  })
}

/** 更新试题集 */
export function updatePaperApi(data: Paper.IPaperrrayList) {
  return request({
    url: "/admin/api/project/paper/modify",
    method: "post",
    data
  })
}
/** 添加试题集*/
export function addPaperApi(data: Paper.IPaperrrayList) {
  return request({
    url: "/admin/api/project/paper/add",
    method: "post",
    data
  })
}
/**导入题目*/
export function importFileQuestion(data: any) {
  return request({
    url: `/admin/api/project/question/import-question-excel?categoryPid=${data.categoryPid}&subjectId=${data.subjectId}&examId=${data.examId[0]}`,
    method: "post",
    headers: {
      'Content-Type': 'multipart/form-data',
      "Ac-Token": getToken(),
    },
    data: {
      file:data.file[0]['raw']
    }
  })
}
