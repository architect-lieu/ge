<script lang="ts" setup>
import { reactive, ref, watch, toRaw, onMounted } from "vue"
// import { type IPaperrrayList } from "@/api/paper-list/types/paper-list"
import {
  type FormInstance,
  type FormRules,
  ElMessage,
  ElMessageBox,
  ElInput,
  ElDatePicker,
  ElTreeSelect,
  ElRow,
  ElCol
} from "element-plus"
import { format } from "path"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { getMessageDetail, addMessage, getMessageList, modifyMessage } from "@/api/message"
import dayjs from "dayjs"
// import treeSelect from '@/components/TreeSelect'
defineOptions({
  name: "ElementPlus"
})
onMounted(() => {
  // getCategoryTree()
})
const options = reactive([
  {
    value: "1002",
    label: "超级会员"
  },
  {
    value: "1001",
    label: "会员"
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
  messageId: "",
  // orgCode: "",
  // orgName: "",
  // vipExpirationTime: dayjs(Date.now()).format("YYYY-MM-DD 23:59:59"),
  // maxActiveTimes: "",
  // vipFlag: ""
  messageContent: ""
})
const rules = reactive({
  messageContent: [{ required: true, message: "消息内容不能为空", trigger: "blur" }]
  // orgCode: [{ required: true, message: "请输入机构编码", trigger: "blur" }],
  // orgName: [{ required: true, message: "请选择创建时间", trigger: "change" }],
  // vipExpirationTime: [{ required: true, message: "请输入机构名称", trigger: "blur" }],
  // vipFlag: [{ required: true, message: "请输入更新时间", trigger: "blur" }]
})

const addPaperModal = () => {
  modalTitle.value = "新增消息推送"
  dialogVisible.value = true
}

const handleCreate = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (modalTitle.value === "新增消息推送") {
        console.log("valid", valid, formData)
        addMessage({ messageContent: formData.messageContent }).then(() => {
          ElMessage.success("新增成功")
          dialogVisible.value = false
          getTableData()
        })
      } else {
        modifyMessage({
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
  formData.messageContent = ""
  formData.messageId = ""
  dialogVisible.value = false
}
//#endregion

//#region 删
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`正在删除：${row.paperName}，确认删除？`, "提示", {
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
  modalTitle.value = "修改消息内容"
  formData.messageId = toRaw(row.messageId)
  formData.messageContent = toRaw(row.messageContent)
  // Object.assign({...formData},{...row})
  // formData.id = toRaw(row.id)
  // formData.maxActiveTimes = toRaw(row.maxActiveTimes) || 0 // 最大激活次数
  // formData.orgCode = toRaw(row.orgCode) // 	机构编码
  // formData.orgName = toRaw(row.orgName) // 	机构名称
  // formData.vipExpirationTime = toRaw(dayjs(row.vipExpirationTime)) // 	vip过期时间
  // formData.vipFlag = toRaw(String(row.vipFlag)) // vip标记 哪种类型的vip
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
  getMessageList({ page: paginationData.page, size: paginationData.size })
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
  // getTableData()
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
          <el-button type="primary" :icon="CirclePlus" @click="addPaperModal">新增消息推送</el-button>
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
          <!-- <el-table-column prop="id" label="id" align="center" /> -->
          <el-table-column prop="messageContent" label="消息内容" align="center" />
          <el-table-column prop="createTime" label="创建时间" align="center" />

          <el-table-column fixed="right" label="操作" width="150" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
              <!-- <el-button type="danger" text bg size="small" @click="handleDelete(scope.row)">删除</el-button> -->
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
        <!-- <el-form-item prop="orgCode" label="机构激活码">
          <el-row>
            <el-col :span="20"><el-input v-model="formData.orgCode" placeholder="请输入" /></el-col>
            <el-col :span="4"> <el-button @click="createCode">随机生成</el-button></el-col>
          </el-row>
        </el-form-item> -->
        <el-form-item prop="orgName" label="消息内容">
          <el-input type="textarea" v-model="formData.messageContent" placeholder="请输入消息内容" />
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
