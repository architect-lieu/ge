<script lang="ts" setup>
import { reactive, ref, watch, onMounted, toRaw } from "vue"
import { createTableDataApi, deleteTableDataApi, updateTableDataApi, getTableDataApi } from "@/api/table"
import {
  getCategoryTreeDataApi,
  getCategoryDataApi,
  updateCategoryDataApi,
  createCategoryDataApi,
  getCategoryWithChildernDataApi,
  deleteCategoryDataApi,
  getSubjectDataApi
  // addSubject
} from "@/api/category"
import { type IGetTableData } from "@/api/table/types/table"
import { type IGetCategoryData, type GetCategoryResponseData } from "@/api/category/types/category"
import {
  type FormInstance,
  type FormRules,
  ElMessage,
  ElMessageBox,
  ElTree,
  ElRow,
  ElCol,
  ElInput,
  ElSelect,
  ElTreeSelect
} from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import {
  IGetChaptersDataApi,
  IGetChapterUpdate,
  IGetChaptersListData,
  IGetChaptersDetailData,
  ICreateChaptersDataApi,
  // getSubjectList,
  addSubject,
  modifySubject
} from "@/api/chapter/index"
import {
  IChaptersData,
  IChaptersParams,
  GetChaptersDetailData,
  GetChaptersListData
} from "@/api/chapter/types/chapters "
import dayjs from "dayjs"
import { formatDateTime } from "@/utils/format"
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()
defineOptions({
  name: "ElementPlus"
})

const loading = ref<boolean>(false)
onMounted(() => {
  getCategoryTree()
})
//#region 获取分类树
const tableData = ref<IGetTableData[]>([])
const treeData = ref<IGetTableData[]>([])
const isRootNode = ref<boolean>(false)
const dialogTitle = ref<string>("添加节点")
const dialogVisible = ref<boolean>(false)
const treeLevel = ref<number>(0)
const formRef = ref<FormInstance | null>(null)

interface formNodeDataType {
  categoryFatherName?: string
  categoryName: string
  categoryId?: number
  parentId?: string | number
  parentFlag?: boolean
}
const formNodeData = reactive<formNodeDataType>({
  categoryFatherName: "",
  categoryName: "",
  categoryId: 0,
  parentId: "",
  parentFlag: true
})
const defaultProps = {
  children: "children",
  label: "categoryName"
}
const getCategoryTree = () => {
  loading.value = true
  getCategoryTreeDataApi()
    .then((res: any) => {
      treeData.value = res.data
    })
    .catch(() => {
      tableData.value = []
    })
    .finally(() => {
      loading.value = false
    })
}
// 章节查询参数
const subjectRequestParams = reactive({
  // page: 1,
  // size: 10,
  categoryId: 0
})
/**根据chapterId获取科目信息*/
const getSubjectListData = () => {
  getSubjectDataApi(subjectRequestParams.categoryId).then((res) => {
    console.log("获取的章节信息", res)
    subjectTableData.value = res.data
  })
}
const handleNodeClick = (data: IGetCategoryData) => {
  console.log("data", data, data.categoryId)

  // if (!data.children.length) {
  console.log("获取结点的信息")
  subjectRequestParams.categoryId = data.categoryId
  getSubjectListData()
  // }
  treeLevel.value = toRaw(data).level
}
const clickGetList = (res: any) => {
  console.log("获取的当前行的信息", res)
  chaptersRequestParams.categoryId = res.categoryId
  getChaptersListData()
}
/**tree-select 过滤*/
const filterNode = (value: string, data: any) => {
  console.log(value, data)
  if (!value) return true
  return data.categoryName !== "建筑工程"
}
/** 添加分类弹窗*/
const addCategoryModal = () => {
  dialogTitle.value = "添加节点"
  dialogVisible.value = true
}
/** 添加科目弹窗*/
const addSubjectModal = () => {
  dialogSubjectVisible.value = true
}
/** 添加分类*/
const append = (data: any, title: string) => {
  console.log("data", toRaw(data), title)
  dialogVisible.value = true
  dialogTitle.value = title
  const addValue = {
    categoryName: "",
    categoryFatherName: "",
    categoryId: toRaw(data).categoryId,
    parentId: toRaw(data).parentId,
    parentFlag: toRaw(data).parentFlag
  }
  if (title === "添加节点") {
    addValue.categoryName = ""
    addValue.categoryFatherName = toRaw(data).categoryName
    Object.assign(formNodeData, { ...addValue })
  } else if (title === "添加子节点") {
    addValue.parentId = toRaw(data).categoryId
    addValue.categoryId = ""
    Object.assign(formNodeData, { ...addValue })
  } else {
    addValue.categoryFatherName = ""
    addValue.categoryName = toRaw(data).categoryName
    Object.assign(formNodeData, { ...addValue })
  }
}
/** 删除分类*/
const remove = (data: any) => {
  console.log(data)
  const integers: any = []
  integers.push(toRaw(data).categoryId)
  const tip = `确定 <strong style='color:red;'>删除</strong> 节点 <strong style='color:#409eff;'>${toRaw(
    data.categoryName
  )}</strong> ？`
  ElMessageBox({
    title: "Message",
    message: tip,
    showCancelButton: true,
    dangerouslyUseHTMLString: true,
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteCategoryDataApi(toRaw(integers)).then((res: any) => {
      if (res.code === 1200) {
        ElMessage.success("删除成功")
        getCategoryTree()
        getSubjectListData()
      }
    })
  })
}
/** 分类表单rules*/
const formRules: FormRules = reactive({
  categoryName: [{ required: true, trigger: "blur", message: "请输入节点名" }],
  // categoryId: [{ required: true, trigger: "blur", message: "请输入文档分类ID" }],
  categoryFatherName: [{ required: true, trigger: "blur", message: "请输入父节点ID" }]
})
const handleCreate = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (dialogTitle.value === "添加节点" || dialogTitle.value === "添加子节点") {
        /**如果是根节点则删除这个属性*/
        let params
        if (isRootNode.value) {
          params = {
            categoryName: formNodeData.categoryName
          }
        } else {
          params = {
            parentId: Number(formNodeData.parentId),
            categoryName: formNodeData.categoryName,
            parentFlag: formNodeData.parentFlag
          }
        }
        createCategoryDataApi(params).then(() => {
          ElMessage.success("新增成功")
          dialogVisible.value = false
          getCategoryTree()
        })
      } else {
        updateCategoryDataApi({
          categoryName: formNodeData.categoryName,
          categoryId: Number(formNodeData.categoryId),
          parentId: Number(formNodeData.parentId),
          parentFlag: formNodeData.parentFlag
        }).then(() => {
          ElMessage.success("更新成功")
          dialogVisible.value = false
          getCategoryTree()
          getSubjectListData()
        })
      }
      // subjectRequestParams.categoryId = data.categoryId
    } else {
      return false
    }
  })
}
const resetForm = () => {
  formNodeData.categoryName = ""
  formNodeData.parentId = ""
  formNodeData.parentFlag = false
}
//#endregion

//#region 查询章节信息
const currentChapterName = ref("当前分类：")

const options = reactive([
  {
    value: "All",
    label: "All"
  },
  {
    value: true,
    label: "True"
  },
  {
    value: false,
    label: "False"
  }
])
// let formChaptersParams = reactive<IChaptersParams>({
//   categoryId:0,
//   trueQuestionChapterFlag:true
// })
const chaptersRequestParams = reactive<IChaptersData>({
  page: paginationData.page,
  size: paginationData.size,
  categoryId: 0,
  chapterName: "",
  trueQuestionChapterFlag: "All"
})
const chapterTableData = ref<IChaptersData[]>([])
const subjectTableData = ref<IChaptersData[]>([])
const getChaptersById = (data: IChaptersParams) => {
  IGetChaptersListData(data).then((res: any) => {
    chapterTableData.value = res.data
  })
}
const resetChaptersForm = () => {
  paginationData.page = 1
  paginationData.size = 10
  chaptersRequestParams.chapterName = ""
  chaptersRequestParams.trueQuestionChapterFlag = "All"
  getChaptersListData()
}

/** 章节名搜索*/
const handleChaptersSearch = () => {
  paginationData.page = 1
  getChaptersListData()
}

/** 根据chapters获取章节信息*/
const getChaptersListData = () => {
  chaptersRequestParams.page = paginationData.page
  chaptersRequestParams.size = paginationData.size
  if (chaptersRequestParams.trueQuestionChapterFlag === "All") delete chaptersRequestParams.trueQuestionChapterFlag
  IGetChaptersDataApi(chaptersRequestParams)
    .then((res: any) => {
      paginationData.total = res.data.total
      chapterTableData.value = res.data.records
    })
    .catch((err) => {
      console.log("err", err)
      ElMessage(err)
    })
}
/** 章节删除*/
const delChapter = (data: any) => {
  console.log("data", data)
}

//#endregion

//#region 添加编辑章节信息
const chapterModal = ref<boolean>(false)
const chapterFormRef = ref<FormInstance | null>(null)
const chapterModalTitle = ref<string>("章节添加")
const formChapterData = reactive<IChaptersData>({
  chapterId: 0,
  categoryId: 0,
  chapterName: "",
  trueQuestionChapterFlag: false
})
const resetChapterForm = () => {
  formChapterData.chapterName = ""
}
/** 科目编辑*/
const editorSubject = (data: any) => {
  console.log("章节编辑data", data)
  chapterModalTitle.value = "科目编辑"
  chapterModal.value = true
  formChapterData.categoryId = toRaw(data.row).categoryId
  formChapterData.chapterId = toRaw(data.row).chapterId
  formChapterData.chapterName = toRaw(data.row).chapterName
  formChapterData.trueQuestionChapterFlag = toRaw(data.row).trueQuestionChapterFlag
}
/** 章节编辑*/
const editorChapter = (data: any) => {
  console.log("章节编辑data", data)
  chapterModalTitle.value = "章节编辑"
  chapterModal.value = true
  formChapterData.categoryId = toRaw(data.row).categoryId
  formChapterData.chapterId = toRaw(data.row).chapterId
  formChapterData.chapterName = toRaw(data.row).chapterName
  formChapterData.trueQuestionChapterFlag = toRaw(data.row).trueQuestionChapterFlag
}

/** 章节添加*/
const addChapterData = (val) => {
  console.log("val", val)
  subjectRequestParams.categoryId = toRaw(val.categoryId)
  chapterModalTitle.value = "章节添加"
  if (subjectRequestParams.categoryId !== 0) {
    formChapterData.categoryId = toRaw(val.categoryId)
    chapterModal.value = true
    // handleChapterForm()
  } else {
    ElMessage.warning("请选择章节分类")
  }
}
// 章节名称改变
const handleChangeCategoryName = (e: any) => {
  console.log("章节名称改变", e, formChapterData)
  formChapterData.chapterName = e
}
/** 添加/编辑章节*/
const handleChapterForm = () => {
  chapterFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (chapterModalTitle.value === "章节添加") {
        delete formChapterData.chapterId
        ICreateChaptersDataApi(formChapterData).then((res: any) => {
          if (res.code !== 1200) return
          ElMessage.success("新增成功")
          chapterModal.value = false
          getChaptersListData()
        })
      } else {
        console.log("章节编辑formChapterData", formChapterData)
        // let params = {
        //   "parentId": formChapterData.chapterId,
        //   "categoryId": formChapterData.categoryId,
        //   "categoryName": formChapterData.chapterName,
        // }
        IGetChapterUpdate(formChapterData).then((res: any) => {
          if (res.code !== 1200) return
          chapterModal.value = false
          ElMessage.success("更新成功")
          getChaptersListData()
        })
      }
    }
  })
}
/** 监听分页参数的变化 */
watch([() => paginationData.page, () => paginationData.size], getChaptersListData, { immediate: true })
const chapterFormRules: FormRules = reactive({
  categoryName: [{ required: true, trigger: "blur", message: "请输入节点名" }],
  parentId: [{ required: true, trigger: "blur", message: "请输入父节点ID" }]
})
//#endregion

// #region
const dialogSubjectVisible = ref(false)
const dialogSubjectTitle = ref("添加科目")
const formNodeSubjectData = reactive<any>({
  categoryId: "",
  subjectName: ""
})
const formSubjectRef = ref<FormInstance | null>(null)
const formSubjectRules = reactive({
  categoryId: [{ required: true, trigger: "blur", message: "请选择结点" }],
  subjectName: [{ required: true, trigger: "blur", message: "请输入科目名称" }]
})
const handleCreateSubject = () => {
  formSubjectRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (dialogSubjectTitle.value === "添加科目") {
        let params
        if (isRootNode.value) {
          params = {
            categoryId: formNodeSubjectData.categoryId,
            subjectName: formNodeSubjectData.subjectName
          }
        } else {
          params = {
            categoryId: formNodeSubjectData.categoryId,
            subjectName: formNodeSubjectData.subjectName
          }
        }
        addSubject(params).then(() => {
          ElMessage.success("新增成功")
          dialogSubjectVisible.value = false
          // getCategoryTree()
          getSubjectListData()
        })
      } else {
        modifySubject({
          subjectName: formNodeSubjectData.subjectName,
          categoryId: Number(formNodeSubjectData.categoryId)
        }).then(() => {
          ElMessage.success("更新成功")
          dialogSubjectVisible.value = false
          getSubjectListData()
          // getCategoryTree()
        })
      }
      // subjectRequestParams.categoryId = data.categoryId
    } else {
      return false
    }
  })
}
// #endregion
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div />
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div style="margin-bottom: 18px">
            <el-button type="primary" :icon="CirclePlus" @click="addCategoryModal">新增分类或者科目</el-button>
            <!-- <el-button type="primary" :icon="CirclePlus" @click="addSubjectModal">新增科目</el-button> -->
            <!-- <el-button type="primary" :icon="CirclePlus" @click="addChapterData">新增科目</el-button> -->
          </div>

          <div class="tree-wrapper">
            <el-tree
              :data="treeData"
              :props="defaultProps"
              node-key="categoryId"
              :default-expand-all="true"
              :highlight-current="true"
              :check-strictly="false"
              :expand-on-click-node="false"
              @node-click="handleNodeClick"
            >
              <template #default="{ node, data }">
                <span class="custom-tree-node">
                  <span>{{ node.label }}</span>
                  <span v-if="data.level">
                    <a style="margin-left: 8px; color: rgb(99, 159, 227)" @click="append(data, '添加子节点')">添加</a>
                    <a style="margin-left: 8px; color: rgb(99, 159, 227)" @click="append(data, '更新节点')"> 编辑 </a>
                    <a style="margin-left: 8px; color: red" @click="remove(data)"> 删除 </a>
                    <!-- <a style="margin-left: 8px" @click="remove(data)"> 移除 </a> -->
                  </span>
                </span>
              </template>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="16">
          <!-- <el-form :inline="true">
            <el-form-item label="章节名">
              <el-input v-model="chaptersRequestParams.chapterName" placeholder="请输入章节名称" />
            </el-form-item>
            <el-form-item label="是否是真题">
              <el-select v-model="chaptersRequestParams.trueQuestionChapterFlag">
                <el-option v-for="(item, index) in options" :key="index" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChaptersSearch()">搜索</el-button>
              <el-button @click="resetChaptersForm()">重置</el-button>
            </el-form-item>
          </el-form> -->
          <div class="table-wrapper">
            <el-table :data="subjectTableData" stripe border style="width: 100%" @expand-change="clickGetList">
              <el-table-column type="expand">
                <template #default="props">
                  <div m="4">
                    <el-table :data="chapterTableData">
                      <el-table-column label="章节名" prop="chapterName" />
                      <el-table-column label="更新时间" prop="updateTime">
                        <template #default="scope">
                          <span>{{ dayjs(scope.row.updateTime).format("YYYY-MM-DD HH:mm:ss") }}</span>
                        </template>
                      </el-table-column>
                      <el-table-column prop="trueQuestionChapterFlag" label="是否是真题章节" width="180">
                        <template #default="scope">
                          <el-switch
                            v-model="scope.row.trueQuestionChapterFlag"
                            active-color="#13ce66"
                            disabled
                            inactive-color="#ff4949"
                          />
                        </template>
                      </el-table-column>
                      <el-table-column>
                        <template #default="scope">
                          <!-- <el-button type="danger" size="small" @click.prevent="editorChapter(scope)">新增节点</el-button> -->
                          <el-button type="primary" size="small" @click.prevent="editorChapter(scope)">编辑</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="categoryName" label="科目名称" width="200" />
              <!-- <el-table-column prop="createTime" label="创建时间" /> -->
              <el-table-column prop="updateTime" label="更新时间">
                <template #default="scope">
                  <span>{{ formatDateTime(scope.row.updateTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column>
                <template #default="scope">
                  <el-button type="primary" size="small" @click.prevent="addChapterData(scope.row)">新增章节</el-button>
                  <el-button type="primary" size="small" @click.prevent="append(scope.row, '更新节点')"
                    >编辑科目</el-button
                  >
                  <el-button type="primary" size="small" @click.prevent="append(scope.row, '添加子节点')"
                    >添加子节点</el-button
                  >
                  <el-button type="danger" size="small" @click.prevent="remove(scope.row)">删除</el-button>
                </template>
              </el-table-column>
              <!-- <el-table-column prop="categoryNameNum" label="题目数量" /> -->
            </el-table>
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
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 新增/修改题目分类 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" @close="resetForm" width="30%">
      <el-form ref="formRef" :model="formNodeData" :rules="formRules" label-width="120px" label-position="left">
        <el-form-item v-if="dialogTitle == '添加节点'" label="是否是一级节点">
          <el-radio-group v-model="isRootNode">
            <el-radio :label="false" :value="false">否</el-radio>
            <el-radio :label="true" :value="true">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="添加类型">
          <el-radio-group v-model="formNodeData.parentFlag">
            <el-radio :label="true" :value="true">分类</el-radio>
            <el-radio :label="false" :value="false">科目</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="dialogTitle == '添加节点' && !isRootNode" prop="parentId" label="父节点">
          <!-- <el-input v-model="formNodeData.categoryFatherName" placeholder="请输入" /> -->
          <el-tree-select
            v-model="formNodeData.parentId"
            :data="treeData"
            :props="defaultProps"
            node-key="categoryId"
            :filter-node-method="filterNode"
            :highlight-current="true"
            check-strictly
          />
        </el-form-item>
        <el-form-item prop="categoryName" label="节点名或科目名">
          <el-input v-model="formNodeData.categoryName" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确认</el-button>
      </template>
    </el-dialog>
    <!-- 新增/修改科目 -->
    <el-dialog v-model="dialogSubjectVisible" :title="dialogSubjectTitle" @close="resetForm" width="30%">
      <el-form
        ref="formSubjectRef"
        :model="formNodeSubjectData"
        :rules="formSubjectRules"
        label-width="120px"
        label-position="left"
      >
        <el-form-item label="父节点" v-if="dialogSubjectTitle == '添加科目' && !isRootNode" prop="parentId">
          <!-- :filter-node-method="filterNode" -->
          <el-tree-select
            v-model="formNodeSubjectData.categoryId"
            :data="treeData"
            :props="defaultProps"
            node-key="categoryId"
            :highlight-current="true"
            check-strictly
          />
        </el-form-item>
        <el-form-item prop="categoryName" label="科目名">
          <el-input v-model="formNodeSubjectData.subjectName" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogSubjectVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateSubject">确认</el-button>
      </template>
    </el-dialog>

    <!-- 新增/修改章节 -->
    <el-dialog v-model="chapterModal" :title="chapterModalTitle" @close="resetChapterForm" width="30%">
      <el-form ref="chapterFormRef" :model="formChapterData" label-width="100px" label-position="left">
        <!-- <el-form-item prop="categoryName" label="章节Id">
          <el-input v-model="formChapterData.categoryId" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="parentId" label="分类Id">
          <el-input v-model="formChapterData.chapterId" placeholder="请输入" />
        </el-form-item> -->
        <el-form-item prop="categoryName" label="章节名称">
          <el-input @input="handleChangeCategoryName" v-model="formChapterData.chapterName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="parentId" label="是否是真题">
          <el-radio-group v-model="formChapterData.trueQuestionChapterFlag">
            <el-radio :label="true" value="true" size="large">是</el-radio>
            <el-radio :label="false" value="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="chapterModal = false">取消</el-button>
        <el-button type="primary" @click="handleChapterForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.app-container {
  background-color: #fff;
  height: 100%;
}
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
.tree-wrapper {
  margin-bottom: 20px;
  // border: 1px solid;
}
.table-wrapper {
  margin-bottom: 20px;
}

.pager-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
.custom-tree-node {
  // flex: 1;
  // display: flex;
  // align-items: center;
  // justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  // color: blue;
}
</style>
