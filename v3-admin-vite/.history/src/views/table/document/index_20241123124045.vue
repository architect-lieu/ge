<script lang="ts" setup>
import { nextTick, reactive, ref, toRaw } from "vue"
import {
  type ElMessageBoxOptions,
  type FormInstance,
  type FormRules,
  ElMessageBox,
  ElMessage,
  ElTreeSelect,
  ElInput,
  ElRadio,
  ElSelect
} from "element-plus"
import {
  createDocumentDataApi,
  IGetDocumentDetailData,
  getDocumentDataApi,
  updateDocumentDataApi,
  deleteDocumentDataApi,
  uploadDocumentFile
} from "@/api/document"
import { getCategoryTreeDataApi, getSubjectDataApi } from "@/api/category"
import {
  IGetDocumentData,
  IGetDocumentRequestData,
  GetDocumentDetailData,
  GetDocumentResponseData
} from "@/api/document/types/document"
import { type GetTableResponseData } from "@/api/table/types/table"
import RoleColumnSolts from "./tsx/RoleColumnSolts"
import StatusColumnSolts from "./tsx/StatusColumnSolts"
import { type ElUpload, type UploadProps } from "element-plus"
import { getToken } from "@/utils/cache/cookies"
import {
  type VxeToolbarInstance,
  type VxeGridInstance,
  type VxeGridProps,
  type VxeModalInstance,
  type VxeModalProps,
  type VxeFormInstance,
  type VxeFormProps,
  type VxeGridPropTypes,
  type VxeFormDefines
} from "vxe-table"
import dayjs from "dayjs"
import { formatDateTime } from "@/utils/format"
defineOptions({
  name: "VxeTable"
})

//#region vxe-grid
interface IRowMeta {
  categoryId: number
  docName: string
  tags: string[]
  type: string
  createTime: string
  status: boolean
  updateTime: string
  docId: number
  data?: IGetDocumentData[]
  /** vxe-table 自动添加上去的属性 */
  _VXE_ID?: string
}
const pageParams = reactive({
  page: 1,
  size: 10
})
// const xToolbar = ref<VxeToolbarInstance>
const xGridDom = ref<VxeGridInstance>()

const xGridOpt: VxeGridProps = reactive({
  loading: true,
  autoResize: true,
  /** 分页配置项 */
  pagerConfig: {
    align: "right"
  },
  /** 表单配置项 */
  formConfig: {
    items: [
      {
        field: "docName",
        itemRender: {
          name: "$input",
          props: { placeholder: "文档名称", clearable: true }
        }
      },
      {
        itemRender: {
          name: "$buttons",
          children: [
            {
              props: { type: "submit", content: "查询", status: "primary" }
            },
            {
              props: { type: "reset", content: "重置" }
            }
          ]
        }
      }
    ]
  },
  /** 工具栏配置 */
  toolbarConfig: {
    refresh: true,
    custom: true,
    slots: { buttons: "toolbar-btns" }
  },
  /** 自定义列配置项 */
  customConfig: {
    /** 是否允许列选中  */
    checkMethod: ({ column }) => !["username"].includes(column.field)
  },
  /** 列配置 */
  columns: [
    {
      type: "checkbox",
      width: "50px"
    },
    {
      field: "categoryId",
      title: "类型ID"
    },
    {
      field: "docName",
      title: "文档名称"
    },
    {
      field: "tags",
      title: "文档标签"
    },
    {
      field: "type",
      title: "文档类型"
    },
    {
      field: "hotFlag",
      title: "是否是热门文档"
    },
    {
      field: "freeFlag",
      title: "是否免费下载"
    },
    {
      field: "docDownloadUrl",
      title: "下载路径"
    },
    {
      field: "coverImage",
      title: "文档封面图",
      slots: { default: "row-coverImage" }
    },
    {
      field: "createTime",
      title: "创建时间"
    },
    {
      field: "updateTime",
      title: "更新时间"
      // slots: StatusColumnSolts
    },
    {
      title: "操作",
      width: "150px",
      fixed: "right",
      showOverflow: false,
      slots: { default: "row-operate" }
    }
  ],
  /** 数据代理配置项（基于 Promise API） */
  proxyConfig: {
    /** 启用动态序号代理 */
    seq: true,
    /** 是否代理表单 */
    form: true,
    /** 是否自动加载，默认为 true */
    // autoLoad: false,
    props: {
      total: "total"
    },
    ajax: {
      query: ({ page, form }: VxeGridPropTypes.ProxyAjaxQueryParams) => {
        xGridOpt.loading = true
        crudStore.clearTable()
        return new Promise<any>((resolve: Function) => {
          let total = 0
          let result: IRowMeta[] = []
          /** 加载数据 */
          const callback = (res: any) => {
            if (res && res.data) {
              const resData = res.data
              console.log("resData", resData)
              resData.createTime = formatDateTime(resData.createTime)
              resData.updateTime = formatDateTime(resData.updateTime)
              // 总数
              if (Number.isInteger(resData.total)) {
                total = resData.total
              }
              // 分页数据
              if (Array.isArray(resData.records)) {
                result = resData.records
              }
            }
            xGridOpt.loading = false
            resolve({ total, result })
          }

          /** 接口需要的参数 */
          const params: IGetDocumentRequestData = {
            categoryId: "",
            docName: form.docName,
            page: pageParams.page || 1,
            size: pageParams.size || 10
          }
          /** 调用接口 */
          getDocumentDataApi(params).then(callback).catch(callback)
        })
      }
    }
  }
  // importConfig: {
  //   remote: true,
  //   types: ["xlsx",'txt'],
  //   modes: ["insert"],
  //   // 自定义服务端导入
  //   importMethod({ file }) {
  //     const $grid = xGridDom.value
  //     const formBody = new FormData()
  //     formBody.append("file", file)
  //     const callback = (res:any)=>{
  //       console.log('文件上传',res);

  //     }
  //     uploadDocumentFile(formBody).then(callback).catch(callback)
  //   }
  // }
})
//#endregion

//#region vxe-modal
const xModalDom = ref<VxeModalInstance>()
const xModalOpt: VxeModalProps = reactive({
  title: "",
  showClose: true,
  escClosable: true,
  maskClosable: true,
  beforeHideMethod: () => {
    xFormDom.value?.clearValidate()
    return Promise.resolve()
  }
})
//#endregion
// console.log(' import.meta.env.BASE_URL: ',  import.meta.env.VITE_BASE_API);
// console.log(' import.meta.env.PROD: ',  import.meta.env.PROD);

//#region element-plus fileUpload
const addDocumentModal = ref(false)
const documentTitleModal = ref("新增文档")
const documentFormRef = ref<FormInstance | null>(null)
const formRules: FormRules = reactive({
  categoryId: [{ required: true, trigger: "blur", message: "请选择结点" }],
  docName: [{ required: true, trigger: "blur", message: "请输入文档名称" }],
  tag: [{ required: true, trigger: "blur", message: "请输入tag多个tag用,分割" }],
  type: [{ required: true, trigger: "blur", message: "请输入结点类型" }],
  docDownloadUrl: [{ required: true, trigger: "blur", message: "请输入下载地址" }]
})
const upLoadDocument = ref(null)
const upLoadImg = ref(null)
const addDocumentForm = reactive<any>({
  docId: 0,
  hotFlag: false,
  coverImage: "",
  downloadUrlKey: "", // 临时下载的key 接口待对接
  categoryId: "",
  categoryPid: "",
  docName: "",
  freeFlag: false,
  tags: [],
  tag: "",
  type: "",
  previewImages: [],
  docDownloadUrl: ""
})
const clearFiles = () => {
  upLoadDocument?.value?.clearFiles()
  upLoadImg?.value?.clearFiles()
  addDocumentForm.categoryPid = ""
  addDocumentForm.docId = 0
  addDocumentForm.hotFlag = false
  addDocumentForm.coverImage = ""
  addDocumentForm.downloadUrlKey = ""
  addDocumentForm.categoryId = ""
  addDocumentForm.docName = ""
  addDocumentForm.freeFlag = false
  addDocumentForm.tags = []
  addDocumentForm.tag = ""
  addDocumentForm.type = ""
  addDocumentForm.previewImages = []
  addDocumentForm.docDownloadUrl = ""
}
const resetDocumentModal = () => {
  addDocumentModal.value = false
  documentTitleModal.value = "新增文档"
  documentFormRef.value?.resetFields()
  clearFiles()
}
// 修改文档弹窗
const openDocumentModal = (type = 0, row: any) => {
  addDocumentModal.value = true
  getCategoryTree()
  if (type === 1) {
    console.log("row", row)
    documentTitleModal.value = "修改文档"
    addDocumentForm.categoryId = row.categoryId
    addDocumentForm.categoryPid = row.categoryPid
    getSubjectList(toRaw(row.categoryPid) || toRaw(row.categoryId))
    addDocumentForm.docName = row?.docName
    addDocumentForm.freeFlag = row?.freeFlag
    addDocumentForm.docId = row.docId
    addDocumentForm.downloadUrlKey = row.downloadUrlKey
    addDocumentForm.downloadFlag = row?.downloadUrlKey
    addDocumentForm.tag = row?.tags.toString() || ""
    addDocumentForm.type = row?.type
    addDocumentForm.docDownloadUrl = row?.docDownloadUrl
    addDocumentForm.previewImages = row?.previewImages
  }
}

const handleAddDocument = () => {
  documentFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      const callback = (err?: any) => {
        console.log("err", err)
        addDocumentModal.value = false
        if (err.code !== 1200) return
        ElMessage.success("操作成功")
        crudStore.commitQuery()
        documentFormRef.value?.resetFields()
      }
      addDocumentForm.tags = addDocumentForm.tag?.split(",")
      console.log("tag", addDocumentForm.tag, addDocumentForm.tags)
      delete addDocumentForm.tag
      console.log("documentTitleModal.value", documentTitleModal.value)
      if (documentTitleModal.value == "新增文档") {
        createDocumentDataApi(addDocumentForm).then(callback).catch(callback)
      } else {
        updateDocumentDataApi(addDocumentForm).then(callback).catch(callback)
      }
    }
  })
}
const defaultProps = {
  children: "children",
  label: "categoryName"
}
const treeData = ref<any>([])
const treeCategoryId = ref("")
const getTreeCategoryId = (val: string) => {
  console.log("val tree", val)
}
console.log(123)
const elTree = ref(null)
const filterNode = (value: any, data: any) => {
  console.log(value, data, "树形选择过滤")
  return false
}
// 获取分类树
const getCategoryTree = () => {
  getCategoryTreeDataApi()
    .then((res: any) => {
      treeData.value = res.data
    })
    .catch(() => {
      treeData.value = []
    })
    .finally(() => {})
}
const handleNodeClick = (item: any) => {
  if (toRaw(item.children.length === 0)) {
    addDocumentForm.categoryId = ""
    getSubjectList(toRaw(item.categoryId))
  }
}
const selectChange = (val: any) => {
  console.log("val", val)
}
const subjectList = ref([])
const getSubjectList = (data: any) => {
  getSubjectDataApi(data).then((res: any) => {
    subjectList.value = res.data
  })
}
const upLoadUrl = import.meta.env.VITE_BASE_API + "/admin/api/project/document/upload/doc"
const upLoadImgUrl = import.meta.env.VITE_BASE_API + "/admin/api/project/document/upload/cover-image"
const setHeader = {
  "Ac-Token": getToken()
}
const xModalUploadDom = ref<VxeModalInstance>()
const xUploadList = ref<any>([])
const xUploadImg = ref<any>([])
const xFileDownloadUrl = ref("")
const xModalUpload: VxeModalProps = reactive({
  title: "文件上传",
  showClose: true,
  escClosable: true,
  maskClosable: true,
  beforeHideMethod: () => {
    return Promise.resolve()
  }
})
// 文件下载
const download = (url: string, type: string, name: string) => {
  //url--对应阿里云资源地址
  fetch(url).then((res) =>
    res.blob().then((blob) => {
      const a = document.createElement("a")
      const url = window.URL.createObjectURL(blob)
      const filename = `${name}.${type} `
      a.href = url
      a.download = filename
      a.click()
      window.URL.revokeObjectURL(url)
    })
  )
}
const onShowUploadModal = () => {
  xModalUploadDom.value?.open()
}
const fileUpLoading = ref(false)
const documentUploadClick = () => {
  fileUpLoading.value = true
}
const handleUploadError = () => {
  fileUpLoading.value = false
}
const handleOnSuccess = (res: any) => {
  fileUpLoading.value = false
  // addDocumentForm.docName = res.data.
  xFileDownloadUrl.value = res.data.urlData
  addDocumentForm.downloadUrlKey = res.data.fileKey
  addDocumentForm.docDownloadUrl = res.data.urlData
  addDocumentForm.previewImages = res.data.previewImages
  addDocumentForm.type = res.data.type
  addDocumentForm.coverImage = res.data.coverImage
  console.log("onSuccessUrl", res, xFileDownloadUrl.value)
}
const upLoadImgSuccess = (res: any) => {
  // xFileDownloadUrl.value = res.data.urlData
  fileUpLoading.value = false
  addDocumentForm.coverImage = res.data.urlData
  console.log("图片上传成功", res, addDocumentForm)
}
const handlePreview: UploadProps["onPreview"] = (uploadFile) => {
  console.log("handlePreview", uploadFile)
}
const handleRemove: UploadProps["onRemove"] = (uploadFile) => {
  console.log("handleRemove", uploadFile)
}
const handleRemoveImg: UploadProps["onRemove"] = (uploadFile) => {
  console.log("handleRemove", uploadFile)
}
const handleExceed: UploadProps["onExceed"] = (uploadFile) => {
  console.log("handleExceed", uploadFile)
}
//#endregion

//#region vxe-form
const xFormDom = ref<VxeFormInstance>()
const xFormOpt = reactive<VxeFormProps>({
  span: 24,
  titleWidth: "100px",
  loading: false,
  /** 是否显示标题冒号 */
  titleColon: false,
  /** 表单数据 */
  data: {
    docId: 0,
    categoryId: "",
    docName: "",
    tags: "",
    type: "",
    docDownloadUrl: ""
    // createTime:'',
    // updateTime:''
  },
  /** 项列表 */
  items: [
    {
      field: "categoryId",
      title: "类型Id",
      itemRender: { name: "$select", props: { placeholder: "请输入" } }
    },
    {
      field: "docName",
      title: "文档名称",
      itemRender: { name: "$input", props: { placeholder: "请输入" } }
    },
    {
      field: "tags",
      title: "文档标签",
      itemRender: { name: "$input", props: { placeholder: "请输入标签用,分割" } }
    },
    {
      field: "type",
      title: "文档类型",
      itemRender: { name: "$input", props: { placeholder: "请输入" } }
    },
    {
      field: "docDownloadUrl",
      title: "下载地址",
      itemRender: { name: "$input", props: { placeholder: "请输入" } }
    },
    {
      align: "right",
      itemRender: {
        name: "$buttons",
        children: [
          { props: { content: "取消" }, events: { click: () => xModalDom.value?.close() } },
          {
            props: { type: "submit", content: "确定", status: "primary" },
            events: {
              click: () => {
                crudStore.onSubmitForm()
              }
            }
          }
        ]
      }
    }
  ],
  /** 校验规则 */
  rules: {
    categoryId: [
      {
        required: true,
        validator: ({ itemValue }) => {
          if (!itemValue) {
            return new Error("请输入")
          }
          if (!itemValue) {
            return new Error("空格无效")
          }
        }
      }
    ],
    docName: [
      {
        required: true,
        validator: ({ itemValue }) => {
          if (!itemValue) {
            return new Error("请输入")
          }
          if (!itemValue?.trim()) {
            return new Error("空格无效")
          }
        }
      }
    ],
    tags: [
      {
        required: true,
        validator: ({ itemValue }) => {
          if (!itemValue) {
            return new Error("请输入")
          }
          if (!itemValue) {
            return new Error("空格无效")
          }
        }
      }
    ],
    type: [
      {
        required: true,
        validator: ({ itemValue }) => {
          if (!itemValue) {
            return new Error("请输入")
          }
          if (!itemValue?.trim()) {
            return new Error("空格无效")
          }
        }
      }
    ],
    docDownloadUrl: [
      {
        required: true,
        validator: ({ itemValue }) => {
          if (!itemValue) {
            return new Error("请输入")
          }
          if (!itemValue?.trim()) {
            return new Error("空格无效")
          }
        }
      }
    ]
  }
})
//#endregion

//#region CRUD
const crudStore = reactive({
  /** 表单类型：修改：true 新增：false */
  isUpdate: true,
  /** 加载表格数据 */
  commitQuery: () => xGridDom.value?.commitProxy("query"),
  /** 清空表格数据 */
  clearTable: () => xGridDom.value?.reloadData([]),
  /** 点击显示弹窗 */
  onShowModal: (row?: any) => {
    console.log("qwe", row)
    // 获取分类树
    getCategoryTree()
    if (row) {
      crudStore.isUpdate = true
      xModalOpt.title = "修改文档"
      // 赋值
      xFormOpt.data.categoryId = row.categoryId
      xFormOpt.data.docName = row.docName
      xFormOpt.data.tags = row.tags.toString()
      xFormOpt.data.freeFlag = row.freeFlag || false
      xFormOpt.data.type = row.type
      xFormOpt.data.docId = row.docId
      xFormOpt.data.docDownloadUrl = row.docDownloadUrl
    } else {
      crudStore.isUpdate = false
      xModalOpt.title = "新增文档"
      getCategoryTree()
    }
    // 禁用表单项
    if (xFormOpt.items) {
      if (xFormOpt.items[0]?.itemRender?.props) {
        xFormOpt.items[0].itemRender.props.disabled = crudStore.isUpdate
      }
    }
    xModalDom.value?.open()
    nextTick(() => {
      !crudStore.isUpdate && xFormDom.value?.reset()
      xFormDom.value?.clearValidate()
    })
  },

  /** 下载文档*/
  onDownload(row: any) {
    console.log("row", row)
    window.open(row.docDownloadUrl)
    // download(row.docDownloadUrl, row.docName, row.type)
  },
  /** 确定并保存 */
  onSubmitForm: () => {
    if (xFormOpt.loading) return
    xFormDom.value?.validate((errMap?: VxeFormDefines.ValidateErrorMapParams) => {
      if (errMap) return
      xFormOpt.loading = true
      const callback = (err?: any) => {
        xFormOpt.loading = false
        if (err.code !== 1200) return
        xModalDom.value?.close()
        ElMessage.success("操作成功")
        !crudStore.isUpdate && crudStore.afterInsert()
        crudStore.commitQuery()
      }
      if (crudStore.isUpdate) {
        // 调用修改接口
        xFormOpt.data.tags = xFormOpt.data.tags.split(",")
        console.log("调用修改接口", xFormOpt)

        updateDocumentDataApi(xFormOpt.data).then(callback).catch(callback)
      } else {
        // 调用新增接口
        xFormOpt.data.tags = xFormOpt.data.tags.split(",")
        createDocumentDataApi(xFormOpt.data).then(callback).catch(callback)
      }
    })
  },
  /** 新增后是否跳入最后一页 */
  afterInsert: () => {
    const pager: VxeGridPropTypes.ProxyAjaxQueryPageParams = xGridDom.value?.getProxyInfo()?.pager
    if (pager) {
      const currTotal: number = pager.currentPage * pager.pageSize
      if (currTotal === pager.total) {
        ++pager.currentPage
      }
    }
  },
  /** 删除 */
  onDelete: (row: IRowMeta) => {
    const tip = `确定 <strong style='color:red;'>删除</strong> 文档名称 <strong style='color:#409eff;'>${row.docName}</strong> ？`
    const config: ElMessageBoxOptions = {
      type: "warning",
      showClose: true,
      closeOnClickModal: true,
      closeOnPressEscape: true,
      cancelButtonText: "取消",
      confirmButtonText: "确定",
      dangerouslyUseHTMLString: true
    }
    ElMessageBox.confirm(tip, "提示", config)
      .then(() => {
        deleteDocumentDataApi(row.docId)
          .then(() => {
            ElMessage.success("删除成功")
            crudStore.afterDelete()
            crudStore.commitQuery()
          })
          .catch(() => 1)
      })
      .catch(() => 1)
  },
  /** 删除后是否返回上一页 */
  afterDelete: () => {
    const tableData: IRowMeta[] = xGridDom.value!.getData()
    const pager: VxeGridPropTypes.ProxyAjaxQueryPageParams = xGridDom.value?.getProxyInfo()?.pager
    if (pager && pager.currentPage > 1 && tableData.length === 1) {
      --pager.currentPage
    }
  },
  /** 更多自定义方法 */
  moreFunc: () => {}
})
//#endregion
</script>

<template>
  <div class="app-container">
    <!-- 表格 -->
    <vxe-grid ref="xGridDom" v-bind="xGridOpt">
      <!-- 左侧按钮列表 -->
      <template #toolbar-btns>
        <vxe-button status="primary" icon="vxe-icon-add" @click="openDocumentModal(0, '')">新增文档</vxe-button>
        <!-- <vxe-button status="primary" icon="vxe-icon-add" @click="onShowUploadModal()">上传文档</vxe-button> -->
        <vxe-button status="danger" icon="vxe-icon-delete">批量删除</vxe-button>
      </template>
      <template #row-coverImage="{ row }">
        <span>{{ row.coverImage ? row.coverImage : "无" }}</span>
      </template>
      <!-- 操作 -->
      <template #row-operate="{ row }">
        <el-button link type="primary" @click="crudStore.onDownload(row)">下载</el-button>
        <el-button link type="primary" @click="openDocumentModal(1, row)">修改</el-button>
        <el-button link type="danger" @click="crudStore.onDelete(row)">删除</el-button>
      </template>
    </vxe-grid>
    <!-- 弹窗 -->
    <vxe-modal ref="xModalDom" v-bind="xModalOpt">
      <ElUpload
        v-model:file-list="xUploadList"
        class="upload-demo"
        multiple
        :limit="1"
        :action="upLoadUrl"
        :headers="setHeader"
        :on-success="handleOnSuccess"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-exceed="handleExceed"
      >
        <el-button type="primary">上传文件</el-button>
        <template #tip>
          <div class="el-upload__tip" />
        </template>
      </ElUpload>

      <!-- <vxe-form ref="xFormDom" v-bind="xFormOpt" /> -->
    </vxe-modal>
    <vxe-modal ref="xModalUploadDom" v-bind="xModalUpload">
      <!-- :http-request="uploadFile" -->
      <ElUpload
        v-model:file-list="xUploadList"
        class="upload-demo"
        multiple
        :limit="1"
        :action="upLoadUrl"
        :headers="setHeader"
        :on-success="handleOnSuccess"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-exceed="handleExceed"
      >
        <el-button type="primary">上传文件</el-button>
        <template #tip>
          <div class="el-upload__tip" />
        </template>
      </ElUpload>
      <!--  :filter-node-method="filterNode" -->

      <span>文件上传地址:</span>
      <span>{{ xFileDownloadUrl }}</span>
    </vxe-modal>
    <el-dialog v-model="addDocumentModal" :title="documentTitleModal" @close="resetDocumentModal" width="30%">
      <el-form
        v-loading="fileUpLoading"
        ref="documentFormRef"
        :model="addDocumentForm"
        :rules="formRules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="热门文档">
          <el-radio-group v-model="addDocumentForm.hotFlag" class="ml-4">
            <el-radio :label="false" size="large">否</el-radio>
            <el-radio :label="true" size="large">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否免费下载">
          <el-radio-group v-model="addDocumentForm.freeFlag" class="ml-4">
            <el-radio :label="false" size="large">否</el-radio>
            <el-radio :label="true" size="large">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传文档">
          <ElUpload
            ref="upLoadDocument"
            v-model:file-list="xUploadList"
            class="upload-demo"
            multiple
            :limit="1"
            :action="upLoadUrl"
            :headers="setHeader"
            :before-upload="documentUploadClick"
            :on-success="handleOnSuccess"
            :on-error="handleUploadError"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
          >
            <el-button type="primary">上传文件</el-button>
            <template #tip>
              <div class="el-upload__tip" />
            </template>
          </ElUpload>
        </el-form-item>
        <el-form-item label="上传封面" v-if="addDocumentForm.hotFlag">
          <!-- :on-preview="handlePreview"
          :on-exceed="handleExceed" -->

          <ElUpload
            ref="upLoadImg"
            v-model:file-list="xUploadImg"
            class="upload-demo"
            multiple
            :limit="1"
            :action="upLoadImgUrl"
            :headers="setHeader"
            :before-upload="documentUploadClick"
            :on-error="handleUploadError"
            :on-success="upLoadImgSuccess"
            :on-remove="handleRemoveImg"
          >
            <el-button type="primary">上传图片</el-button>
            <template #tip>
              <div class="el-upload__tip" />
            </template>
          </ElUpload>
        </el-form-item>
        <el-form-item label="题目类型" name="categoryId">
          <!-- @change="getTreeCategoryId" -->
          <el-tree-select
            ref="elTree"
            v-model="addDocumentForm.categoryPid"
            :data="treeData"
            :props="defaultProps"
            node-key="categoryId"
            :filter-method="filterNode"
            @node-click="handleNodeClick"
            :highlight-current="true"
            :check-strictly="false"
          />
        </el-form-item>
        <el-form-item label="选择科目">
          <el-select
            v-model="addDocumentForm.categoryId"
            :disabled="subjectList.length === 0 ? true : false"
            @change="selectChange"
          >
            <el-option v-for="item in subjectList" :label="item.categoryName" :value="item.categoryId" />
          </el-select>
        </el-form-item>
        <el-form-item label="文档名称" prop="docName">
          <el-input v-model="addDocumentForm.docName" />
        </el-form-item>
        <el-form-item label="文档标签" prop="tag">
          <el-input v-model="addDocumentForm.tag" placeholder="标签请用','分割'" />
        </el-form-item>
        <el-form-item label="文档类型" prop="type">
          <el-input v-model="addDocumentForm.type" placeholder="上传文件自动获取" :disabled="true" />
        </el-form-item>
        <el-form-item label="文档地址" prop="docDownloadUrl">
          <el-input v-model="addDocumentForm.docDownloadUrl" placeholder="上传文件自动获取" :disabled="true" />
        </el-form-item>
        <el-form-item label="封面地址" prop="coverImage" v-if="addDocumentForm.hotFlag">
          <el-input v-model="addDocumentForm.coverImage" placeholder="上传封面自动获取" :disabled="true" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetDocumentModal">取消</el-button>
        <el-button type="primary" @click="handleAddDocument">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>
