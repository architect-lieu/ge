<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from "element-plus"
import { formatDateTime } from "@/utils/format"
import { getQuestionTypeListApi, addQuestionTypeApi, modifyQuestionTypeApi } from "@/api/question-type"
import type { questionType } from "@/api/question/types/question"

defineOptions({ name: "QuestionType" })

const loading = ref(false)
const tableData = ref<questionType[]>([])
const keyword = ref<string>("")

const dialogVisible = ref(false)
const dialogTitle = ref("新增题型")
const formRef = ref<FormInstance>()
const formData = reactive<{
  questionTypeId?: number | string
  questionTypeName: string
  questionTypeCode: string
}>({
  questionTypeId: undefined,
  questionTypeName: "",
  questionTypeCode: ""
})

const rules: FormRules = {
  questionTypeName: [{ required: true, message: "请输入题型名称", trigger: "blur" }],
  questionTypeCode: [{ required: true, message: "请输入题型编码", trigger: "blur" }]
}

const resetForm = () => {
  formData.questionTypeId = undefined
  formData.questionTypeName = ""
  formData.questionTypeCode = ""
}

const openAdd = () => {
  dialogTitle.value = "新增题型"
  resetForm()
  dialogVisible.value = true
}

const openEdit = (row: questionType) => {
  dialogTitle.value = "编辑题型"
  formData.questionTypeId = row.questionTypeId
  formData.questionTypeName = row.questionTypeName
  formData.questionTypeCode = row.questionTypeCode
  dialogVisible.value = true
}

const submit = () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) return
    try {
      if (formData.questionTypeId) {
        await modifyQuestionTypeApi({
          questionTypeId: formData.questionTypeId,
          questionTypeName: formData.questionTypeName,
          questionTypeCode: formData.questionTypeCode
        })
        ElMessage.success("修改成功")
      } else {
        await addQuestionTypeApi({
          questionTypeName: formData.questionTypeName,
          questionTypeCode: formData.questionTypeCode
        })
        ElMessage.success("新增成功")
      }
      dialogVisible.value = false
      getList()
    } catch (e) {
      ElMessage.error("操作失败")
    }
  })
}

const getList = async () => {
  loading.value = true
  try {
    const { data, code } = await getQuestionTypeListApi(
      keyword.value ? { keyword: keyword.value } : undefined
    )
    if (code !== 1200) {
      tableData.value = []
      return
    }
    tableData.value = (data as unknown as questionType[]) || []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  getList()
}
const handleReset = () => {
  keyword.value = ""
  getList()
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form :inline="true">
        <el-form-item label="关键字">
          <el-input v-model="keyword" placeholder="按名称或编码查询" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" @click="openAdd">新增题型</el-button>
        </div>
        <div />
      </div>

      <div class="table-wrapper">
        <el-table :data="tableData">
          <el-table-column prop="questionTypeName" label="题型名称" align="center" />
          <el-table-column prop="questionTypeCode" label="题型编码" align="center" />
          <el-table-column prop="questionNum" label="题目数量" align="center" />
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
          <el-table-column fixed="right" label="操作" width="120" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="openEdit(scope.row)">编辑</el-button>
              <!-- 后端暂无删除接口 -->
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="30%" @close="resetForm">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px" label-position="left">
        <el-form-item label="题型名称" prop="questionTypeName">
          <el-input v-model="formData.questionTypeName" />
        </el-form-item>
        <el-form-item label="题型编码" prop="questionTypeCode">
          <el-input v-model="formData.questionTypeCode" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确认</el-button>
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
</style>



