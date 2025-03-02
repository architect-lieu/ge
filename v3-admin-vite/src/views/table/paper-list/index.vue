<script lang="ts" setup>
import { reactive, ref, watch, toRaw, onMounted } from "vue"
import { createTableDataApi, deleteTableDataApi, updateTableDataApi, getTableDataApi } from "@/api/table"
import { getPaperListApi, updatePaperApi, addPaperApi } from "@/api/paper-list/index"
import { type IPaperrrayList } from "@/api/paper-list/types/paper-list"
import { getCategoryTreeDataApi, getSubjectDataApi } from "@/api/category"
import { IGetChaptersDataApi } from "@/api/chapter/index"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox, ElTreeSelect, ElSelect } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { formatDateTime } from "@/utils/format"
// import treeSelect from '@/components/TreeSelect'
defineOptions({
  name: "ElementPlus"
})
onMounted(() => {
  getCategoryTree()
})
const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()
const modalTitle = ref<string>("")
//#region 增
const treeData = ref([])
const categoryId = ref("")
const defaultProps = {
  children: "children",
  label: "categoryName"
}
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
interface formType {
  chapterId: string | number
  questionNum?: number | string
  paperId: number | string
  paperName: string
}
const formData = reactive<formType>({
  paperName: "",
  questionNum: "",
  chapterId: "",
  paperId: ""
})
const formRules: FormRules = reactive({
  // questionNum: [{ required: true, trigger: "blur", message: "请输入	题目数量" }],
  paperName: [{ required: true, trigger: "blur", message: "请输入卷子名" }]
})
const handleSelect = (val: any) => {
  console.log("val", val)
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
/** 根据chapters获取章节信息*/
interface categoryType {
  categoryId: number
  chapterId: number
  chapterName: string
  createTime: string
  papers: any[]
  trueQuestionChapterFlag: boolean
  updateTime: string
  label: string
  value: string
}
const handleNodeClick = (item: any) => {
  if (item.children.length === 0) {
    getSubjectList(item.categoryId)
  }
}
const categorySelect = reactive<categoryType[]>([])
const getChaptersListData = () => {
  IGetChaptersDataApi({ categoryId: Number(categoryId.value) })
    .then((res: any) => {
      console.log("根据chapters获取章节信息", res)
      const { records } = res.data
      const arr: any = []
      records.map((item: { chapterName: any; chapterId: any }) => {
        arr.push({
          label: String(item.chapterName),
          value: String(item.chapterId)
        })
      })
      categorySelect.push(...arr)
      console.log("categorySelect------", categorySelect)
    })
    .catch((err) => {
      console.log("err", err)
      ElMessage(err)
    })
}
watch(categoryId, (newval, oldval) => {
  getChaptersListData()
})
const selectChange = (val: any) => {
  console.log("val", val)
}
const addPaperModal = () => {
  modalTitle.value = "新增试题"
  dialogVisible.value = true
}

const filterNode = (value: string, data: any) => {
  console.log(value, data)
  if (!value) return true
  return data.categoryName !== "建筑工程"
}
const handleCreate = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (modalTitle.value === "新增试题") {
        addPaperApi({
          chapterId: Number(formData.chapterId),
          // questionNum: formData.questionNum,
          paperName: formData.paperName
        }).then(() => {
          ElMessage.success("新增成功")
          dialogVisible.value = false
          getTableData()
        })
      } else {
        updatePaperApi({
          paperId: formData.paperId,
          // questionNum: formData.questionNum,
          paperName: formData.paperName,
          chapterId: Number(formData.chapterId)
        }).then(() => {
          ElMessage.success("修改成功")
          dialogVisible.value = false
          getTableData()
        })
      }
    } else {
      return false
    }
  })
}
const resetForm = () => {
  currentUpdateId.value = undefined
  formData.paperName = ""
  formData.questionNum = ""
  formData.chapterId = ""
  formData.paperId = ""
}
//#endregion

//#region 删
const handleDelete = (row: IPaperrrayList) => {
  ElMessageBox.confirm(`正在删除用户：${row.paperName}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    // deleteTableDataApi(row.paperId).then(() => {
    //   ElMessage.success("删除成功")
    //   getTableData()
    // })
  })
}
//#endregion

//#region 改
const currentUpdateId = ref<undefined | string>(undefined)
const handleUpdate = (row: IPaperrrayList) => {
  modalTitle.value = "更新试题"
  formData.paperName = String(toRaw(row.paperName))
  formData.questionNum = toRaw(row.questionNum || "")
  formData.chapterId = String(toRaw(row.chapterId))
  formData.paperId = String(toRaw(row.paperId))
  console.log("formData", formData)
  dialogVisible.value = true
}
//#endregion

//#region 查
const tableData = ref<IPaperrrayList[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  paperName: "",
  chapterId: ""
})
const getTableData = () => {
  loading.value = true
  getPaperListApi({
    page: paginationData.page,
    size: paginationData.size,
    chapterId: searchData.chapterId,
    paperName: searchData.paperName
  })
    .then((res: any) => {
      console.log("res", res)
      paginationData.total = res.data.total
      tableData.value = res.data.records
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

/** 监听分页参数的变化 */
watch([() => paginationData.page, () => paginationData.size], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="paperName" label="卷子名">
          <el-input v-model="searchData.paperName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="chapterId" label="所属章节ID">
          <el-input v-model="searchData.chapterId" placeholder="请输入" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" :icon="CirclePlus" @click="addPaperModal">新增试题集</el-button>
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
          <el-table-column prop="paperId" label="	卷子id" align="center" />
          <el-table-column prop="chapterId" label="	章节id" align="center" />
          <el-table-column prop="paperName" label="卷子名" align="center" />
          <el-table-column prop="questionNum" label="题目数量" align="center">
            <template #default="scope">
              <span>{{ scope.row.questionNum || "--" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" align="center">
            <template #default="scope">
              <span>{{ formatDateTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="updateTime" label="更新时间" align="center">
            <template #default="scope">
              <span>{{ formatDateTime(scope.row.updateTime) }}</span>
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
    <el-dialog v-model="dialogVisible" :title="modalTitle" @close="resetForm" width="30%">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="left">
        <el-form-item label="题目分类">
          <el-tree-select
            v-model="categoryId"
            :data="treeData"
            :props="defaultProps"
            node-key="categoryId"
            :filter-node-method="filterNode"
            :highlight-current="true"
            @node-click="handleNodeClick"
            :check-strictly="false"
          />
          <!-- <treeSelect @emitValue="handleSelect"></treeSelect> -->
        </el-form-item>
        <el-form-item label="选择科目">
          <el-select
            v-model="formData.chapterId"
            :disabled="subjectList.length === 0 ? true : false"
            @change="selectChange"
          >
            <el-option v-for="item in subjectList" :label="item.categoryName" :value="item.categoryId" />
          </el-select>
        </el-form-item>
        <el-form-item prop="paperName" label="卷子名">
          <el-input v-model="formData.paperName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="questionNum" label="题目数量" v-if="modalTitle === '更新试题'">
          <el-input v-model="formData.questionNum" placeholder="请输入" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确认</el-button>
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
</style>
