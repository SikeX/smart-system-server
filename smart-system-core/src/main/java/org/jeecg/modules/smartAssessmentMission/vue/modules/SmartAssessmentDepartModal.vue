<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="被考核单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="assessmentDepart">
              <j-select-depart v-model="model.assessmentDepart"multi />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被考核单位登录账号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="departUser">
              <a-input v-model="model.departUser"placeholder="请输入被考核单位登录账号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="截止时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deadline">
              <j-date placeholder="请选择截止时间" v-model="model.deadline" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签收状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signStatus">
              <a-input v-model="model.signStatus"placeholder="请输入签收状态" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签收时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signTime">
              <j-date placeholder="请选择签收时间" v-model="model.signTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签收人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signUser">
              <a-input v-model="model.signUser"placeholder="请输入签收人" disabled></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: "SmartAssessmentDepartModal",
    components: {
    },
    props:{
      mainId:{
        type:String,
        required:false,
        default:''
      }
    },
    data () {
      return {
        title:"操作",
        width:800,
        visible: false,
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
           assessmentDepart: [
              { required: true, message: '请输入被考核单位!'},
           ],
           departUser: [
              { required: true, message: '请输入被考核单位登录账号!'},
           ],
        },
        url: {
          add: "/smartAssessmentMission/smartAssessmentMission/addSmartAssessmentDepart",
          edit: "/smartAssessmentMission/smartAssessmentMission/editSmartAssessmentDepart",
        }

      }
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
      close () {
        this.$emit('close');
        this.visible = false;
        this.$refs.form.clearValidate();
      },
      handleOk () {
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
            this.model['missionId'] = this.mainId
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }else{
             return false
          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>
