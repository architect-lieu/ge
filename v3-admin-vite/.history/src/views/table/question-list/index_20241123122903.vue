<script lang="ts" setup>
import { reactive, ref, watch, toRaw, onMounted } from "vue"
import {
  getQuestionListApi,
  updateQuestionApi,
  createQuestionApi,
  createQuestionTypeApi,
  getQuestionTypeApi,
  getQuestionExportList,
  importQuestionList,
  exportDemo
} from "@/api/question/index"
import { getCategoryTreeDataApi, getSubjectDataApi } from "@/api/category"
import { getPaperListApi, importFileQuestion } from "@/api/paper-list/index"
import {
  questionPageParams,
  updateQuestionParams,
  questionListParams,
  questionType
} from "@/api/question/types/question"
import FileSaver from "file-saver"
import {
  type FormInstance,
  type FormRules,
  ElMessage,
  ElUpload,
  ElMessageBox,
  ElSelect,
  ElCheckbox
} from "element-plus"
import {
  Search,
  Refresh,
  CirclePlus,
  Delete,
  Download,
  RefreshRight,
  Plus,
  Minus,
  Upload
} from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { getToken } from "@/utils/cache/cookies"
import { formatDateTime } from "@/utils/format"
defineOptions({
  name: "ElementPlus"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

interface options {
  label: string
  value: number | string
  chapterId?: string | number
}
onMounted(() => {
  // 获取获取试题类型
  getQuestionList()
  // 获取试题集

  // 获取树形结构
  getCategoryTree()
})
//#region 题目导入
const upLoadUrl = import.meta.env.VITE_BASE_API + "/admin/api/project/question/import-question-excel"
const setHeader = {
  "Ac-Token": getToken()
}
const fileFormDataRef = ref(null)
const fileFormData = reactive({
  categoryPid: "",
  subjectId: "",
  examId: "",
  file: []
})
const handleImportFile = () => {
  console.log("fileFormData", fileFormData)
  importFileQuestion(fileFormData)
    .then((res: any) => {
      if (res.code === 1200) {
        ElMessage.success("导入成功！")
      }
    })
    .catch((err) => {
      ElMessage.error("导入失败！")
    })
    .finally(() => {
      uploadQuestionExcelClone()
    })
}
const importFileInit = (formEl: any) => {
  if (!formEl) return
  formEl.resetFields()
  uploadQuestionExcel.value = false
}
const uploadQuestionExcelClone = () => {
  uploadQuestionExcel.value = false
}
//#endregion

//#region 树形选择器数据
const treeData = ref([])
const categoryId = ref("")
const defaultProps = {
  children: "children",
  label: "categoryName"
}
/** 获取treeSelect数据*/
const getCategoryTree = () => {
  getCategoryTreeDataApi()
    .then((res: any) => {
      treeData.value = res.data
    })
    .catch(() => {
      treeData.value = []
    })
}
const subjectList = ref([])
const getSubjectList = (data: any) => {
  getSubjectDataApi(data).then((res: any) => {
    subjectList.value = res.data
  })
}
const filterNode = (value: string, data: any) => {
  // console.log('filterNode',value, data)
  // if (!value) return true
  // return data.categoryName !== "建筑工程"
}
const subjectSelect = (item) => {
  console.log(item, "获取的item")
  getPaperList({ categoryId: item })
}

const handleNodeClick = (item: any) => {
  if (item.children.length === 0) {
    console.log("item选择的结点", item)
    getSubjectList(item.categoryId)
  }
}
//#endregion
//#region 增
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance>()
const questionTitle = ref<string>("新增题目")
const questionTypeOptions = reactive<options[]>([])
const QDifficulty = reactive<options[]>([
  {
    label: "简单",
    value: "简单"
  },
  {
    label: "中等",
    value: "中等"
  },
  {
    label: "困难",
    value: "困难"
  }
])
interface questionItemType {
  key: number
  value: string
  isRight: boolean
}
const questionItem = reactive<questionItemType[]>([
  {
    key: 1,
    value: "",
    isRight: false
  }
])
const addQuestionIitem = () => {
  QFormData.questionList.push({
    key: Date.now(),
    value: "",
    isRight: false
  })
}
const removeQuestionItem = (index: number) => {
  if (index !== -1) {
    QFormData.questionList.splice(index, 1)
  }
}
const formData = reactive({
  page: paginationData.page,
  size: paginationData.size,
  paperId: "",
  categoryId: "",
  difficulty: "",
  questionTitle: "",
  questionTypeCode: "",
  questionTypeName: ""
})
const QFormData = reactive<updateQuestionParams>({
  analysis: "", //问题解析
  categoryId: "", //章节id
  categoryPid: "", //分类Id
  collectFlag: false,
  difficulty: "", //难度
  options: [], //选项
  paperIds: [], //所属试题集id
  questionId: "", //
  questionTitle: "", // 问题名
  questionTypeCode: "", //问题类型编码
  questionTypeName: "", //问题题型名称
  rightAnswer: "", //正确回答[填空或者简答题]
  rightOptions: [], //正确选项
  questionList: [
    {
      key: 1,
      value: "",
      isRight: false
    }
  ]
})
const questionCreateRules: FormRules = reactive({
  categoryId: [{ required: true, trigger: "blur", message: "请选择所属章节" }],
  questionTitle: [{ required: true, trigger: "blur", message: "请输入问题" }],
  questionTypeName: [{ required: true, trigger: "blur", message: "请选择问题题型" }],
  difficulty: [{ required: true, trigger: "blur", message: "请选择问题难度" }]
})
const resetForm = (formEl: FormInstance | undefined) => {
  // if (!formEl) return
  // formEl.resetFields()
  currentUpdateId.value = undefined
  formData.paperId = ""
  formData.categoryId = ""
  formData.difficulty = ""
  formData.questionTitle = ""
  formData.questionTypeCode = ""
  formData.questionTypeName = ""

  QFormDataInit()
}
const addDialogModal = () => {
  questionTitle.value = "新增题目"
  dialogVisible.value = true
}
const QFormDataInit = () => {
  console.log("QuestionInit")

  dialogVisible.value = false
  QFormData.analysis = ""
  // QFormData.subjectCategoryId = ''
  QFormData.categoryPid = ""
  QFormData.categoryId = ""
  QFormData.difficulty = ""
  QFormData.paperIds = []
  QFormData.questionId = ""
  QFormData.questionTitle = ""
  QFormData.questionTypeCode = ""
  QFormData.questionTypeName = ""
  QFormData.rightAnswer = ""
  QFormData.rightOptions = []
  QFormData.options = []
  QFormData.questionList = [
    {
      key: 1,
      value: "",
      isRight: false
    }
  ]
}
const paperListPage = {
  page: 1,
  size: 10,
  total: 10
}
/** 试题集参数*/
const paperListParams = reactive({
  chapterId: "",
  paperName: ""
})
const paperListOptions = ref<options[]>([])
const paperListOptionSelect = (val: any) => {
  console.log("val---", val, paperListOptions)
  QFormData.paperIds = val
  // paperListOptions.filter((item) => {
  //   if (item.value === val) {
  //     QFormData.categoryId = item.chapterId
  //   }
  // })
}
/** 获取试题集*/
const getPaperList = async (item) => {
  console.log("获取试题集item", item)

  try {
    const params = {
      ...paperListPage,
      chapterId: item.categoryId
    }
    const { data, code } = await getPaperListApi(params)
    if (code !== 1200) return ElMessage.warning("获取试题集列表失败!")
    const arr: any = []
    data.records.map((item: { paperName: any; paperId: any; chapterId: any }) => {
      arr.push({
        label: item.paperName,
        value: item.paperId,
        chapterId: item.chapterId
      })
    })
    console.log(arr, "-----", data.records)
    paperListOptions.value = arr
  } catch (err) {
    console.log(err)
  }
}
const handleCreate = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      console.log("valid", valid)
      const optArr = QFormData.questionList.map((item: { value: any }) => {
        return item.value
      })
      const rightArr: string[] = []
      // let rightAnswer :number[] = []
      QFormData.questionList.map((item: any, index: number) => {
        if (item.isRight) {
          // rightAnswer.push(index)
          rightArr.push(item.value)
        }
      })
      const params = Object.assign(QFormData, {
        options: optArr,
        rightOptions: rightArr
        // rightAnswer:rightAnswer.toString(),
        // paperIds: [Number(QFormData.paperIds)]
      })
      delete params.questionList
      if (questionTitle.value === "新增题目") {
        createQuestionApi(params).then(() => {
          ElMessage.success("新增成功！")
          getTableData()
        })
      } else {
        updateQuestionApi(params).then((res: any) => {
          console.log(res)
          ElMessage.success("修改成功！")
          getTableData()
        })
      }
      QFormDataInit()
    } else {
      return false
    }
  })
}

//#endregion

//#region 删
const handleDelete = (row: questionListParams) => {
  ElMessageBox.confirm(`正在删除用户：${row.categoryId}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    // deleteTableDataApi(row.id).then(() => {
    //   ElMessage.success("删除成功")
    //   getTableData()
    // })
  })
}
//#endregion

//#region 改
const currentUpdateId = ref<undefined | string>(undefined)
const handleUpdate = (row: any) => {
  console.log("row", row)

  // getPaperList({categoryId:item})
  // getPaperList()
  dialogVisible.value = true
  console.log("row", toRaw(row))
  questionTitle.value = "修改题目"
  QFormData.categoryId = toRaw(row.categoryId)
  getSubjectList(row?.subject?.parentId)
  // QFormData.subjectCategoryId = toRaw(row?.categoryId)
  QFormData.categoryPid = toRaw(row?.subject?.parentId)
  QFormData.questionId = toRaw(row.questionId)
  QFormData.paperIds = toRaw(row.paperIds)
  QFormData.questionTitle = toRaw(row.questionTitle)
  QFormData.questionTypeCode = toRaw(row.questionTitle)
  QFormData.questionTypeName = toRaw(row.questionTypeName)
  QFormData.difficulty = toRaw(row.difficulty)
  QFormData.analysis = toRaw(row.analysis)
  QFormData.rightAnswer = toRaw(row.rightAnswer)
  // QFormData.subjectCategoryId = toRaw(row.subjectCategoryId)
  QFormData.questionList.shift()
  toRaw(row.options).map((item: any, index: number) => {
    let isRightItem = false
    toRaw(row.rightOptions).map((isRight: string) => {
      if (item === isRight) {
        isRightItem = true
      }
    })
    QFormData.questionList.push({
      key: Date.now() + index,
      value: item,
      isRight: isRightItem
    })
  })
  const params = {
    chapterId: toRaw(row.chapterId),
    questionTitle: toRaw(row.questionTitle)
  }
  Object.assign(QFormData, { ...params })
  console.log("QFormData", QFormData)
  // currentUpdateId.value = row.id
}
//#endregion

//#region 查
const tableData = ref<questionListParams[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  username: "",
  phone: ""
})
const getTableData = () => {
  loading.value = true
  formData.page = paginationData.page
  formData.size = paginationData.size
  getQuestionListApi({
    ...formData
  })
    .then((res: any) => {
      tableData.value = res.data.records
      paginationData.total = res.data.total
    })
    .catch(() => {
      tableData.value = []
    })
    .finally(() => {
      loading.value = false
    })
}
const handleSearch = () => {
  if (paginationData.page === 1) {
    getTableData()
  }
  paginationData.page = 1
}
const exportDemoExcel = () => {
  exportDemo().then((data) => {
    const blob = new Blob([data], { type: "application/vnd.ms-excel;charset=UTF-8" })
    FileSaver.saveAs(blob, "导入模板示例.xlsx")
  })
}
//数据导出
const exportData = () => {
  getQuestionExportList().then((data) => {
    const blob = new Blob([data], { type: "application/vnd.ms-excel;charset=UTF-8" })
    FileSaver.saveAs(blob, "题目列表.xlsx")
  })
}
console.log("123")

const fileList = ref([])
const uploadQuestionExcel = ref(false)
const handlePreview = () => {}
const handleOnSuccess = (res) => {
  console.log("res", res)
  if (res.code === 1200) {
    ElMessage.success("导入成功")
    uploadQuestionExcel.value = false
    getTableData()
  } else {
    ElMessage.error("系统错误")
  }
}
const handleRemove = () => {}
const handleExceed = () => {}

// 数据导入
const importData = () => {
  uploadQuestionExcel.value = true
}
const resetSearch = () => {
  searchFormRef.value?.resetFields()
  if (paginationData.page === 1) {
    getTableData()
  }
  paginationData.page = 1
}
const handleRefresh = () => {
  getTableData()
}
//#endregion

//#region
const addQuestionTypeModal = reactive<questionType>({
  questionNum: "",
  questionTypeCode: "",
  questionTypeId: "",
  questionTypeName: ""
})
const questionRef = ref<FormInstance | null>(null)
const questionDialog = ref<boolean>(false)
const questionFormRules: FormRules = reactive({
  questionTypeName: [{ required: true, trigger: "blur", message: "请输入题型名称" }],
  questionTypeCode: [{ required: true, trigger: "blur", message: "请输入题型编码" }],
  questionTypeId: [{ required: true, trigger: "blur", message: "请输入题型Id" }],
  questionNum: [{ required: true, trigger: "blur", message: "请输入问题数量" }]
})

const handleQuestionCreate = async () => {
  try {
    const { data, message } = await createQuestionTypeApi(addQuestionTypeModal)
    console.log(data, "data")
    if (!data) return
    ElMessage.success("添加成功")
  } catch (err) {
    console.log("err", err)
    ElMessage.error("系统错误！")
  } finally {
    questionDialog.value = false
  }
}
const getQuestionList = async () => {
  try {
    const { data, code } = await getQuestionTypeApi()
    console.log("data,code----", data, code)
    if (code !== 1200) return
    data.map((item: { questionTypeName: string; questionTypeId: number }) => {
      questionTypeOptions.push({
        label: item.questionTypeName,
        value: item.questionTypeId
      })
    })
  } catch (err) {
    console.log("err", err)
  }
}
//#endregion
/** 监听分页参数的变化 */
watch([() => paginationData.page, () => paginationData.size], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="searchData.username" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="phone" label="手机号">
          <el-input v-model="searchData.phone" placeholder="请输入" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          <el-button :icon="Download" @click="exportDemoExcel">模板导出</el-button>
          <el-button :icon="Download" @click="exportData">导出</el-button>
          <el-button :icon="Upload" @click="importData">导入</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div>
          <!-- <el-button type="primary" :icon="CirclePlus" @click="questionDialog = true">添加题型</el-button> -->
          <el-button type="primary" :icon="CirclePlus" @click="addDialogModal">新增题目</el-button>
          <el-button type="danger" :icon="Delete">批量删除</el-button>
        </div>
        <div>
          <el-tooltip content="下载">
            <el-button type="primary" :icon="Download" circle />
          </el-tooltip>
          <el-tooltip content="刷新表格">
            <el-button type="primary" :icon="RefreshRight" circle @click="handleRefresh" />
          </el-tooltip>
        </div>
      </div>
      <div class="table-wrapper">
        <el-table :data="tableData">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column prop="questionTitle" label="题目" align="center" show-overflow-tooltip />
          <el-table-column prop="questionTypeName" label="类型" align="center" />
          <el-table-column prop="options" width="250" label="选项" align="center" show-overflow-tooltip>
            <template #default="scope">
              <el-tag type="info" v-for="item in scope.row.options">{{ item }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="rightOptions" label="正确答案" align="center" show-overflow-tooltip>
            <template #default="scope">
              <el-tag type="info" v-for="item in scope.row.rightOptions">{{ item }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="paperList" label="所属科目" align="center">
            <template #default="scope">
              <!-- <span>{{ scope.row.paperList }}</span> -->
              <span v-if="!scope.row.paperList">--</span>
              <span v-else>
                <el-tag type="info" v-for="item in scope.row.paperList">{{ item.paperName }}</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" align="center">
            <template #default="scope">
              <span>{{ formatDateTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="150" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="danger" text bg size="small" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pager-wrapper">
        <el-pagination
          background
          :layout="paginationData.layout"
          :page-sizes="paginationData.pageSizes"
          :total="paginationData.total"
          :page-size="paginationData.size"
          :currentPage="paginationData.page"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    <!-- 新增/修改 -->
    <el-dialog v-model="dialogVisible" :title="questionTitle" @close="resetForm" width="30%">
      <el-form ref="formRef" :model="QFormData" :rules="questionCreateRules" label-width="100px" label-position="left">
        <el-form-item label="所属分类" prop="categoryId">
          <el-tree-select
            v-model="QFormData.categoryPid"
            :data="treeData"
            :props="defaultProps"
            :check-strictly="false"
            node-key="categoryId"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            :highlight-current="true"
          />
          <!-- <treeSelect @emitValue="handleSelect"></treeSelect> -->
        </el-form-item>

        <el-form-item label="所属科目">
          <!-- multiple -->
          <el-select v-model="QFormData.categoryId" @change="subjectSelect">
            <el-option v-for="item in subjectList" :label="item.categoryName" :value="item.categoryId" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属试题集" prop="paperIds">
          <el-select v-model="QFormData.paperIds" @change="paperListOptionSelect" multiple>
            <el-option v-for="item in paperListOptions" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题名" prop="questionTitle">
          <el-input v-model="QFormData.questionTitle" />
        </el-form-item>
        <el-form-item label="题型" prop="questionTypeName">
          <el-select v-model="QFormData.questionTypeName">
            <el-option v-for="item in questionTypeOptions" :label="item.label" :value="item.label" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题难度" prop="difficulty">
          <el-select v-model="QFormData.difficulty">
            <el-option v-for="item in QDifficulty" :label="item.label" :value="item.label" />
          </el-select>
        </el-form-item>
        <el-form-item
          v-for="(item, index) in QFormData.questionList"
          :key="item.key"
          :label="'问题选项' + (index + 1)"
          :prop="'questionList[' + index + '].value'"
          :rules="{
            required: true,
            message: '问题选项不能为空',
            trigger: 'blur'
          }"
        >
          <el-input v-model="item.value" />
          <div style="display: flex; flex-wrap: wrap; align-items: center">
            <el-checkbox v-model="item.isRight" label="是否是正确选项" />
            &nbsp;
            <el-button type="primary" size="small" :icon="Plus" circle @click.prevent="addQuestionIitem()" />
            <el-button
              v-if="index !== 0"
              size="small"
              :icon="Minus"
              type="primary"
              circle
              @click.prevent="removeQuestionItem(index)"
            />
            &nbsp; &nbsp;
          </div>
        </el-form-item>
        <!-- <el-form-item label="正确选项">
          <el-input v-model="QFormData.rightOptions"></el-input>
        </el-form-item> -->
        <el-form-item label="问题解析">
          <el-input
            placeholder="请输入问题解析"
            :autosize="{ minRows: 2, maxRows: 4 }"
            type="textarea"
            v-model="QFormData.analysis"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="QFormDataInit()">取消</el-button>
        <el-button type="primary" @click="handleCreate">确认</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="uploadQuestionExcel" title="题目导入" @close="importFileInit" width="30%">
      <el-form label-width="100px" :model="fileFormData" ref="fileFormDataRef">
        <el-form-item label="所属分类" prop="categoryPid">
          <el-tree-select
            v-model="fileFormData.categoryPid"
            :data="treeData"
            :props="defaultProps"
            :check-strictly="false"
            node-key="categoryId"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            :highlight-current="true"
          />
          <!-- <treeSelect @emitValue="handleSelect"></treeSelect> -->
        </el-form-item>

        <el-form-item label="所属科目" prop="subjectId">
          <!-- multiple -->
          <el-select v-model="fileFormData.subjectId" @change="subjectSelect">
            <el-option v-for="item in subjectList" :label="item.categoryName" :value="item.categoryId" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属试题集" prop="examId">
          <el-select v-model="fileFormData.examId" @change="paperListOptionSelect" multiple>
            <el-option v-for="item in paperListOptions" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="题目导入" prop="file">
          <!-- :action="upLoadUrl" -->
          <!-- v-model:file-list="fileList" -->
          <ElUpload
            :auto-upload="false"
            class="upload-demo"
            v-model:file-list="fileFormData.file"
            multiple
            :limit="1"
            :headers="setHeader"
            :on-success="handleOnSuccess"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
          >
            <el-button :icon="Upload">导入</el-button>
          </ElUpload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="importFileInit(fileFormDataRef)">取消</el-button>
        <el-button type="primary" @click="handleImportFile">确认</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="questionDialog" title="添加题型" @close="uploadQuestionExcelClone" width="30%">
      <el-form
        ref="questionRef"
        :model="addQuestionTypeModal"
        :rules="questionFormRules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item prop="questionTypeName" label="题型名称">
          <el-input v-model="addQuestionTypeModal.questionTypeName" placeholder="题目类型" />
        </el-form-item>
        <el-form-item prop="questionTypeCode" label="题型编码">
          <el-input v-model="addQuestionTypeModal.questionTypeCode" placeholder="请输入题型编码" />
        </el-form-item>
        <el-form-item prop="questionTypeId" label="题型Id">
          <el-input v-model="addQuestionTypeModal.questionTypeId" placeholder="请输入题型Id" />
        </el-form-item>
        <el-form-item prop="questionNum" label="问题数量">
          <el-input v-model="addQuestionTypeModal.questionNum" placeholder="请输入问题数量" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="questionDialog = false">取消</el-button>
        <el-button type="primary" @click="handleQuestionCreate">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.search-wrapper {
  margin-bottom: 20px;

  :deep(.el-card__body) {
    padding-bottom: 2px;
  }
}

.toolbar-wrapper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.table-wrapper {
  margin-bottom: 20px;
}

.pager-wrapper {
  display: flex;
  justify-content: flex-end;
}

.el-tag {
  margin-right: 10px;
}
</style>
