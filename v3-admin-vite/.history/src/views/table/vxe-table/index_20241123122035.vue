<script lang="ts" setup>
import { reactive, ref, watch, toRaw, onMounted } from "vue"
// import { type IPaperrrayList } from "@/api/paper-list/types/paper-list"
// import { deleteTableDataApi, getTableDataApi } from "@/api/table"
import { updateCategoryDataApi, createCategoryDataApi, userInfoExport } from "@/api/user"
import { IGetUserDetailData } from "@/api/user/types/user"
import {
  type FormInstance,
  type FormRules,
  ElMessage,
  ElMessageBox,
  ElInput,
  ElDatePicker,
  ElTreeSelect,
  ElRow,
  ElCol,
  ElButton
} from "element-plus"
import { format } from "path"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { codeDetail, addCode, getCodeList, modifyCode } from "@/api/code"
import dayjs from "dayjs"
import axios from "axios"
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
  id: "",
  orgCode: "",
  orgName: "",
  vipExpirationTime: dayjs(Date.now()).format("YYYY-MM-DD 23:59:59"),
  maxActiveTimes: "",
  vipFlag: ""
})

//#endregion

//#region 查
const tableData = ref<any[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const getTableData = () => {
  loading.value = true
  const params: any = {
    startTime: searchData.startTime,
    endTime: searchData.endTime,
    mobilephone: searchData.mobilephone,
    nickName: searchData.nickName,
    page: paginationData.page,
    size: paginationData.size
  }
  Object.keys(params).map((item) => {
    if (!params[item]) {
      delete params[item]
    }
  })
  createCategoryDataApi(params)
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
const searchData = reactive({
  nickName: "",
  mobilephone: "",
  startTime: "",
  endTime: ""
})
// const nickName = ref('')
// const mobilephone = ref('')
const time = ref([])
const searchUserList = () => {
  getTableData()
}
const resetSearch = () => {
  searchData.nickName = ""
  searchData.mobilephone = ""
  searchData.startTime = ""
  searchData.startTime = ""
  searchData.endTime = ""
  getTableData()
}
const getDateTime = (data) => {
  if (data[0] && data[1]) {
    console.log(123, data)
    searchData.startTime = dayjs(data[0]).format("YYYY-MM-DD HH:mm:ss")
    searchData.endTime = dayjs(data[1]).format("YYYY-MM-DD HH:mm:ss")
  }
}
const exportExcel = () => {
  const params: any = {
    startTime: searchData.startTime,
    endTime: searchData.endTime,
    mobilephone: searchData.mobilephone,
    nickName: searchData.nickName,
    page: paginationData.page,
    size: paginationData.size
  }
  Object.keys(params).map((item) => {
    if (!params[item]) {
      delete params[item]
    }
  })
  userInfoExport(params).then((data) => {
    console.log("data", data)
    const blob = new Blob([data], { type: "application/vnd.ms-excel" })
    const objectUrl = URL.createObjectURL(blob)
    const a = document.createElement("a")
    document.body.appendChild(a)
    a.setAttribute("style", "display:none")
    a.setAttribute("href", objectUrl)
    const filename = "用户信息.xlsx"
    a.setAttribute("download", filename)
    a.click()
    URL.revokeObjectURL(objectUrl)
  })
}
/** 监听分页参数的变化 */
watch([() => paginationData.page, () => paginationData.size], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="searchData.nickName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="phone" label="手机号">
          <el-input v-model="searchData.mobilephone" placeholder="请输入" />
        </el-form-item>
        <el-form-item>
          <el-date-picker
            style="width: 260px"
            @calendar-change="getDateTime"
            type="daterange"
            range-separator="To"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchUserList">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          <el-button :icon="Download" @click="exportExcel">用户列表导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card v-loading="loading" shadow="never">
      <div class="table-wrapper">
        <el-table ref="xGridDom" :data="tableData">
          <!-- <template #toolbar-btns>
            <vxe-button status="primary" icon="vxe-icon-add" @click="crudStore.onShowModal()">新增用户</vxe-button>
            <vxe-button status="danger" icon="vxe-icon-delete">批量删除</vxe-button>
          </template> -->
          <el-table-column prop="userId" label="用户id" align="center" />
          <el-table-column prop="nickName" label="用户名" align="center" />
          <el-table-column prop="source" label="用户来源" align="center" />
          <el-table-column prop="mobilephone" label="手机号" align="center" />
          <el-table-column prop="subjects" label="答题科目" align="center" />
          <el-table-column prop="lastLoginTime" label="最后登录时间" align="center">
            <template v-slot:default="scope">
              {{ dayjs(scope.row.lastLoginTime).format("YYYY-MM-DD HH:mm:ss") }}
            </template>
          </el-table-column>
          <el-table-column prop="vipFlag" label="用户类型" align="center">
            <template #default="scope">
              <span v-if="scope.row.vipFlag === 1000">普通用户</span>
              <span v-if="scope.row.vipFlag === 1001" style="color: #409eff">普通会员</span>
              <span v-if="scope.row.vipFlag === 1002" style="color: #f1dac3">超级会员</span>
            </template>
          </el-table-column>

          <!-- <el-table-column fixed="right" label="操作" width="150" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="danger" text bg size="small" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column> -->
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
    <!-- <el-dialog v-model="dialogVisible" :title="modalTitle" @close="resetForm" width="30%">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="160px" label-position="left">
        <el-form-item prop="orgCode" label="机构激活码">
          <el-row>
            <el-col :span="20"><el-input v-model="formData.orgCode" placeholder="请输入" /></el-col>
            <el-col :span="4"> <el-button @click="createCode">随机生成</el-button></el-col>
          </el-row>
        </el-form-item>
        <el-form-item prop="orgName" label="机构名称">
          <el-input v-model="formData.orgName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="maxActiveTimes" label="最大激活次数">
          <el-input v-model.number="formData.maxActiveTimes" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="vipFlag" label="vip类型">
          <el-select v-model="formData.vipFlag">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item prop="vipExpirationTime" label="vip过期时间">
          <el-date-picker v-model="formData.vipExpirationTime" placeholder="请选择过期时间" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetForm(formRef)">取消</el-button>
        <el-button type="primary" @click="handleCreate">确认</el-button>
      </template>
    </el-dialog> -->
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
  // justify-content: space-between;
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
