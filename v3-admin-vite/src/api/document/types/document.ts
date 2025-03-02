export interface IDocumentData {
  docId: number
  tags: string[]
  type: string
  docName: string
  updateTime: string
  categoryId: number
  createTime: string
  docDownloadUrl: string
}
export interface IGetDocumentDetailData extends IDocumentData {}

export interface IUpdateDocumentRequestData extends IDocumentData {}

export interface IUpdateDocumentRequestData {
  id: string
  username: string
  password?: string
}

export interface IGetDocumentRequestData {
  /** 	类型ID */
  categoryId: number
  /** 文档名称 */
  docName?: string
  /** 当前页 */
  page: number
  /** 页大小 */
  size: number
}

export interface IGetDocumentData {
  categoryId: number
  createTime: string
  docDownloadUrl: string
  docId: number
  tags: string[]
  type: string
  updateTime: string
  docName?: string
}

export type GetDocumentResponseData = IApiResponseData<{
  records: IGetDocumentData[]
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
export type GetDocumentDetailData = IApiResponseData<{
  data: IDocumentData[]
  message: string
  code: number
}>
