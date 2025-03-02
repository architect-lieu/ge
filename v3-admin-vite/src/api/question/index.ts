import { request } from "@/utils/service"
import type * as Question from "./types/question"

/** 试题集列表 Token */
export function getQuestionListApi(data: Question.questionPageParams) {
  return request<Question.questionListParams>({
    url: "/admin/api/project/question/list",
    method: "post",
    data
  })
}

/** 更新试题集 */
export function updateQuestionApi(data: Question.updateQuestionParams) {
  return request({
    url: "/admin/api/project/question/modify",
    method: "post",
    data
  })
}
/** 添加试题集*/
export function createQuestionApi(data: Question.updateQuestionParams) {
  return request({
    url: "/admin/api/project/question/add",
    method: "post",
    data
  })
}
/** 添加试题类型*/
export function createQuestionTypeApi(data: Question.questionType) {
  return request<Question.GetQuestionListResponse>({
    url: "/admin/api/project/question-type/add",
    method: "post",
    data
  })
}
/** 获取试题类型*/
export function getQuestionTypeApi() {
  return request<Question.GetQuestionTypeListResponse>({
    url: "/admin/api/project/question-type/all/list",
    method: "get"
  })
}
/** 导出试题*/
export function getQuestionExportList() {
  return request<Question.GetQuestionTypeListResponse>({
    url: "/admin/api/project/question/export-question-excel",
    method: "post",
    responseType: 'blob'
  })
}
/** 导入试题*/
export function importQuestionList() {
  return request<Question.GetQuestionTypeListResponse>({
    url: "/admin/api/project/question/import-question-excel",
    method: "post"
  })
}
//
/** 导出模板*/
export function exportDemo() {
  return request<Question.GetQuestionTypeListResponse>({
    url: "/admin/api/project/question/export-question-template",
    method: "get",
    responseType: 'blob'
  })
}


