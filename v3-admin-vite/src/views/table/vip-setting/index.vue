<script lang="ts" setup>
import { reactive, ref, watch, toRaw, onMounted } from "vue"
import { type IPaperrrayList } from "@/api/paper-list/types/paper-list"
import {
  type FormInstance,
  type FormRules,
  ElMessage,
  ElMessageBox,
  ElTreeSelect,
  ElSelect,
  ElRow,
  ElCol
} from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { vipDetail, addVipSetting, getVipList, modifyVip } from "@/api/vip"
// import treeSelect from '@/components/TreeSelect'
import { formatDateTime } from "@/utils/format"
defineOptions({
  name: "ElementPlus"
})
onMounted(() => {
  // getCategoryTree()
})
const options = reactive([
  {
    value: "超级会员",
    label: "超级会员"
  },
  {
    value: "会员",
    label: "会员"
  },
  {
    value: "普通会员",
    label: "普通会员"
  }
])
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

const formData = reactive({
  categoryNum: null, // 可刷题库数
  collectCategoryNum: null, // 可以收藏的题库数
  dataStatistic: false, // 做题统计
  errorPriority: false, // 错题优先
  exerciseRecordMaxDay: null, // 练习记录保存最大天数
  highFrequencyError: false, // 高频错题权限 1有 - 0无
  mockExamination: false, // 模拟考试权限
  mockExaminationRecord: false, // 模拟考试权限记录
  monthDownloadDocNum: null, // 每月文档可以下载次数
  monthPrice: null, // 价格
  monthSearchQuestionNum: null, // 每月可以搜题次数
  optionOutOfOrder: false, // 选项乱序
  quarterPrice: null, // 季度价格
  questionWithoutAds: false, // 做题没广告
  searchWithoutAds: false, // 搜题没广告
  undonePriority: false, // 未做优先
  vipConfigId: null, // vip配置ID
  vipType: "", // vip类型 普通会员 会员 超级会员
  yearPrice: null // 年度价格
})
const rules = reactive({
  categoryNum: [{ required: true, message: "请输入可刷题库数", trigger: "blur" }],
  collectCategoryNum: [{ required: true, message: "请输入可以收藏的题库数", trigger: "blur" }],
  createTime: [{ required: true, message: "请选择创建时间", trigger: "change" }],
  exerciseRecordDay: [{ required: true, message: "请输入练习记录保存最大天数", trigger: "blur" }],
  monthDownloadDocNum: [{ required: true, message: "请输入每月文档可以下载次数", trigger: "blur" }],
  monthPrice: [{ required: true, message: "请输入价格", trigger: "blur" }],
  monthSearchQuestionNum: [{ required: true, message: "请输入每月可以搜题次数", trigger: "blur" }],
  quarterPrice: [{ required: true, message: "请输入季度价格", trigger: "blur" }],
  vipConfigId: [{ required: true, message: "请输入vip配置ID", trigger: "blur" }],
  vipType: [{ required: true, message: "请输入vip类型", trigger: "blur" }],
  yearPrice: [{ required: true, message: "请输入年价格", trigger: "blur" }]
})

const addPaperModal = () => {
  modalTitle.value = "新增VIP规则"
  dialogVisible.value = true
}

const handleCreate = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (modalTitle.value === "新增VIP规则") {
        console.log("valid", valid, formData)
        addVipSetting({ ...formData }).then(() => {
          ElMessage.success("新增成功")
          dialogVisible.value = false
          getTableData()
        })
      } else {
        modifyVip({
          ...formData
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
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
  dialogVisible.value = false
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
const handleUpdate = (row: any) => {
  modalTitle.value = "修改VIP规则"
  console.log("row.paperName", toRaw(row.optionOutOfOrder), row.highFrequencyError)
  // Object.assign({...formData},{...row})
  formData.categoryNum = toRaw(row.categoryNum) || 0 // 可刷题库数
  formData.collectCategoryNum = toRaw(row.collectCategoryNum) // 可以收藏的题库数
  formData.dataStatistic = toRaw(row.dataStatistic) // 做题统计
  formData.errorPriority = toRaw(row.errorPriority) // 错题优先
  formData.exerciseRecordMaxDay = toRaw(row.exerciseRecordMaxDay) // 练习记录保存最大天数
  formData.highFrequencyError = toRaw(row.highFrequencyError) // 高频错题权限 1有 - 0无
  formData.mockExamination = toRaw(row.mockExamination) // 模拟考试权限
  formData.mockExaminationRecord = toRaw(row.mockExaminationRecord) // 模拟考试权限记录
  formData.monthDownloadDocNum = toRaw(row.monthDownloadDocNum) // 每月文档可以下载次数
  formData.monthPrice = toRaw(row.monthPrice) // 价格
  formData.monthSearchQuestionNum = toRaw(row.monthSearchQuestionNum) // 每月可以搜题次数
  formData.optionOutOfOrder = toRaw(row.optionOutOfOrder) // 选项乱序
  formData.quarterPrice = row.quarterPrice // 季度价格
  formData.questionWithoutAds = toRaw(row.questionWithoutAds) // 做题没广告
  formData.searchWithoutAds = toRaw(row.searchWithoutAds) // 搜题没广告
  formData.undonePriority = toRaw(row.undonePriority) // 未做优先
  formData.vipType = toRaw(row.vipType) // vip类型 普通会员 会员 超级会员
  formData.yearPrice = toRaw(row.yearPrice) // 年度价格
  formData.vipConfigId = toRaw(row.vipConfigId)
  console.log("formData", formData)
  dialogVisible.value = true
}
//#endregion

//#region 查
const tableData = ref<any[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  paperName: "",
  chapterId: ""
})
const getTableData = () => {
  loading.value = true
  getVipList({
    page: paginationData.page,
    size: paginationData.size
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

const handleRefresh = () => {
  getTableData()
}
//#endregion

/** 监听分页参数的变化 */
watch([() => paginationData.page, () => paginationData.size], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" :icon="CirclePlus" @click="addPaperModal">新增VIP规则</el-button>
          <!-- <el-button type="danger" :icon="Delete">批量删除</el-button> -->
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
          <el-table-column prop="vipType" label="vip类型" align="center" />
          <el-table-column prop="yearPrice" label="年度价格" align="center" />
          <el-table-column prop="quarterPrice" label="季度价格" align="center" />
          <el-table-column prop="monthPrice" label="月价格" align="center" />

          <el-table-column prop="categoryNum" label="可刷题库数" align="center" />
          <el-table-column prop="collectCategoryNum" label="可以收藏的题库数" align="center" />
          <el-table-column prop="dataStatistic" label="做题统计" align="center">
            <template #default="scope">
              <el-switch v-model="scope.row.dataStatistic" active-color="#13ce66" disabled inactive-color="#ff4949" />
            </template>
          </el-table-column>
          <el-table-column prop="errorPriority" label="错题优先" align="center">
            <template #default="scope">
              <el-switch v-model="scope.row.errorPriority" active-color="#13ce66" disabled inactive-color="#ff4949" />
            </template>
          </el-table-column>
          <el-table-column prop="exerciseRecordMaxDay" label="练习记录保存最大天数" align="center" />
          <el-table-column prop="highFrequencyError" label="高频错题权限" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.highFrequencyError"
                active-color="#13ce66"
                disabled
                inactive-color="#ff4949"
              />
            </template>
          </el-table-column>
          <el-table-column prop="mockExamination" label="模拟考试权限" align="center">
            <template #default="scope">
              <el-switch v-model="scope.row.mockExamination" active-color="#13ce66" disabled inactive-color="#ff4949" />
            </template>
          </el-table-column>
          <el-table-column prop="mockExaminationRecord" label="模拟考试权限记录" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.mockExaminationRecord"
                active-color="#13ce66"
                disabled
                inactive-color="#ff4949"
              />
            </template>
          </el-table-column>
          <el-table-column prop="monthDownloadDocNum" label="每月文档可以下载次数" align="center" />
          <el-table-column prop="monthSearchQuestionNum" label="每月可以搜题次数" align="center" />
          <el-table-column prop="optionOutOfOrder" label="选项乱序" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.optionOutOfOrder"
                active-color="#13ce66"
                disabled
                inactive-color="#ff4949"
              />
            </template>
          </el-table-column>
          <el-table-column prop="questionWithoutAds" label="做题没广告" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.questionWithoutAds"
                active-color="#13ce66"
                disabled
                inactive-color="#ff4949"
              />
            </template>
          </el-table-column>
          <el-table-column prop="searchWithoutAds" label="搜题没广告" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.searchWithoutAds"
                active-color="#13ce66"
                disabled
                inactive-color="#ff4949"
              />
            </template>
          </el-table-column>
          <el-table-column prop="undonePriority" label="未做优先" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.searchWithoutAds"
                active-color="#13ce66"
                disabled
                inactive-color="#ff4949"
              />
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
        <!-- <el-table-column prop="questionNum" label="题目数量" align="center">
            <template #default="scope">
              <span>{{ scope.row.questionNum || 0 }}</span>
            </template>
          </el-table-column> -->
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
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="160px" label-position="left">
        <el-form-item prop="categoryNum" label="可刷题库数">
          <el-input v-model.number="formData.categoryNum" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="collectCategoryNum" label="可以收藏的题库数">
          <el-input v-model.number="formData.collectCategoryNum" placeholder="请输入" />
        </el-form-item>

        <el-form-item prop="exerciseRecordMaxDay" label="练习记录保存最大天数">
          <el-input v-model.number="formData.exerciseRecordMaxDay" placeholder="请输入" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="highFrequencyError" label="高频错题权限">
              <el-switch v-model="formData.highFrequencyError" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="undonePriority" label="未做优先">
              <el-switch v-model="formData.undonePriority" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="dataStatistic" label="做题统计">
              <el-switch v-model="formData.dataStatistic" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="errorPriority" label="错题优先">
              <el-switch v-model="formData.errorPriority" /> </el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="optionOutOfOrder" label="选项乱序">
              <el-switch v-model="formData.optionOutOfOrder" /> </el-form-item
          ></el-col>
          <el-col :span="12">
            <el-form-item prop="questionWithoutAds" label="做题没广告">
              <el-switch v-model="formData.questionWithoutAds" /> </el-form-item
          ></el-col> </el-row
        ><el-row>
          <el-col :span="12"
            ><el-form-item prop="searchWithoutAds" label="搜题没广告">
              <el-switch v-model="formData.searchWithoutAds" /> </el-form-item
          ></el-col>
          <el-col :span="12">
            <el-form-item prop="mockExamination" label="模拟考试权限">
              <el-switch v-model="formData.mockExamination" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="mockExaminationRecord" label="模拟考试权限记录">
          <el-switch v-model="formData.mockExaminationRecord" />
        </el-form-item>
        <el-form-item prop="monthDownloadDocNum" label="每月文档可以下载次数">
          <el-input v-model.number="formData.monthDownloadDocNum" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="monthSearchQuestionNum" label="每月可以搜题次数">
          <el-input v-model.number="formData.monthSearchQuestionNum" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="vipType" label="vip类型">
          <el-select v-model="formData.vipType" placeholder="请选择">
            <el-option v-for="(item, index) in options" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item prop="yearPrice" label="年度价格">
          <el-input v-model.number="formData.yearPrice" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="quarterPrice" label="季度价格">
          <el-input v-model.number="formData.quarterPrice" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="monthPrice" label="每月价格">
          <el-input v-model.number="formData.monthPrice" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetForm(formRef)">取消</el-button>
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
