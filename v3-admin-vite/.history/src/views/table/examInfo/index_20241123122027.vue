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
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { getCategoryTreeDataApi,getSubjectDataApi } from "@/api/category"
import { modifyExamInfoDetail, addExamInfo, getCategoryInfo, getExamInfoList,modifyExamInfo } from "@/api/examInfo"
import dayjs from "dayjs"
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


//#region 树形选择器数据
const treeData = ref([])
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
const getSubjectList = (data:any) => {
  getSubjectDataApi(data).then((res:any) => {
    subjectList.value = res.data
  })
}
const filterNode = (value: string, data: any) => {
  // console.log('filterNode',value, data)
  // if (!value) return true
  // return data.categoryName !== "建筑工程"
}
const subjectSelect = (item) => {
  console.log(item,'获取的item');
}

const handleNodeClick = (item:any) => {
  if (item.children.length === 0) {
    console.log('item选择的结点', item.categoryId);
    formData.categoryId = item.categoryId
    getSubjectList(item.categoryId)
  }
}
//#region 增
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)

const formData = reactive<any>({
  categoryId:'',
  subjectId: '',
  examInfoId:'',
  // examInfoTitle: '考试简介',
  // examInfoId:'123',
  examInfoContent: {
    //考试简介
    examIntroduction: [{
      title: '',
      content: '',
    }],
    //报考指南
    examGuid: [{
      title: '',
      content: ''
    }],
    //考试大纲
    examScope: [{
      title: '',
      content: ''
    }],
    //考试安排
    examArrangements: [{
      title: '',
      content: ''
    }],
    //成绩证书
    examCertificate: [{
      title: '',
      content: ''
    }],
    // 考试资讯
    examInformation: [{
      title: '',
      content: ''
    }]
  }
})
const addTableItem = (type) => {
  formData.examInfoContent[type].push({
    title: '',
    content:''
  })
}
const removeTableItem = (type, index) => {
  formData.examInfoContent[type].splice(index,1)
}
const rules = reactive({
  examInfoContent: [{ required: true, message: "消息内容不能为空", trigger: "blur" }],
  // orgCode: [{ required: true, message: "请输入机构编码", trigger: "blur" }],
  // orgName: [{ required: true, message: "请选择创建时间", trigger: "change" }],
  // vipExpirationTime: [{ required: true, message: "请输入机构名称", trigger: "blur" }],
  // vipFlag: [{ required: true, message: "请输入更新时间", trigger: "blur" }]
})

const addPaperModal = () => {
  modalTitle.value = "新增考试信息"
  dialogVisible.value = true
}
const handleCreate = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (modalTitle.value === "新增考试信息") {
        console.log("valid", formData)
        addExamInfo(formData).then(() => {
          ElMessage.success("新增成功")
          dialogVisible.value = false
          getTableData()
          resetForm()
        })
      } else {
        modifyExamInfo({
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
const resetForm = () => {
  dialogVisible.value = false
  formData.subjectId = ''
  formData.categoryId = ''
  formData.examInfoContent = {
    //考试简介
    examIntroduction: [{
      title: '',
      content: '',
    }],
    //报考指南
    examGuid: [{
      title: '',
      content: ''
    }],
    //考试大纲
    examScope: [{
      title: '',
      content: ''
    }],
    //考试安排
    examArrangements: [{
      title: '',
      content: ''
    }],
    //成绩证书
    examCertificate: [{
      title: '',
      content: ''
    }],
    // 考试资讯
    examInformation: [{
      title: '',
      content: ''
    }]
  }
  formData.examInfoId = ''

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
  console.log(row, '修改');
  getSubjectList(toRaw(row.categoryId))
  modalTitle.value = "修改消息内容"
  formData.categoryId = toRaw(row.categoryId)
  formData.subjectId = toRaw(row.subjectId)
  formData.examInfoId = toRaw(row.examInfoId)
  formData.examInfoContent = toRaw(row.examInfoContent)
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
  getExamInfoList({ page: paginationData.page, size: paginationData.size}).then((res: any) => {
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
          <el-button type="primary" :icon="CirclePlus" @click="addPaperModal">新增考试信息</el-button>
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
          <el-table-column prop="examInfoId" label="id" align="center" />
          <!-- <el-table-column prop="examInfoTitle" label="分类名" align="center" /> -->
          <el-table-column prop="examInfoTitle" label="科目" align="center" >
              <template #default="scope">
                     <span>{{ scope.row.subject.categoryName }}</span>
              </template>
          </el-table-column>
          <!-- <el-table-column prop="createTime" label="内容" align="center" >
            <template #default="scope">
                     <span >{{ scope.row.examInfoContent }}</span>
              </template>
          </el-table-column> -->
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
        <el-form-item label="所属分类" prop="categoryId">
          <!-- :default-expand-all="true" -->
          <!-- v-model="QFormData.categoryId" -->
          <el-tree-select
            v-model="formData.categoryId"
            :data="treeData"
            :props="defaultProps"
            :check-strictly="false"
            node-key="categoryId"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            :highlight-current="true"
          ></el-tree-select>
          <!-- <treeSelect @emitValue="handleSelect"></treeSelect> -->
        </el-form-item>

        <el-form-item label="所属科目">
          <!-- multiple -->
          <el-select v-model="formData.subjectId" @change="subjectSelect" >
            <el-option v-for="item in subjectList" :label="item.categoryName" :value="item.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-row style="margin-bottom: 10px;">
          <el-col :span="6"><span>考试简介</span></el-col>
          <el-col :span="6"  ><el-button :icon="CirclePlus" @click="addTableItem('examIntroduction')">添加项目</el-button></el-col>
        </el-row>
        <div v-for="(item, index) in formData.examInfoContent.examIntroduction">
          <el-form-item  prop="title" :label="'简介标题'+(index+1)">
            <el-row >
              <el-col :span="index>0?16:24"><el-input v-model="item.title" placeholder="请输入标题"></el-input></el-col>
              <el-col :span="8" v-if="index>0" ><el-button :icon="CirclePlus" @click="removeTableItem('examIntroduction',index)">删除项目</el-button></el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="orgName" :label="'消息内容'+(index+1)">
            <el-input type="textarea" v-model="item.content" placeholder="请输入消息内容" />
          </el-form-item>
        </div>
        <el-row style="margin-bottom: 10px;">
          <el-col :span="6"><span>报考指南</span></el-col>
          <el-col :span="6"  ><el-button :icon="CirclePlus" @click="addTableItem('examGuid')">添加项目</el-button></el-col>
        </el-row>
        <div v-for="(item, index) in formData.examInfoContent.examGuid">
          <el-form-item  prop="title" :label="'简介标题'+(index+1)">
            <el-row >
              <el-col :span="index>0?16:24"><el-input v-model="item.title" placeholder="请输入标题"></el-input></el-col>
              <el-col :span="8" v-if="index>0" ><el-button :icon="CirclePlus" @click="removeTableItem('examGuid',index)">删除项目</el-button></el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="orgName" :label="'消息内容'+(index+1)">
            <el-input type="textarea" v-model="item.content" placeholder="请输入消息内容" />
          </el-form-item>
        </div>
        <el-row style="margin-bottom: 10px;">
          <el-col :span="6"><span>考试大纲</span></el-col>
          <el-col :span="6"  ><el-button :icon="CirclePlus" @click="addTableItem('examScope')">添加项目</el-button></el-col>
        </el-row>
        <div v-for="(item, index) in formData.examInfoContent.examScope">
          <el-form-item  prop="title" :label="'简介标题'+(index+1)">
            <el-row >
              <el-col :span="index>0?16:24"><el-input v-model="item.title" placeholder="请输入标题"></el-input></el-col>
              <el-col :span="8" v-if="index>0" ><el-button :icon="CirclePlus" @click="removeTableItem('examScope',index)">删除项目</el-button></el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="orgName" :label="'消息内容'+(index+1)">
            <el-input type="textarea" v-model="item.content" placeholder="请输入消息内容" />
          </el-form-item>
        </div>

        <el-row style="margin-bottom: 10px;">
          <el-col :span="6"><span>考试安排</span></el-col>
          <el-col :span="6"  ><el-button :icon="CirclePlus" @click="addTableItem('examArrangements')">添加项目</el-button></el-col>
        </el-row>
        <div v-for="(item, index) in formData.examInfoContent.examArrangements">
          <el-form-item  prop="title" :label="'简介标题'+(index+1)">
            <el-row >
              <el-col :span="index>0?16:24"><el-input v-model="item.title" placeholder="请输入标题"></el-input></el-col>
              <el-col :span="8" v-if="index>0" ><el-button :icon="CirclePlus" @click="removeTableItem('examArrangements',index)">删除项目</el-button></el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="orgName" :label="'消息内容'+(index+1)">
            <el-input type="textarea" v-model="item.content" placeholder="请输入消息内容" />
          </el-form-item>
        </div>

        <el-row style="margin-bottom: 10px;">
          <el-col :span="6"><span>成绩证书</span></el-col>
          <el-col :span="6"  ><el-button :icon="CirclePlus" @click="addTableItem('examCertificate')">添加项目</el-button></el-col>
        </el-row>
        <div v-for="(item, index) in formData.examInfoContent.examCertificate">
          <el-form-item  prop="title" :label="'简介标题'+(index+1)">
            <el-row >
              <el-col :span="index>0?16:24"><el-input v-model="item.title" placeholder="请输入标题"></el-input></el-col>
              <el-col :span="8" v-if="index>0" ><el-button :icon="CirclePlus" @click="removeTableItem('examCertificate',index)">删除项目</el-button></el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="orgName" :label="'消息内容'+(index+1)">
            <el-input type="textarea" v-model="item.content" placeholder="请输入消息内容" />
          </el-form-item>
        </div>

        <el-row style="margin-bottom: 10px;">
          <el-col :span="6"><span>考试资讯</span></el-col>
          <el-col :span="6"  ><el-button :icon="CirclePlus" @click="addTableItem('examInformation')">添加项目</el-button></el-col>
        </el-row>
        <div v-for="(item, index) in formData.examInfoContent.examInformation">
          <el-form-item  prop="title" :label="'简介标题'+(index+1)">
            <el-row >
              <el-col :span="index>0?16:24"><el-input v-model="item.title" placeholder="请输入标题"></el-input></el-col>
              <el-col :span="8" v-if="index>0" ><el-button :icon="CirclePlus" @click="removeTableItem('examInformation',index)">删除项目</el-button></el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="orgName" :label="'消息内容'+(index+1)">
            <el-input type="textarea" v-model="item.content" placeholder="请输入消息内容" />
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="resetForm">取消</el-button>
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
