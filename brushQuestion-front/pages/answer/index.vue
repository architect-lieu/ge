<template>
	<!-- <page-meta :root-font-size="getRootFontSize()"></page-meta> -->
	<page-meta :page-style="'overflow:'+(show?'hidden':'visible')"></page-meta>
	<uni-nav-bar :style="{'height':navigationBarAndStatusBarHeight,display:'flex',width:'100%'}" :status-bar="true"
		:title="''">
		<block v-slot:left>
			<uni-icons @click="back" type="back" color="#000" size="24" />
		</block>
		<view class="time-box" v-if="isExam">
			<uni-countdown :show-day="false" :hour="0" :minute="Number(examSetting.examTime)" :second="0"
				@timeup="examEnd('endTime')" />
		</view>
		<view class="input-view" v-else>
			<button class="type-button mini-btn" @click="switchQuestion(0)" :class="{'active-btn':!questionType}">答题</button>
			<button class="type-button mini-btn" @click="switchQuestion(1)" :class="{'active-btn':questionType}">背题</button>
		</view>
	</uni-nav-bar>
	<!-- <AD></AD> -->
	<view class="container" page-font-size="{{pageFontSize}}" root-font-size="{{rootFontSize}}">
		<swiper :duration="durationTime" disable-programmatic-animation="true" skip-hidden-item-layout="true" class="swiper"
			:current="current" @change="swiperChange">
			<swiper-item v-for="(question, index) in list" :key="index">
				<view>
					<view class="question" style="padding: 0 20rpx;">
						<view class="question-title">
							<text class="option-tag">
								{{question.difficulty}}
							</text>
							{{current+1}}、
							{{ question.questionTitle }}
						</view>
						<!-- 单选题逻辑 -->
						<view class="question-options" v-if="question.questionTypeName === '单选题' && questionType ==0">
							<view class="question-option" v-for="(option, optionIndex) in question.optionsList" :key="optionIndex"
								@tap="selectOption(option,optionIndex,question)">
								<text class="option"
									:class="{ 'option-right': question.isSelected === true && option.isRight,'option-error':option.isChecked === true && option.isChecked !== option.isRight }">
									<!-- 如果已经选中 则判断是否选中正确答案 -->
									<text v-if="!(question.isSelected) || !option.isChecked ">{{optionTag[optionIndex]}}</text>
									<!-- <text else>{{optionTag[optionIndex]}}</text> -->
								</text>
								<text class="option-item"
									:class="{ 'option-right-text': question.optionsList.isSelected === true && option.isRight,'option-error-text':option.isChecked === true && option.isChecked !== option.isRight }">{{option['label']}}
								</text>
							</view>
						</view>
						<!-- 多选题逻辑 -->
						<view v-if="question.questionTypeName === '多选题' && questionType ==0">
							<view class="question-option" v-for="(option, optionIndex) in question.optionsList" :key="optionIndex"
								@tap="selectOption(option,optionIndex,question)">
								<text class="option"
									:class="{'more-option-select':option.isChecked ||question.isSelected === true && option.isRight, 'more-option-error': question.isSelected === true &&option.isChecked === true && option.isChecked !== option.isRight }">
									<!-- 如果已经选中 则判断是否选中正确答案 -->
									<text>{{optionTag[optionIndex]}}</text>
									<!-- <text else>{{optionTag[optionIndex]}}</text> -->
								</text>
								<text class="option-item"
									:class="{ 'option-right-text': option.isChecked ,'option-error-text':question.isSelected === true && option.isChecked &&option.isChecked !== option.isRight }">{{option['label']}}
								</text>
							</view>
						</view>
						<!-- 简答题 -->
						<!-- <view v-if="question.questionTypeName === '简答题' && questionType ==0">
							<uni-easyinput type="textarea" v-model="jdtText" placeholder="请输入答案后自行判断"></uni-easyinput>
							<div class="btn-gropu">
								<button :disabled="question.rightAnswer" class="btn-def" size="mini" @click="answerResult(index,question,jdtText)">显示答案</button>
								<button  :disabled="question.isChecked" class="btn-success" type="primary" size="mini"
									@click="showAnswerOpt(question,index,jdtText,1)">答对了</button>
								<button :disabled="question.isChecked" class="btn-error" type="warn" size="mini"
									@click="showAnswerOpt(question,index,jdtText,0)">答错了</button>
							</div>
						</view> -->
            <!-- 填空题 -->
						<view v-if="['填空题','简答题'].includes(question.questionTypeName) && questionType ==0">
							<uni-easyinput type="textarea" v-model="tktText" placeholder="请输入答案后自行判断"></uni-easyinput>
							<div class="btn-gropu">
								<button class="btn-def" v-if="!question.isSelected" size="mini" @click="showAnswerOpt(question,index,tktText)">提交答案</button>
							</div>
						</view>
						<!-- 判断题 -->
						<view v-if="question.questionTypeName === '判断题' && questionType ==0">
							<view class="question-option" v-for="(option, optionIndex) in question.optionsList" :key="optionIndex"
								@tap="selectOption(option,optionIndex,question)">
								<text class="option"
									:class="{ 'option-right': question.isSelected === true && option.isRight,'option-error':option.isChecked === true && option.isChecked !== option.isRight }">
									<!-- 如果已经选中 则判断是否选中正确答案 -->
									<text v-if="!(question.isSelected) || !option.isChecked ">{{optionTag[optionIndex]}}</text>
									<!-- <text else>{{optionTag[optionIndex]}}</text> -->
								</text>
								<text class="option-item"
									:class="{ 'option-right-text': question.optionsList.isSelected === true && option.isRight,'option-error-text':option.isChecked === true && option.isChecked !== option.isRight }">{{option['label']}}
								</text>
							</view>
						</view>
						<!-- 背题逻辑 -->
						<view v-if="questionType ==1">
							<view class="question-options" v-if="['单选题','判断题'].includes(question.questionTypeName)">
								<view class="question-option" v-for="(option, optionIndex) in question.optionsList" :key="optionIndex">
									<text class="option"
										:class="{ 'option-right':  option.isRight,'option-error':option.isChecked === true && option.isChecked !== option.isRight }">
										<!-- 如果已经选中 则判断是否选中正确答案 -->
										<text
											v-if="!(question.isSelected) || !option.isChecked ">{{option.isRight?null:optionTag[optionIndex]}}</text>
										<!-- <text else>{{optionTag[optionIndex]}}</text> -->
									</text>
									<text class="option-item"
										:class="{ 'option-right-text': option.isRight,'option-error-text':option.isChecked === true && option.isChecked !== option.isRight }">{{option['label']}}
									</text>
								</view>
							</view>
							<!-- 多选题逻辑 -->
							<view v-if="question.questionTypeName === '多选题'">
								<view class="question-option" v-for="(option, optionIndex) in question.optionsList" :key="optionIndex">
									<text class="option"
										:class="{'more-option-select': option.isRight, 'more-option-error': question.isSelected === true &&option.isChecked === true && option.isChecked !== option.isRight }">
										<!-- 如果已经选中 则判断是否选中正确答案 -->
										<text>{{optionTag[optionIndex]}}</text>
									</text>
									<text class="option-item"
										:class="{ 'option-right-text': option.isRight ,'option-error-text':question.isSelected === true && option.isChecked &&option.isChecked !== option.isRight }">{{option['label']}}
									</text>
								</view>
							</view>
						</view>
						<button class="post-answer"
							v-if="questionType ==0 &&question.questionTypeName == '多选题' &&  !question.isSelected"
							@click="moreAnswer(option,optionIndex,question)">提交答案</button>
						<!-- :style="{width:winWidth}" -->
						<!--  -->
						<view class="answer" v-if="question.isSelected || questionType==1">
							<text style="font-weight: 500;">答案:&nbsp;</text>
							<text v-if="!(['简答题','填空题'].includes(question.questionTypeName))" style="color:#4674f7" v-for="(item,index) in question.options" :key="index">
								<text v-for="(it,dix) in question.rightOptions" :key="it">
									{{item == it?optionTag[index]:''}}
								</text>
							</text>
              <text v-else>{{ question.rightOptions[0] }}</text>
							<text style="color:#4674f7">{{currentQuestionRightItem}}</text>
							<text style="color:#4674f7;float: right;" @click="errAnswerPost(question)">报错</text>
						</view>
					</view>

					<view v-if="question.isSelected || questionType==1"
						style="width: 100%;height: 20rpx;background-color: #f3f3f3;">
					</view>
					<!-- 解析 -->
					<!--  -->
					<view class="bottom-content" v-if="question.isSelected || questionType==1">
						<view class="analysis">
							<view style="width: 100%;height: 60rpx;">
								<text class="answer-info">解析</text>
								<img src="../../static/answer/anwser.png" style="width: 35rpx;height: 35rpx;margin-right: 10rpx;"
									alt="">
								<img src="../../static/answer/vip.png" style="width: 50rpx;height: 25rpx;margin-right: 10rpx;" alt=""
									srcset="" />
							</view>
							<view style="width: 100%;min-height: 50rpx;line-height:50rpx;display: inline-block;">
								{{question.analysis}}
							</view>
						</view>
						<view class="note">
							<text class="answer-info">笔记</text>
							<text style="color:#4674f7;float: right;" @click="toggle('notes')">添加笔记</text>
						</view>
					</view>
				</view>
			</swiper-item>
		</swiper>
		<view class="foote">
			<view>
				<!-- <button class="button" type="primary"><text class="button-text">底部</text></button> -->
				<!-- 普通弹窗 -->
				<view class="popup-header">
					<view class="popup-left">


						<view class="icon-item" v-if="isExam" @click="examEnd('click')">
							<button class="end-exam" size="mini"
								style="background-color: #4675f7;color: #fff;border-radius: 50rpx">交卷</button>
						</view>
						<view class="icon-item" @click="collectAdd(collageTag)">
							<img v-if="!collageTag" class="img" src="../../static/answer/college-f.png" alt="收藏" srcset="">
							<img v-else class="img" src="../../static/answer/college-t.png" alt="收藏" srcset="">
							<text>收藏</text>
						</view>
						<view class="icon-item" v-if="!isExam">
							<img class="img" src="../../static/answer/delete.png" alt="删除" srcset="">
							<text>删除</text>
						</view>
					</view>

					<view class="popup-right">
						<view class="icon-item">
							<img class="img" src="../../static/answer/isRight.png" alt="正确">
							<text>{{isRight}}</text>
						</view>
						<view class="icon-item">
							<img class="img" src="../../static/answer/error.png" alt="错误">
							<text>{{error}}</text>
						</view>
						<view class="icon-item" @click="toggle('card')">
							<img class="img" src="../../static/answer/pannel.png" alt="面板">
							<text>答题卡</text>
						</view>
					</view>

				</view>
				<uni-popup ref="popup" background-color="#fff" @change="change">
					<view v-if="popupType === 'card'" class="popup-content"
						:class="{ 'popup-height': type === 'left' || type === 'right' }">
						<view v-for="(item,index) in list" :key="index">
							<!-- 'item-list-error':!item.isSelectRight -->
							<view class="item-list"
								:class="{'item-list-isRight':item.errorHistoryFlag,'item-list-error':item.errorHistoryFlag,'item-list-current':current == index}"
								@click="changeQuestionCurrentIndex(index)">
								{{index+1}}
								<img v-if="item.collectFlag" class="item-icon" src="../../static/answer/college.png" alt="">
							</view>
						</view>
					</view>
					<!-- 添加笔记 -->
					<view v-if="popupType === 'notes'">
						<view
							style="padding: 0 10rpx;line-height: 80rpx;height: 80rpx;background-color: #f2f2f2;display: flex;justify-content: space-between;">
							<uni-icons type="closeempty" size="20" @click="closePopup"></uni-icons>
							<view style="color: #4674F6;" @click="addNotesButton">确认</view>
						</view>
						<textarea class="width:100%;height:100rpx" type="textarea" v-model="notesText" placeholder="请输入笔记" />
					</view>
				</uni-popup>
			</view>
		</view>
	</view>
</template>


<script setup>
import {
  ref,
  computed,
  reactive,
  toRaw,
  watch,
  getCurrentInstance
} from 'vue'
import {
  onLoad,
  onReady,
  onHide,
  onUnload
} from '@dcloudio/uni-app'
import {
  useCategory
} from '@/store/index.js'
import {
  mapStores
} from 'pinia'
import AD from '@/components/ad'
 
// import common from '@/utils/common.js'
const winWidth = uni.getWindowInfo().windowWidth + "px"
const winHeight = uni.getWindowInfo().windowHeight + "px"
const info = useCategory()
const answerType = ref(1) // 1刷题 2背题  3语音  4考试
const collage = ref(true)
const isRight = ref(0)
const error = ref(0)
const type = '单选'
const jdtText = ref('')
const tktText = ref('')
let questionType = ref(0) // 0刷题 1 背题 3考试
// 引入全局定义的$http
const currentInstance = getCurrentInstance()
const {
  $http
} = currentInstance.appContext.config.globalProperties
// 状态栏高度
const statusBarHeight = wx.getStorageSync('statusBarHeight') + 'px'
// 导航栏高度
const navigationBarHeight = wx.getStorageSync('navigationBarHeight') + 'px'
// 胶囊按钮高度
const menuButtonHeight = wx.getStorageSync('menuButtonHeight') + 'px'
// 导航栏和状态栏高度
const navigationBarAndStatusBarHeight = wx.getStorageSync('statusBarHeight') +
		wx.getStorageSync('navigationBarHeight') +
		'px'
const back = () => {
  console.log('123');
  uni.navigateBack({
    delta: 1
  })
}
// 滑动时动画时长
const durationTime = ref(500)
// 缓存答题历史
const currentAnswerRecord = reactive({
  total: '',
  size: '',
  page: '',
  categoryId: uni.getStorageSync('currentCategoryId') || uni.getStorageSync('fCurrentCategoryId'),
  currentIndex: ''
})
// 设置当前index值
const setCurrentIndex = () => {
  if (current.value) {
    currentAnswerRecord.currentIndex = current.value
  }
}
// 缓存答题记录的方法
const setStoryAnswerRecord = () => {
  let arr = uni.getStorageSync('currentAnswerRecordList') || []
  if (!arr.length) {
    arr.push({
      ...currentAnswerRecord
    })
  }
  arr.map(item => {
    if (item.categoryId == currentAnswerRecord.categoryId) {
      item.currentIndex = currentAnswerRecord.currentIndex || current.value
      item.page = currentAnswerRecord.page
      item.size = currentAnswerRecord.size
      item.total = currentAnswerRecord.total
    } else {
      arr.push({
        ...currentAnswerRecord
      })
    }

  })
  uni.setStorageSync('currentAnswerRecordList', arr)
}
// 当前题目所在的index坐标
let current = ref(0)
// watch(current, (newval, oldval) => {
//   currentAnswerRecord.currentIndex = newval
//   console.log('currentAnswerRecord', currentAnswerRecord);
// })
const popup = ref(null)
let popupType = ref('card')
const toggle = (type) => {
  // open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
  if (type == 'notes') {
    popupType.value = type
  } else {
    popupType.value = type
  }
  popup.value.open('bottom')
}

const changeQuestionCurrentIndex = (index) => {
  durationTime.value = 0
  current.value = index
  //好像是防止穿透的控制
  show.value = false
  popup.value.close()
  setTimeout(() => {
    durationTime.value = 500
  }, 1000)
}
const switchQuestion = (type) => {
  questionType.value = type
}
const show = ref(false)
const change = (e) => {
  show.value = e.show
  // popupType.value = 'card'
}
const closePopup = () => {
  show.value = false
  popup.value.close()
}

// 添加笔记
let notesText = ref('')
const addNotesButton = () => {
  let opt = {
    params: {
      notesContent: notesText.value,
      categoryId: list['value'][current.value].categoryId,
      questionId: list['value'][current.value].questionId,
      userId: wx.getStorageSync('userInfo').userId,
      question: list[current.value]
    },
    callBack: (e) => {
      show.value = false
      popup.value.close()
      notesText.value = ''
      uni.showToast({
        title: '笔记添加成功',
        icon: 'none',
        duration: 2000
      })
    }
  }
  $http('addNotes', opt)
}
const mode = 'round'
//#region 页面操作和小功能集合
const infoData = ref([{
  content: '内容 A'
}, {
  content: '内容 B'
}, {
  content: '内容 C'
}])
let collageTag = ref(false)
//分页滑动
const swiperChange = (e) => {
  tktText.value = ''
  console.log('分页滑动', e);
  current.value = e.detail.current;
  collageTag.value = list['value'][e.detail.current]['collectFlag']
  if (current.value < currentPage.total && current.value === currentPage.page * currentPage.size - 1) {
    getQuestionList()
  }
}
// 选择选项 option 选择的信息  optionIndex 题目的索引  list 为当前题目的详情
const selectOption = (option, optionIndex, list) => {
  // list.answer = []
  list.answer.push(option.label)
  // option.isChecked = true
  if (!list.isSelected && (['单选题','判断题'].includes(list.questionTypeName))) {
    option.isChecked = true
    list.isSelected = true
    // 把选择的答案添加到当前的answer里
    // list.answer.push(option.label);
    // 1=正确 i=0为错误
    let i = 0
    // 循环遍历 如果选项
    list.optionsList.map((item, index) => {
      if (item.isRight) {
        item.isChecked = true
      }
      if (item.isChecked) {
        i++
      }
    })

    if (i == 1) {
      list.isSelectRight = true
      // 答对的时候添加到集合中的数据
      questionRecordList.value.push({
        categoryId: list.categoryId,
        questionId: list.questionId,
        status: 1,
        questionTypeName: list.questionTypeName
      })
    } else {
      list.isSelectRight = false;
      console.log('添加错题的数据', list, option.label);
      // !list.errorHistoryFlag && 
      addErrorQuestion(list.categoryId, list.questionId, 2, list.answer)
      // 答错的时候需要添加的数据
      questionRecordList.value.push({
        answer: list.answer,
        categoryId: list.categoryId,
        questionId: list.questionId,
        status: 2,
        questionTypeName: list.questionTypeName
      })
    }
    locationQuestionList(list)
  }
  if (!list.isSelected && list.questionTypeName === '多选题') {
    option.isChecked = true

  }
  // 把最后一次答题的index 添加到缓存里
  setCurrentIndex()
}
// 答题缓存
const locationQuestionList = (item) => {
  let arr = uni.getStorageSync('answerDataList') || []
  arr.push(toRaw(item))
  uni.setStorageSync('answerDataList', arr)
}
// 判断选择的题目是否与正确打完全完相等
const areArraysEqual = (arr1, arr2) => {
  if (arr1.length !== arr2.length) {
    return false;
  }
  const sortedArr1 = arr1.sort();
  const sortedArr2 = arr2.sort();
  const str1 = JSON.stringify(sortedArr1);
  const str2 = JSON.stringify(sortedArr2);
  return str1 === str2;
}

// 多选题选择答案
const moreAnswer = (option, index, list) => {
  list.isSelected = true
  // 判断选项和答案是否为完全相等
  let isEqual = areArraysEqual(list.answer, list.rightOptions)
  let status = isEqual ? 1 : 2
  console.log('选项中选择的是否都真确', status);
  // 插入答题记录
  addErrorQuestion(list.categoryId, list.questionId, status, list.answer)
  questionRecordList.value.push({
    answer: list.answer,
    categoryId: list.categoryId,
    questionId: list.questionId,
    status: status,
    questionTypeName: list.questionTypeName
  })
  locationQuestionList(list)
  // 缓存答题顺序
  setCurrentIndex()
}
//对答题记录进行缓存
const storeAnswerData = (item) => {
  let arr = uni.setStorageSync('answerDataList') || []
  arr.push(item)
  uni.setStorageSync('answerDataList', arr)
}

const examAnswerInfo = computed(() => {
  return ''
})
//提交考试记录数据
const postExamScores = (val, examSetting, arrList) => {
  let userInfo = uni.getStorageSync('userInfo')
  let answerResults = arrList.map(item => {
    return {
      answers: item.answer,
      questionId: item.questionId,
      rightFlag: item.isSelectRight
    }
  })
  let opt = {
    params: {
      errorNum: val.errNum,
      successNum: val.rightNum,
      score: val.total,
      userId: userInfo.userId,
      examData: {
        answerResults,
        computeScoreType: examSetting.moreDataWay, //多选题积分模式
        firstErrorQuestion: examSetting.worongPrior,
        notAnswerQuestionFirst: examSetting.doneNot,
        selectOutOfOrder: examSetting.optionOrder,
        showAnswer: examSetting.showAnswer,
        timeNum: examSetting.examTime,
      }
    },
    callBack: (res) => {
      console.log('添加考试记录的回调', res);
    }
  }
  $http('addExamRecord', opt)
}
// 模拟考试分数统计 type 0 时间到自动交卷  1 手动交卷
const answerRate = (type) => {
  let answerList = uni.getStorageSync('answerDataList')
  let examSetting = uni.getStorageSync('examSetting')
  let examScores = {
    answerRate: 0,
    total: 0,
    rightNum: 0,
    errNum: 0,
    currentTime: ''
  }
  answerList.map(item => {
    if (areArraysEqual(item.answer, item.rightOptions)) {
      examScores.rightNum += 1
    } else {
      examScores.errNum += 1
    }
    examScores.total += 1
  })
  examScores.answerRate = ((Math.round(examScores.rightNum) / examScores.total * 10000) / 100).toFixed(2) + '%'
  uni.setStorageSync('exerciseResult', examScores)
  let title = ''
  if (examScores.total > examScores.total + examScores.errNum) {
    title = '还有未完成的题目，是否要提交试卷'
  } else {
    title = '已完成所有试题，现在交卷嘛?'
  }
  if (type === 1) {
    uni.showModal({
      title: '提示',
      content: '已完成所有试题，现在交卷嘛？',
      success: function(res) {
        if (res.confirm) {
          postExamScores(examScores, examSetting, answerList)
          uni.redirectTo({
            url: '../exerciseResult/exerciseResult'
          })
        } else if (res.cancel) {
          console.log('用户点击取消');
        }
      }
    })
  } else {
    uni.showToast({
      title: '考试时间结束！',
      icon: 'error',
      duration: 1500
    })
    postExamScores(examScores, examSetting, answerList)
    setTimeout(() => {
      uni.redirectTo({
        url: '../exerciseResult/exerciseResult'
      })
    }, 1500)
  }

  console.log('正确率', examScores);
}
// 考试交卷
const examEnd = (type) => {
  console.log('考试交卷', type);
  if (type == 'endTime') {
    // 考试时间结束
    answerRate(0)
  } else {
    // 手动交卷
    answerRate(1)
  }

}
// 答案报错
const errAnswerPost = (list) => {
  uni.setStorageSync('errQuestionDetail', list)
  uni.navigateTo({
    url: `../addErrorQuestion/index?feedType=errAnswerFeed`
  });
}

// 添加错题记录
/**
	 * categoryId 问题的分类ID
	 * questionId 问题id
	 * status 答题状态  1对 2错
	 * */
const addErrorQuestion = (categoryId, questionId, status, answer) => {
  console.log('添加错题', answer);
  let opt = {
    params: {
      "categoryId": categoryId,
      "questionId": questionId,
      "status": status,
      "answer": answer
      // "userId": wx.getStorageSync('userInfo').userId
    },
    callBack: (res) => {
      console.log('错题添加成功', res);

    }
  }
  $http('addErrQuestion', opt)
}
// 批量添加错题记录
const questionRecordList = ref([])
watch(questionRecordList.value, (newValue, oldValue) => {
  console.log(newValue, 'newValue')
  let r = 0
  let e = 0
  newValue.map(item => {
    console.log(item,'item')
    if (item.status == 1) {
      r +=1
    } else {
      e +=1
    }
  })
  isRight.value = r
  error.value = e
},{deep:true})
const addErrorQuestionList = () => {
  let opt = {
    params: {
      questionRecordList: questionRecordList.value
    },
    callBack: (res) => {
      console.log('批量添加错题记录', res);
    }
  }
  $http('addErrQuestionList', opt)
}
// 收藏问题
const collectAdd = (tag) => {
  console.log('收藏问题', tag);
  let opt = {
    params: {
      "categoryId": list['value'][current.value]['categoryId'],
      "questionId": list['value'][current.value]['questionId'],
      "questionTypeName": list['value'][current.value]['questionTypeName'],
      "collectType": 2,
    },
    callBack: (res) => {
      collageTag.value = !collageTag.value
      list['value'][current.value]['collectFlag'] = !tag
      uni.showToast({
        title: !tag ? '收藏成功' : '删除成功',
        icon: 'none',
        duration: 2000
      })
    }
  }
  tag ? $http('removeCollectQuestion', opt) : $http('collectQuestion', opt)
}
// 是否收藏
const isCllegeFlage = computed(() => {
  return list['value'][current.value]['collectFlag']
})
//#endregion


//#region 普通题目列表请求

let list = ref([])
// 定义分页数据
const currentPage = reactive({
  page: 1,
  size: 50,
  total: 50
})
const optionTag = ['A', 'B', 'C', 'D', 'E', 'F']
// 请求题目数据
const getQuestionList = (requestionParams) => {
  uni.showLoading({
    title: '加载中...'
  })
  setTimeout(() => {
    uni.hideLoading();
  }, 1000)
  if (requestionParams.type == 5 || requestionParams.type == 6) {
    delete requestionParams.categoryId
  }
  let opt = {
    params: requestionParams,
    callBack: (res) => {
      uni.hideLoading();
      console.log('res', res.records);
      if (requestionParams.type == 1) {
        res.records.map((item, index) => {
          item.index = currentPage.page > 1 ? currentPage.page * currentPage.size +
							index : index
        })
      }
      if (requestionParams.type == 4) {
        res.records.sort(function() {
          return Math.random() - 0.5; // 值为 -0.5 ~ 0.5 的随机数
        })
      }
      // 记录刷题数据
      if (requestionParams.type == 0) {
        if (res.total && res.pages && res.size) {
          currentAnswerRecord.total = res.total
          currentAnswerRecord.page = res.pages
          currentAnswerRecord.size = res.size
        }
      }
      res.records.map((item, index) => {
        item.answer = []
        item.isSelectRight = false
        item.isSelected = false
        item.isSelected = false
        //把题目选项进行整理,添加问题内容、是否选择、是否是正确答案(isRight暂设false)
        const optionObjects = item.options.map(option => {
          let opt = {
            label: option,
            isChecked: "",
            isRight: false
          }
          console.log('requestionParams',requestionParams)
          // 处理历史错误数据的回显
          // if (requestionParams.type == 1) {
          //   if (item.errorHistoryFlag && item.rightOptions.includes(option)) {
          //   item.isSelectRight = true
          //   item.isSelected = true
          //   opt.isChecked = true;
          //   }
          //   if (item.errorQuestionRecord && item.errorHistoryFlag === false) {
          //     item.errorQuestionRecord && item.errorQuestionRecord.answer.map(
          //       it => {
          //         item.isSelectRight = false
          //         item.isSelected = true
          //         if (item.rightOptions.includes(option)) {
          //           opt.isChecked = true;
          //         }
          //         if (it === option) {
          //           opt.isChecked = true
          //           item.answer = item.errorQuestionRecord.answer
          //         }
          //       })
          //   }
          // }
         
          return opt
        });

        // // 添加错题的下标
        let rightAnswerItem = []
        item.options.map((opt, optIdx) => {
          item.rightOptions.map((right, rightIdx) => {
            if (opt === right) {
              rightAnswerItem.push(optIdx)
            }
          })
        })
        item.rightAnswerItem = rightAnswerItem
        // 标记正确答案
        const result = optionObjects.map(optionObj => {
          if (item.rightOptions.includes(optionObj.label)) {
            optionObj.isRight = true;
          }
          return optionObj;
        });

        item.optionsList = result;

      });
      list.value = [...list.value, ...res.records]
      // list.value = res.records
      // 设置加载后当前的 题目是否收藏
      if (res.records.length > 0) {
        collageTag.value = res.records[current.value]['collectFlag']
        currentPage.page = res.page
        currentPage.size = res.size
        currentPage.total = res.total
      }
    }
  }
  $http('getQuestionList', opt)
}
//#endregion
//#region 获取收藏列表 
const getCollectQuestionList = () => {
  let opt = {
    params: {
      page: currentPage.page,
      size: currentPage.size,
      categoryId: uni.getStorageSync('currentCategoryId') || uni.getStorageSync('fCurrentCategoryId')
    },
    callBack: (res) => {
      console.log('获取的收藏列表', res);
      res.records.map((item, index) => {
        //把题目选项进行整理,添加问题内容、是否选择、是否是正确答案(isRight暂设false)
        const optionObjects = item.options.map(option => {
          return {
            label: option,
            isChecked: "",
            isRight: false
          };
        });
        // 添加错题的下标
        setAnswerHistory(item)
      });
      list.value = [...list.value, ...res.records]
      // list.value = res.records
      // 设置加载后当前的 题目是否收藏
      if (res.records.length > 0) {
        collageTag.value = res.records[current.value]['collectFlag']
        currentPage.page = res.page
        currentPage.size = res.size
        currentPage.total = res.total
      }
    },
  }
  $http('getCollectQuestion', opt)
}
//#endregion
//#region 获取错题列表
const getErrorQuestionList = (type) => {
  let opt = {
    params: {
      page: currentPage.page,
      size: currentPage.size,
      categoryId: uni.getStorageSync('currentCategoryId') || uni.getStorageSync('fCurrentCategoryId'),
      status: 2,
    },
    callBack: (res) => {
      console.log('获取错题数据', res);
      // 整体题目数据
      res.records.map((item, index) => {
        const optionObjects = item.question.options.map(option => {
          return {
            label: option,
            isChecked: "",
            isRight: false
          };
        });
        console.log('optionObjects', optionObjects);
        const result = optionObjects.map(optionObj => {
          if (item.question.rightOptions.includes(optionObj.label)) {
            optionObj.isRight = true;
          }
          return optionObj;
        });
        item.question.optionsList = result;
        item.question.isSelected = false
      });

      let a = res.records.map(item => {
        return item.question
      })
      list.value = [...a]
      // 设置加载后当前的 题目是否收藏
      collageTag.value = res.records[current.value]['collectFlag']
      currentPage.page = res.page
      currentPage.size = res.size
      currentPage.total = res.total
    }
  }
  $http('getErrorQuestionList', opt)
}
// 简答题是否答对 type = 1答对了 0打错了
const answerResult = (index, list,value) => {
  console.log('简答题是否答对逻辑', index, list, value)
  // list.isChecked = true
  list.isSelected = true
  list.answer.push(value)
  // showAnswerOpt(index,list,value,type)
}
// 简答题交互逻辑
 /* 
 * @param [{Object}] list 当前题目
 * @param {Number} index 当前题目下标
 * @param {String} value textarea 输入的值
*/
const showAnswerOpt = (list,index,value,result='0') => {
  // 把当前答案添加到当前的answer里
  list.answer.push(value)
  list.rightAnswer = value
  list.isChecked = true
  list.isSelected = true
  
  if (['填空题','简答题'].includes(list.questionTypeName) ) {
    if ((list.rightOptions[0] == value) || (result==1)) {
      // 答对的时候添加到集合中的数据
      list.isSelectRight = true
      answerSuccess(list)
    } else {
      list.isSelectRight = false
      answerError(list)
    }
    locationQuestionList(list)
  }
}


const examSetting = reactive({
  ...uni.getStorageSync('examSetting')
})
let isExam = ref(false)
//#endregion

//#region 
// 记录题目是否答对并提交答题记录
const answerSuccess = (list) => {
  list.isSelectRight = true
  questionRecordList.value.push({
    categoryId: list.categoryId,
    questionId: list.questionId,
    status: 1,
    questionTypeName: list.questionTypeName
  })
}
const answerError = (list) => {
  list.isSelectRight = false;
  addErrorQuestion(list.categoryId, list.questionId, 2, list.answer)
  // 答错的时候需要添加的数据
  questionRecordList.value.push({
    answer: list.answer,
    categoryId: list.categoryId,
    questionId: list.questionId,
    status: 2,
    questionTypeName: list.questionTypeName
  })
}
//#endregion
onLoad((params) => {

  const currentAnswerRecordList = uni.getStorageSync('currentAnswerRecordList')
  currentPage.page = currentAnswerRecordList.page || 1
  currentPage.size = currentAnswerRecordList.size || 50
  // current.value = currentAnswerRecordList.currentIndex
  // 查询类型  0刷题 1背题 2高频错题 33考试  4随机练习 11错题 12收藏  13笔记  
  console.log('listType', params);
  console.log(typeof params.listType, '------')
  //默认传参 (考试模式为单选参数)
  let questionParams = {
    type: params.listType,
    categoryId: uni.getStorageSync('currentCategoryId') || uni.getStorageSync('fCurrentCategoryId'),
    difficulty: params?.difficulty ?? '', //难度
    paperId: Number(params?.paperId) ?? '', //所属试题集
    questionTypeCode: '', //问题类型编码
    questionTypeName: params?.questionTypeName ?? '', //问题类型名称
    page: currentPage.page,
    size: currentPage.size,
  }
  // 答题历史恢复
  if (params.listType == 0 && currentAnswerRecordList) {
    currentAnswerRecordList.map((item) => {
      if (item.categoryId == questionParams.categoryId) {
        current.value = item.currentIndex || 0
      }
    })
  }

  // 查询错题 questionType =1 为背题模式
  if (params.listType == 11) {
    // 设置当前的模式为背题模式
    questionType.value = 1
    getErrorQuestionList(params.listType)
  } else if (params.listType == 12) {
    // 查询收藏的题目
    getCollectQuestionList()
  } else if (['1','2','3'].includes(params.listType)) {
    if (params.listType == 3) {
      // questionType.value = 0
      console.log('questionType------', questionType.value)
      isExam.value = true
    }
    // 考试
    let examSetting = uni.getStorageSync('examSetting')
    // 单选题参数
    let questionParams1 = Object.assign({}, questionParams, {
      type: params.listType,
      page: 1, // 默认1
      size: examSetting.radio, // 题目数量 page*size
      questionTypeName: '单选题'
    })
    // 多选题参数
    let questionParams2 = Object.assign({}, questionParams1, {
      type: params.listType,
      size: examSetting.multipleScore, // 题目数量 page*size
      questionTypeName: '多选题'
    })
    let questionParams3 = Object.assign({}, questionParams1, {
      type: params.listType,
      size: examSetting.cpn, // 题目数量 page*size
      questionTypeName: '填空题'
    })
    let questionParams4 = Object.assign({}, questionParams1, {
      type: params.listType,
      size: examSetting.saq, // 题目数量 page*size
      questionTypeName: '简答题'
    })
    getQuestionList(questionParams1)
    getQuestionList(questionParams2)
    getQuestionList(questionParams3)
    getQuestionList(questionParams4)
  } else {
    getQuestionList(questionParams)
  }
  // console.log(wx.getStorageSync('userInfo'), '获取个人信息');
})
// 隐藏页面
onHide(() => {
  setStoryAnswerRecord()
})
// 页面销毁
onUnload(() => {
  setStoryAnswerRecord()
  questionRecordList.value.length > 0 && addErrorQuestionList()
})
onReady(() => {
  // console.log('info', info.currentInfo);
})
// 设置历史记答题回显
const setAnswerHistory = (item) => {
  let rightAnswerItem = []
  item.options.map((opt, optIdx) => {
    item.rightOptions.map((right, rightIdx) => {
      if (opt === right) {
        rightAnswerItem.push(optIdx)
      }
    })
  })
  item.rightAnswerItem = rightAnswerItem
  // 标记正确答案
  const result = optionObjects.map(optionObj => {
    if (item.rightOptions.includes(optionObj.label)) {
      optionObj.isRight = true;
    }
    return optionObj;
  });
  item.optionsList = result;
  item.isSelected = false
  item.answer = []
}
</script>

<style lang="scss">
	.btn-gropu {
		display: flex;
		margin-top: 10px;

		.btn-def {
			width: 30%;
			border: 1px solid #5daeff;
			background-color: #fff;
			color: #5daeff;
			border-radius: 25px;

		}

		.btn-success {
			width: 30%;
			border-radius: 25px;
			background-color: #04c292;
		}

		.btn-error {
			width: 30%;
			border-radius: 25px;
			background-color: #fa4b4b;
		}
	}

	.container {
		position: relative;
		left: 0;
		top: 0;
		right: 0;
		width: 99vw;
		height: 88vh;
		border-top: 1rpx solid #f8f8f8;
		background-color: #ffffff;

		swiper {
			height: 600px;
			// border: 1rpx solid;
		}

		.question {
			// padding: 20px;
		}

		.question-title {
			font-size: 38.04rpx;
			font-weight: 600;
			margin-bottom: 41.67rpx;

			.option-tag {
				padding: 9.06rpx;
				font-size: 23.55rpx;
				text-align: center;
				border-radius: 4px;
				color: #fff;
				display: inline-block;
				background-color: #4674f6;
			}
		}

		.question-options {
			display: flex;
			flex-direction: column;
		}

		.question-option {
			// padding: 10px;
			border-radius: 5px;
			margin-bottom: 50.72rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			cursor: pointer;

			.option {

				border: 1rpx solid;
				width: 60rpx;
				height: 60rpx;
				text-align: center;
				margin-right: 30rpx;
				border-radius: 50%;
				line-height: 60rpx;
				border-color: #E5E5E5;
				display: inline-block;
			}

			.option-item {
				display: inline-block;
				flex: 1;
				font-weight: 600;
			}





			.more-option-select {
				color: #fff;
				background-color: #4674f6;
			}

			.more-option-text {
				color: #4674f6;
			}

			.more-error-select {
				color: #fff;
				// background-color: #e64c49;
			}



			.option-right {
				background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAYAAAA6/NlyAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAU3SURBVHgB3ZtfUuNGEMa7R8JeDFtlThA4AfYJ1lRi174BJ8jmBHhjSB6xHzdA1nuCZU9AeGMNVTgnWO8JQk4ASQgEW6POtLApS7LkPxoZ2b8qwBYqlT/PdKv7mxFCTBTK19lUKlUgwm/Axhwi5QAhCwSrrhMRrojgChFuwKavAuzmf9JqNesrNxADCBpxRJovdgiooC5cgAgQQFN9ut8M8XB69m7lCjShRfDryj8FicZ+VJFBsHiDZO3s6GUTIhJJcNxCffDUN43y53fpU5iQiQS//vl6Vcr0x6kJ9UBEx4bZrk0y1QWMSbFytyOt1JfnEssg4hvbSl+Wfrr/HsZk5BF+TEjpffWyDMmi3jjMvB315JEEd8Veqpc5SCYtYTxsjzLFhwrmeOXpo85chSSjEpowHzaGiQ4VPDNieyjRbfmQDytaQpOWLdMnMyOWUZ+VQ49DMOiUQMGl3bv3kNyYDSPXTa4DGTili3u3b5DER5hhlLC3nw8z9QHH3cxc3AZzozJ33pvEfFPashaqcyCWyXI16D3oGuFS5e8tQPME5gib5MZFX9PhGWHzPcwZqJqb/vdPgrnzmZOp7IJr/u9YW5cnwdLzTSQJZSi0+AcmpH+UHcGcmZ+z+wmDby/nh0t5/rFtGLlJ8Fyj0CtGHMFWZ2ELkghRrf9eevFrpj7pSKeMlNPlmfwLhdiEhMG2zvnRUhU0QYiv+C92W79rSBLc+UjcOKsvXvUfjloBtq2HFfHCMBNXL5Owt71iOc8ouzfSbTNtZl6ZNogCJAkVt+eHy6445VnI5S6yrx0JuSZswHVICIT2h8aAuHW6Hx01AuG6iP6taULFbafjF1vavdfmo6kvNMeCVyEiUQsDZbvecJJq1tHlVBT3rlV+oSroAjErfGs9415DS2EgyoOSFNppvY2M0jq2L+25QstXGKCd5xEb/RJUaxwtfvIedlq7GGr7iIL9nB8stwwp8hyTw87l4mJQkuK4javUjSgYc6WK3/3n6ckxGSpa/c+w8AfvYS4utMatB8HrsxAFpONuJnXREx2UzOIqLkJRWsVY8RYIVYNEd6z2Bq/zuk9XxcVBUHGBsd0meeFdqFj5Clpg0Xe+0WFTvHGQ2SayPzhnxV1chKC0/iVse/L75wDKpb27k0FG+PnRclmStR13cREKUQu/Vcadod+4G3lxi4sLpPQXmAJs6AkpZRP0k+N4dJJQCLEUFyFY0moJjjFnA4luVDyGiXYM/ymuXbFG1vroeBD9rurMAuiGRcv0H8XKv8cG2J9AmldyQWaRjE1p2eVpNi6q4Dh9/EjwtOCdLNdDM8LCNac24DexTeuEwNp6Rc5TaUkkazCn9GtzrS0Vd+8uk+pPT4yqrhpHmbXeW1fzMJejbJCrR3cJ5lW2eYplJDxu/LLkquN97SG3bHoaimeGtylK8M1Yn2DOZkQ4B1Mbq972kxloADhWTbe7mUUeOzK/bcQEOh4d2alGcSKfkZbqtQM7r0DBXIw4zTtFdESmCTf4Fm6HnRLqabHood5UUghYgPMy1MQb5k0lhFZbLuaHiWXG2iBerNzWEcUOJAhOUOyieFctghh7R3zxR7VGi2IfnnkDjLM8g1gbtNsujMkeASjfr9qGXVU99Ng70nXgPPShCqRRprCXiQT3cPww3ts1RdeC6/2LCE+3RBLcg/dBYcyP8UQV2kOL4B481aVBm+qqWzoe1GLrqS0z9VET0ihoFdxPoUxZ07jNCRAFELiuXP+ssxY98FE81ayQaPFqpA3Wn1K+bOoU2c//V0KQcn0zp2IAAAAASUVORK5CYII=');
				background-size: contain;
				background-repeat: no-repeat;
				background-position: center;
				color: #fff !important;
				background-color: #fff;
			}

			.option-error {
				background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAYAAAA6/NlyAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAASuSURBVHgB3ZtddtpGFMf/d/Cx++TACopXEHsFJisIds4xeWu8AicrCF5B6ApC32Kf1sYrMF1ByAqqrgDs9sF2j+b2XvFxQCCB0Eh8/J6MAFk/ZjRz584VISO61WoRu6gYKvxM4ENmPgRRUd4qhz7qyfseQD2f8QPGtvGETqnV6iEDCA5RyZ1dc8HEFQZVkAL5EdoM07Lk35W+tTw4wolw9321UmD6nFYyCpX3iS9FvI2UpBLOWnQG0v3Nx/2r3++wJEsJi2hZRL/mKDqBXHTTJ3u5TFc3SMg/tXcXhs33VckqDHyQa7h/PHv3CxKycAvrgFTYM9J98RFrhAg09q9uPiX4/HxU1uzSvUwrh1hHmDvW8MkiXXyusN6v2n0wPX+uG54l+2aedKzwBskO8eyzPYoLWmIHLWPpFpsjq5T11guivAgihR9rp1/W9p6NQ65ZB9fIt2cd7L4/lWEfX7HBWMan0vVNI3x8SngD79soejKIHYUHsakubUB1bL6sUtRoMHxwooW7Z9WqIXOLLWIwVbWHrydaWGS/YMvQxc3465GwrnywHV15Ao35B24BI+HwL7FNjLsFwjoyu1r9WMKl3jcW/CuWxMU5xglaeRCM7PT/g6k6SvY0St9u6oO/2/JDdmSKSzSfi+y5nKM5OsfZac8Q0ve+XaOrvHrQwgXit3CAtMpEJkJGx6YcO1/w62HZPprUc4A4Hgen06Z2tpi3phI+tKj0TFno/WecNMawWxv8BGfxsna9WVmIedJRsv/KuVwmHAp7O8dmVqukQVK0zSTScbKSqWzCIT7bA1MgvIZjFpXOU1YxRK9l0OIiMmCedN6yfewhPdRO/0KGERYxfdi//uO3RT6brWyAp9NSGRkS1dJhcpBVyonz0sswTzon2YBchBXfcGQs95/xnW7qxZGLcNQANSRpRJYGFfaQIfNkh+QkLYMWI5ONZyVu6lkmIksLy8a7kSXYD2TAvHk2aUTmAglTH6RLUweOWTSoyF2aqaPCHhySNILKVVqWmgYvfhvuaCwTLsZKMy7hiid0jG48aQ0FHBBOACiLBhWRwYmjBIAMWG11DeZhn+lPuCC01EwaQc2SdpYAYAoaI4hwgkzAnunCAcRo+Mbe7VhzLPdzHUvg4hxhpPcd6LbLKKR7rJ3cr7JuI0u0Oxevbt/o36PQUuugsKXwmNtE0L6lrey9uro5GL6YWDxsYytbthMVPhPCusvmaopaE5ql61Zr/MDU8lBaWaObzBYUOaJVPVM9dkpYh26n0c2KkFxafVYJ08wEgNZGuNrIWgV67VGJw9jUysPZyfeNq+Rh7ry6vj2Kejs2xWNfWCdrD5uDpyWIcR+IFdZgW/dpsRnSC5Uezk3iBYOYSktXwbqixaXP0yVKs0iUHu3WThoGdIE1Ihhcn7m+6EMhifPBgyo93ZEvY7X0dPqcVW0Xx9KPAMhX65JbT1yR7gJd/ciC4HyZRwDSPeTRL2TT2q4ycmAgmurpFmeP8Uhk85kyWmm5EB3i9kGtoDC18JZhq2nl+5KSenqyDZdPqWW2iRXURWn9iOS5KKgy0I13KlOo+7PO8cw9Is2PU8ey/zde0M7qUbz/ARm1qn+laV9HAAAAAElFTkSuQmCC');
				background-size: contain;
				background-repeat: no-repeat;
				background-position: center;
				color: #fff !important;
				background-color: #fff;
			}

			.more-option-error {
				color: #fff !important;
				background-color: #e64c49;
			}

			.more-error-text {
				color: #e64c49 !important;
			}

			.option-right-text {
				color: #4674f6;
			}

			.option-error-text {
				color: #f15752 !important;
			}
		}

		.post-answer {
			margin-top: 50rpx;
			width: 380rpx;
			height: 80rpx;
			line-height: 80rpx;
			color: #fff;
			border-radius: 105rpx;
			background: #4674F6;
		}

		.answer {
			height: 94.2rpx;
			line-height: 94.2rpx;
			padding: 0 28.99rpx;
			// margin: 0 30.8rpx;
			background-color: #f6f6f8;
			margin-bottom: 40rpx;
		}

		.bottom-bar {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 10px;
			background-color: #f5f5f5;
		}

		.progress {
			font-size: 16px;
		}


	}

	.bottom-content {
		padding: 0 20rpx;
		display: flex;
		flex-wrap: wrap;
	}

	.analysis {
		border-top: 1rpx solid #F3F3F3;
		display: flex;
		flex-wrap: wrap;
		min-height: 150rpx;
		width: 100%;
		line-height: 100rpx;
		// margin-bottom: 10rpx;
		// border: 1rpx solid;
	}

	.note {
		display: flex;
		justify-content: space-between;
		height: 100rpx;
		width: 100%;
		// border: 1rpx solid;
		display: flex;
		border-top: 1rpx solid #F3F3F3;
		// margin-top: 55rpx;
	}

	.answer-info {
		font-size: 40rpx;
		font-family: PingFang SC-Bold, PingFang SC;
		font-weight: bold;
		margin-right: 10rpx;
	}

	@mixin flex {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
	}

	@mixin height {
		/* #ifndef APP-NVUE */
		height: 100%;
		/* #endif */
		/* #ifdef APP-NVUE */
		flex: 1;
		/* #endif */
	}

	.box {
		@include flex;
	}

	.end-exam {
		background-color: #4675f7;
		color: #fff;
		border-radius: 50rpx;
	}

	.popup-header {
		position: absolute;
		height: 100rpx;
		width: 100%;
		padding-top: 10px;
		border: 1rpx solid #f8f8f8;
		bottom: 0px;
		display: flex;
		justify-content: space-between;

		.popup-left,
		.popup-right {
			display: inherit;
			flex-direction: inherit;
		}



		.icon-item {
			display: flex;
			flex-wrap: wrap;
			margin: 0 30rpx;
			flex-direction: column;
			text-align: center;
			// border: 1rpx solid;
			align-items: center;



			.img {
				display: block;
				width: 40rpx;
				height: 40rpx;
			}

			text {
				display: block;
			}
		}

	}

	.popup-header-line {
		position: absolute;
		height: 100px;
		width: 100%;
		border: 1rpx solid;
		// bottom: 0px;
	}

	.popup-header-animation {
		transform: translateY(100%);
		opacity: 1;
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		padding-bottom: 34px;
		background-color: #fff;
		transition-duration: 0.3s;
		transition: 300ms ease 0ms;
		transform: translateY(0);
		transition-property: transform;
		transform-origin: 50% 50%;
		-webkit-transition: 300ms ease 0ms;
		-webkit-transform: translateY(0);
		-webkit-transition-property: transform;
		-webkit-transform-origin: 50% 50%;
	}

	.popup-content {
		@include flex;
		// align-items: center;
		justify-content: flex-start;
		padding: 15px;
		height: 500px;
		background-color: #fff;
		align-items: flex-start;
		justify-content: flex-start;
		align-content: flex-start;
		flex-wrap: wrap;

		.item-list {
			width: 81.52rpx;
			height: 81.52rpx;
			line-height: 81.52rpx;
			margin: 10rpx 15rpx;
			border-radius: 50%;
			color: #999999;
			border: 1rpx solid #999999;
			text-align: center;
			position: relative;

			.item-icon {
				position: absolute;
				width: 28rpx;
				height: 28rpx;
				top: -10rpx
			}
		}



		.item-list-isRight {
			background-color: #e0f1fb;
			color: #0e60f5;
		}

		.item-list-error {
			background-color: #fceae8;
			color: #f27570;
			border: none;
		}

		.item-list-current {
			// border-color: #4674F6 ;
			color: #0e60f5 !important;
			background-color: #fff !important;
			border: 1rpx solid #4674F6 !important;
		}
	}

	.popup-height {
		@include height;
		width: 200px;
	}

	.button-group {
		border: 1rpx solid;
		border-radius: 20px;
		background-color: #f2f2f2;
		text-align: center;


	}

	.uni-navbar {
		width: 100%;
		text-align: center;

	}

	.uni-navbar__header-container {
		display: flex;
		justify-content: center;
	}

	$nav-height: 60rpx;

	$border-radius: 30rpx;

	.left-icon {
		margin: 7px 0;
		height: $nav-height;
		line-height: $nav-height;
	}

	.time-box {
		font-size: 30rpx;
		margin-top: 7px;
		height: $nav-height;
		line-height: $nav-height;
	}

	.input-view {
		display: flex;
		flex-direction: row;
		height: $nav-height;
		// border: 1rpx solid;
		background-color: #f2f2f2;
		border-radius: $border-radius;
		// padding: 0 15px;
		flex-wrap: nowrap;
		margin-top: 7px;
		line-height: $nav-height;

		.type-button {
			width: 120rpx;
			font-size: 30rpx;
			// color: #fff;
			border: none;
			height: $nav-height;
			line-height: $nav-height;
			border-radius: $border-radius;

			button {
				border: none !important;
			}
		}

		.active-btn {
			background-color: #4674f6;
			color: #fff;
		}
	}

	.input-text {
		margin: 7px 0;
		display: block;
		height: $nav-height;
		line-height: $nav-height;
	}

	.nav-bar-input {
		height: $nav-height;
		line-height: $nav-height;
		width: 100%;
		// padding: 0 5px;
		font-size: 12px;
		background-color: #f8f8f8;
	}

	.city {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		// width: 160rpx;
		margin-left: 4px;
	}
</style>