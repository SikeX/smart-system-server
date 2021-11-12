<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="主管单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="exeDept">
              <a-input v-model="model.exeDept" placeholder="请输入主管单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="政务服务大厅名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="windowsName">
              <a-input v-model="model.windowsName" placeholder="请输入政务服务大厅名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="人员名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="personName">
              <a-input v-model="model.personName" placeholder="请输入人员名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评价结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="evaluateResult">
              <a-input v-model="model.evaluateResult" placeholder="请输入评价结果"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评价人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="evaluateName">
              <a-input v-model="model.evaluateName" placeholder="请输入评价人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评价人手机号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="evaluatePhone">
              <a-input v-model="model.evaluatePhone" placeholder="请输入评价人手机号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评价时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="evaluateTime">
              <j-date placeholder="请选择评价时间"  v-model="model.evaluateTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
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
    name: 'SmartEvaluateWindowForm',
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
           windowsName: [
              { required: true, message: '请输入政务服务大厅名称!'},
           ],
           evaluatePhone: [
              { required: true, message: '请输入评价人手机号!'},
           ],
        },
        url: {
          add: "/smartEvaluateList/smartEvaluateWindow/add",
          edit: "/smartEvaluateList/smartEvaluateWindow/edit",
          queryById: "/smartEvaluateList/smartEvaluateWindow/queryById"
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