import { request } from "@/utils/service"
import { method } from "lodash-es"
import type * as Document from "./types/document"

/** 添加文档 */
export function createDocumentDataApi(data: any) {
  return request({
    url: "/admin/api/project/document/add",
    method: "post",
    data
  })
}

/** 删除文档(暂无接口) */
export function deleteDocumentDataApi(id: number) {
  return request({
    url: `Document/${id}`,
    method: "delete"
  })
}
export function uploadDocumentFile(data: any) {
  return request({
    url: `/admin/api/project/document/upload/doc`,
    method: "post",
    data,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  })
}
/**文档详情*/
export function IGetDocumentDetailData(id: string) {
  return request<Document.GetDocumentDetailData>({
    url: `/admin/api/project/document/detail${id}`,
    method: "get"
  })
}

/** 文档列表 */
export function getDocumentDataApi(params: Document.IGetDocumentRequestData) {
  return request<Document.GetDocumentDetailData>({
    url: "/admin/api/project/document/list",
    method: "post",
    params
  })
}

/** 更新文档 */
export function updateDocumentDataApi(data: Document.IGetDocumentData) {
  return request({
    url: "/admin/api/project/document/modify",
    method: "post",
    data
  })
}
