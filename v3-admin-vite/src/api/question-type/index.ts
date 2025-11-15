import { request } from "@/utils/service"
import type { questionType } from "@/api/question/types/question"

/** 获取题型列表（支持关键字） */
export function getQuestionTypeListApi(params?: { keyword?: string }) {
  return request<IApiResponseData<questionType[]>>({
    url: "/admin/api/project/question-type/all/list",
    method: "get",
    params
  })
}

/** 新增题型 */
export function addQuestionTypeApi(data: Pick<questionType, "questionTypeName" | "questionTypeCode">) {
  return request<IApiResponseData<boolean>>({
    url: "/admin/api/project/question-type/add",
    method: "post",
    data
  })
}

/** 修改题型 */
export function modifyQuestionTypeApi(
  data: Pick<questionType, "questionTypeId" | "questionTypeName" | "questionTypeCode">
) {
  return request<IApiResponseData<boolean>>({
    url: "/admin/api/project/question-type/modify",
    method: "post",
    data
  })
}



