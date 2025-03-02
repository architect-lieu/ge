export interface IGetUserDetailData {
  // 第一次开通vip时间
  fistVipPayTime: string
  // 头像
  headPicture: string
  // 积分
  integral: number
  // 最后登录时间
  lastLoginTime: string
  // 最后一次续费时间
  lastVipPayTime: string
  // 	用于小程序新用户标识
  minAppNewUserFlag: boolean
  // 手机号
  mobilephone: string
  // 昵称
  nickName: string
  // openid
  openid: string
  // sessionKey
  sessionKey: string
  // 用户来源
  source: string
  //  token
  token: string
  // userId
  userId: number
  // vipFlag
  vipFlag: string
}
export interface IGetUseryData {
  /** 手机号 */
  mobilephone: number
  /** 昵称 */
  nickName: string
  /** 当前页 */
  page: number
  /** 页大小 */
  size: number
  /**用户来源: MIN_APP APP*/
  source: string
  /**会员标识*/
  vipFlag: Boolean
}
export interface IGetUserListData {
  // 页码
  page: number
  // 数据数量
  size: number
}
export type GetUseryResponseData = IApiResponseData<{
  data: IGetUseryData[]
  message: string
  code: number
}>
