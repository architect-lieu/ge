/** 所有 api 接口的响应数据都应该准守该格式 */
interface IApiResponseData<T> {
  code: number
  data: T
  message: string
}

interface IApiResponseDataArray<T>{
  records: any[]
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