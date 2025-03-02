import { request } from "@/utils/service"
import { getToken,removeToken } from "@/utils/cache/cookies"
import type * as Category from "./types/category"

/** 添加节点 */
export function createCategoryDataApi(data: Category.ICreateCategoryRequestData) {
  return request({
    url: "/admin/api/project/category/add",
    method: "post",
    data
  })
}

/** 删除节点 */
export function deleteCategoryDataApi(data: any) {
  return request({
    url: `/admin/api/project/category/del`,
    method: "post",
    data
  })
}

/** 更新分类 */
export function updateCategoryDataApi(data: Category.ICreateCategoryRequestData) {
  return request({
    url: "/admin/api/project/category/modify",
    method: "post",
    data
  })
}

/** 分类详情 */
export function getCategoryDataApi(params: Category.IGetCategoryRequestData) {
  return request<Category.GetCategoryResponseData>({
    url: "/admin/api/project/category/detail",
    method: "get",
    params
  })
}

/** 分类详情连同其下子节点 */
export function getCategoryWithChildernDataApi() {
  return request<Category.GetCategoryResponseData>({
    url: "/admin/api/project/category/detail/with/children",
    method: "get"
  })
}
/** 获取分类树*/
export function getCategoryTreeDataApi() {
  return request({
    url: "/admin/api/project/category/tree",
    method: "get"
  })
}
/** 获取分类树下面的所有科目*/
export function getSubjectDataApi(id: any) {
  console.log(id);
  return request({
    url: `/admin/api/project/category/children-list?pid=${id}`,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      "Ac-Token": getToken(),
    },
    method: "get"
  })
}
/** 根据分类添加科目*/
export function addSubject(data:any) {
  return request({
    url: `/admin/api/project/subject/add?categoryId=${data.categoryId}`,
    method: "get"
  })
}

