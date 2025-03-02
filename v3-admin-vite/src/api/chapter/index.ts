import { request } from "@/utils/service"
import { method } from "lodash-es"
import type * as Chapters from "./types/chapters "

/** 添加章节 */
export function ICreateChaptersDataApi(data: Chapters.IChaptersData) {
  return request({
    url: "/admin/api/project/chapter/add",
    method: "post",
    data
  })
}

/**章节详情*/
export function IGetChaptersDetailData(id: string) {
  return request<Chapters.GetChaptersDetailData>({
    url: `/admin/api/project/chapter/detail/${id}`,
    method: "get"
  })
}
/** 查询分类下所有的章节以及章节下的试题集*/
export function IGetChaptersListData(data: Chapters.IChaptersParams) {
  console.log(data)
  return request<Chapters.GetChaptersDetailData>({
    url: `/admin/api/project/chapter/list/by-category-id?categoryId=${data.categoryId}`,
    method: "get",
    data
  })
}
/** 更新章节*/
export function IGetChapterUpdate(data: Chapters.IChaptersData) {
  return request({
    url: `/admin/api/project/chapter/modify`,
    method: "post",
    data
  })
}

/** 分页查找章节 */
export function IGetChaptersDataApi(data: Chapters.IChaptersData) {
  return request<Chapters.GetChaptersTypeList>({
    url: "/admin/api/project/chapter/page/list",
    method: "post",
    data
  })
}


/**根据分类获取所有科目*/
export function getSubjectList(data:any) {
  return request<any>({
    url: `/admin/api/project/subject/all-list?categoryId=${data.categoryId}`,
    method: "get",
  })
}
/** 添加科目*/
export function addSubject(data: any) {
  return request<any>({
    url: "/admin/api/project/subject/add",
    method: "post",
    data
  })
}
/** 修改科目*/
export function modifySubject(data: any) {
  return request<any>({
    url: "/admin/api/project/subject/modify",
    method: "post",
    data
  })
}
