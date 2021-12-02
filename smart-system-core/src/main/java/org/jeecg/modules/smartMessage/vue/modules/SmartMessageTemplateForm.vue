<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-input v-model="model.title" placeholder="请输入标题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="优先级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="priority">
              <a-input v-model="model.priority" placeholder="请输入优先级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="摘要" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abstract">
              <a-input v-model="model.abstract" placeholder="请输入摘要"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-input v-model="model.content" placeholder="请输入内容"  ></a-input>
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
    name: 'SmartMessageTemplateForm',
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
           title: [
              { required: true, message: '请输入标题!'},
           ],
           priority: [
              { required: true, message: '请输入优先级!'},
           ],
           abstract: [
              { required: true, message: '请输入摘要!'},
           ],
           content: [
              { required: true, message: '请输入内容!'},
           ],
        },
        url: {
          add: "/smartMessageTemplate/smartMessageTemplate/add",
          edit: "/smartMessageTemplate/smartMessageTemplate/edit",
          queryById: "/smartMessageTemplate/smartMessageTemplate/queryById"
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