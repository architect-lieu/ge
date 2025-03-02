<script lang="ts" setup>
import { type FormInstance, type FormRules, ElMessage, ElMessageBox, ElTreeSelect } from "element-plus"
import { reactive, ref, watch, toRaw, onMounted, defineEmits } from "vue"
import { getCategoryTreeDataApi } from "@/api/category"
const treeData = ref<any>([])
const emits = defineEmits(["emitValue"])
const treeSelectData = ref(0)
const defaultProps = {
  children: "children",
  label: "categoryName"
}
onMounted(() => {
  getCategoryTree()
})
const getCategoryTree = () => {
  getCategoryTreeDataApi()
    .then((res: any) => {
      treeData.value = res.data
    })
    .catch(() => {
      treeData.value = []
    })
}
const emitValue = () => {
  emits("emitValue", treeSelectData)
}
/**tree-select 过滤*/
const filterNode = (value: string, data: any) => {
  console.log(value, data)
  if (!value) return true
  return data.categoryName !== "建筑工程"
}
</script>
<template>
  <el-tree-select
    v-model="treeSelectData"
    :data="treeData"
    :props="defaultProps"
    @change="emitValue"
    node-key="categoryId"
    :filter-node-method="filterNode"
    :highlight-current="true"
    check-strictly
  ></el-tree-select>
</template>
