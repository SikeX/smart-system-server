<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="missionName">
              <a-input v-model="model.missionName" placeholder="请输入任务名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="考核年份" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="assessmentYear">
              <a-input v-model="model.assessmentYear" placeholder="请输入考核年份" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="考核时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="assessmentTime">
              <j-date placeholder="请选择考核时间" v-model="model.assessmentTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="总分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalPoint">
              <a-input-number v-model="model.totalPoint" placeholder="请输入总分" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="任务状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="missionStatus">
              <a-input v-model="model.missionStatus" placeholder="请输入任务状态" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="考核要点总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="keyPointsAmount">
              <a-input-number v-model="model.keyPointsAmount" placeholder="请输入考核要点总数" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="考核任务被考核单位" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="smartAssessmentDepartTable.loading"
          :columns="smartAssessmentDepartTable.columns"
          :dataSource="smartAssessmentDepartTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SmartAssessmentMissionForm',
    mixins: [JEditableTableModelMixin],
    components: {
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        model:{
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
           missionName: [
              { required: true, message: '请输入任务名称!'},
           ],
           assessmentYear: [
              { required: true, message: '请输入考核年份!'},
           ],
           assessmentTime: [
              { required: true, message: '请输入考核时间!'},
           ],
        },
        refKeys: ['smartAssessmentDepart', ],
        tableKeys:['smartAssessmentDepart', ],
        activeKey: 'smartAssessmentDepart',
        // 考核任务被考核单位
        smartAssessmentDepartTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '被考核单位',
              key: 'assessmentDepart',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '被考核单位登录账号',
              key: 'departUser',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '截止时间',
              key: 'deadline',
              type: FormTypes.datetime,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '签收状态',
              key: 'signStatus',
              type: FormTypes.input,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '签收时间',
              key: 'signTime',
              type: FormTypes.datetime,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '签收人',
              key: 'signUser',
              type: FormTypes.input,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/smartAssessmentMission/smartAssessmentMission/add",
          edit: "/smartAssessmentMission/smartAssessmentMission/edit",
          queryById: "/smartAssessmentMission/smartAssessmentMission/queryById",
          smartAssessmentDepart: {
            list: '/smartAssessmentMission/smartAssessmentMission/querySmartAssessmentDepartByMainId'
          },
        }
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
    },
    methods: {
      addBefore(){
        this.smartAssessmentDepartTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.smartAssessmentDepart.list, params, this.smartAssessmentDepartTable)
        }
      },
      //校验所有一对一子表表单
      validateSubForm(allValues){
          return new Promise((resolve,reject)=>{
            Promise.all([
            ]).then(() => {
              resolve(allValues)
            }).catch(e => {
              if (e.error === VALIDATE_NO_PASSED) {
                // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
                this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
              } else {
                console.error(e)
              }
            })
          })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          smartAssessmentDepartList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>