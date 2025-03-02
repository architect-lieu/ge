export interface IChaptersData {
  /**	所属分类*/
  categoryId: number
  /** 章节ID*/
  chapterId?: number
  /** 是否是真题章节*/
  trueQuestionChapterFlag?: any
  /** 当前页 */
  page?: number
  /** 页大小 */
  size?: number
  /** 	章节名称*/
  chapterName?: string
  createTime?: string
  updateTime?: string
}

export interface IChaptersParams {
  categoryId: number
  trueQuestionChapterFlag?: Boolean
}
export type GetChaptersListData = IApiResponseData<{
  records: IChaptersData[]
  searchCount: Boolean
  countId: string
  current: number
  maxLimit: number
  optimizeCountSql: Boolean
  orders: any[]
  pages: number
  size: number
  total: number
}>
/** 查询分类下所有的章节以及章节下的试题集*/
export type GetChaptersDetailData = IApiResponseData<{
  data: IChaptersData[]
  message: string
  code: number
}>

/** 分页查找章节*/
export type GetChaptersTypeList = IApiResponseData<{
  data: GetChaptersListData[]
  message: string
  code: number
}>
