export interface ICreateCategoryRequestData {
  // 题目 文档分类ID
  categoryId?: number
  // 分类名称
  categoryName: string
  // 父节点ID
  parentId?: number,
  parentFlag?:boolean
}

export interface IUpdateCategoryRequestData {
  categoryId: string
  categoryName: string
  parentId: string
}

export interface IGetCategoryRequestData {
  /** 题目文档分类ID */
  categoryId: number
  /** 分类名称 */
  categoryName: string
  /** 父节点ID */
  parentId: string,
  parentFlag:boolean
}

export interface IGetCategoryData {
  /** 题目文档分类ID */
  categoryId: number
  /** 分类名称 */
  categoryName: string
  /** 父节点ID */
  children: any[]
  /** 等级 */
  level: number
  /**是否是父节点*/
  parentFlag: Boolean
  /**父节点id*/
  parentId: number
  label?: string
}

export type GetCategoryResponseData = IApiResponseData<{
  data: IGetCategoryData[]
  message: string
  code: number
}>
