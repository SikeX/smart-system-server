<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="问题ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="questionId">
              <j-multi-select-tag type="list_multi" v-model="model.questionId" dictCode="smart_tripeo_question,question,id" placeholder="请选择问题ID" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taskName">
              <a-input v-model="model.taskName" placeholder="请输入任务名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taskType">
              <a-input v-model="model.taskType" placeholder="请输入类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="牵头领导" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="leader">
              <a-input v-model="model.leader" placeholder="请输入牵头领导"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="牵头部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="leadDept">
              <a-input v-model="model.leadDept" placeholder="请输入牵头部门"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="责任部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dutyDept">
              <a-input v-model="model.dutyDept" placeholder="请输入责任部门"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="具体任务" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taskDesc">
              <a-input v-model="model.taskDesc" placeholder="请输入具体任务"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="完成时限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endTime">
              <j-date placeholder="请选择完成时限" v-model="model.endTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否完成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isComplete">
              <j-dict-select-tag type="radio" v-model="model.isComplete" dictCode="" placeholder="请选择是否完成" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SmartTripeoTaskForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/SmartTripeoTask/smartTripeoTask/add",
          edit: "/SmartTripeoTask/smartTripeoTask/edit",
          queryById: "/SmartTripeoTask/smartTripeoTask/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>