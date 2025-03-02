/** 问题分页列表请求参数*/
export interface questionPageParams {
  page: number
  size: number
  paperId: number | string
  categoryId: number | string
  difficulty: string
  questionTitle: string
  questionTypeCode: string
  questionTypeName: string
}
export interface updateQuestionParams {
  analysis: string
  categoryId?: any
  categoryPid?:any,
  subjectCategoryId?:string|number,
  collectFlag: boolean
  difficulty: string
  options: string[] | string
  paperIds: number | string | string[]
  questionId: number | string
  questionTitle?: any
  questionTypeCode: string
  questionTypeName: string
  rightAnswer: string
  rightOptions: string[] | string
  createTime?: string
  updateTime?: string
  questionList?: any
}
export interface questionListParams {
  analysis: string
  categoryId: number | string
  collectFlag: boolean
  difficulty: string
  options: string[]
  paperList?: any[]
  paperIds: number | string
  questionId: number | string
  questionTitle: string
  questionTypeCode?: string
  questionTypeName: string
  rightAnswer: string
  rightOptions: string[]
  updateTime?: string
  createTime?: string
}
export interface questionType {
  questionNum: number | string
  questionTypeCode: string
  questionTypeId: number | string
  questionTypeName: string
  createTime?: string
  updateTime?: string
}

export type GetQuestionListResponse = IApiResponseData<{
  data: questionListParams[] | string | any[]
  message: string
  code: number
}>
export type GetQuestionTypeListResponse = IApiResponseData<{
  map: any
  data: any
  message: string
  code: number
}>
