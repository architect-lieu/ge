/**试题集列表请求参数*/
export interface IPaperParams {
  chapterId: number | string
  paperName?: string
  page?: number
  size?: number
}

/** 修改试题集*/
export interface IPaperModifyParams {
  chapterId: number
  paperId: number
  paperName: string
  questionNum?: number
}
export interface IPaperrrayList extends IPaperParams {
  paperId?: number | string
  questionNum?: number
  createTime?: string
  updateTime?: string
}
/** 试题集响应参数*/
export interface IpaperResponseParams {
  records: IPaperrrayList[]
  searchCount: Boolean
  countId: string
  current: number
  maxLimit: number
  optimizeCountSql: Boolean
  orders: any[]
  pages: number
  size: number
  total: number
}
export type GetPaperListResponse = IApiResponseData<{
  [x: string]: any
  data: IpaperResponseParams[]
  message: string
  code: number
}>
