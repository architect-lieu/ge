import {
	baseUrl,
	proUrl
} from './config.js'
import api from './api.js'
import wechat from '@/utils/wechat.js'
import * as path from 'path'
// var constant  = require('./constant.js')

function request(name, opt = {}) {

	console.log(name, opt, '获取请求的参数')
	let {
		params,
		contentType,
		isBody = 'true',
		query = "",
		callBack
	} = opt;
	let head = []
	let token = uni.getStorageSync('token')
	// if (api[name]['isToken'] && !token) {
	// 	uni.showModal({
	// 		title: '提示',
	// 		content: '请登录后再进行操作',
	// 		confirmText: '去登陆',
	// 		success: (res) => {
	// 			console.log(res);
	// 			if (res.confirm) {
	// 				uni.redirectTo({
	// 					url: '../login/login',

	// 				})
	// 			}
	// 		}
	// 	})
	// 	return
	// }
	console.log('process.env.NODE_ENV', process.env.NODE_ENV, process.env.NODE_ENV === 'development')
	let url = process.env.NODE_ENV == 'development' ? baseUrl + api[name]['url'] + query : proUrl + api[name]['url'] +
		query
	head = {
		'Ac-Token': uni.getStorageSync('token') || '',
		'Content-Type': api[name]['contentType'] ? api[name]['contentType'] : 'application/json',
		'Accept': 'application/json'
	}
	uni.request({
		url: url,
		// 接口请求地址
		data: opt.params,
		header: head,
		method: api[name].method == undefined ? 'POST' : api[name].method,
		dataType: 'json',
		responseType: opt.responseType === undefined ? 'text' : opt.responseType,
		success: function(res) {
			const {
				data: {
					code,
					data,
					message
				}
			} = res
			// if (code === 1405) {
			// 	console.log('登陆过期');
			// 	wechat.wxLogin()
			// 	return
      // }
      console.log('res====',res)
      if (code !== 1200) {
       return  uni.showToast({
          title: message,
          icon: 'none'
        })
      }
      if (code === 1405) {
        uni.clearStorage()
				uni.clearStorageSync()
				return uni.showModal({
					title: '提示',
					content: '登陆过期，请重新登陆',
					confirmText: '确定',
				})
			}
			if (code === 1500) {

				uni.hideLoading();
				return uni.showModal({
					title: '提示',
					content: '请登录后再进行操作',
					confirmText: '确定',
					success: (res) => {
						uni.clearStorage()
						uni.clearStorageSync()
					}
				})


			}
      data.code = code
      callBack(data)
			// var responseData = res.data
			// if (responseData.code === '00000') {
			//   if (params.callBack) {
			//     params.callBack(responseData.data)
			//   }
			//   return
			// }

		},
		fail: function(res) {
			uni.hideLoading()
			setTimeout(() => {
				uni.showToast({
					title: '服务器打了个盹~',
					icon: 'none'
				})
			}, 1)
		}
	})
}

function mpAuthLogin(page, needCode) {
	// 在微信环境打开,请求公众号网页登陆
	var redirectUrl = null

	if (!page) {
		redirectUrl = window.location.href
	} else {
		var {
			protocol,
			host
		} = window.location
		redirectUrl = `${protocol}//${host}` + page
	}
	var scope = 'snsapi_userinfo'
	window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + config.mpAppId +
		'&redirect_uri=' +
		encodeURIComponent(redirectUrl) + '&response_type=code&scope=' + scope + '&state=' + (needCode ? 'needCode' :
			'unNeedCode') +
		'#wechat_redirect'
}

/**
 * 上传图片
 */
function upload(params) {
	uni.uploadFile({
		url: config.baseUrl + params.url,
		filePath: params.filePath,
		name: params.name,
		header: {
			'Authorization': params.login ? undefined : uni.getStorageSync('token')
		},
		dataType: 'json',
		responseType: params.responseType === undefined ? 'json' : params.responseType,
		success: (res) => {
			// 如果有定义了params.callBack，则调用 params.callBack(res.data)
			if (params.callBack) {
				params.callBack(res.data)
			}
		},
		fail: (err) => {
			console.log(err)
			uni.hideLoading()
		}
	})
}

function saveLog() {}
export {
	request,
	mpAuthLogin,
	upload
}