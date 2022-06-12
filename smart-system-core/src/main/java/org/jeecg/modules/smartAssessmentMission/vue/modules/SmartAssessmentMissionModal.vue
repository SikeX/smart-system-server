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
            <a-form-model-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="missionName">
              <a-input v-model="model.missionName" placeholder="请输入任务名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="考核年份" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="assessmentYear">
              <a-input v-model="model.assessmentYear" placeholder="请输入考核年份" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="考核时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="assessmentTime">
              <j-date placeholder="请选择考核时间" v-model="model.assessmentTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalPoint">
              <a-input-number v-model="model.totalPoint" placeholder="请输入总分" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="missionStatus">
              <a-input v-model="model.missionStatus" placeholder="请输入任务状态" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="考核要点总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="keyPointsAmount">
              <a-input-number v-model="model.keyPointsAmount" placeholder="请输入考核要点总数" style="width: 100%" disabled/>
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
    name: "SmartAssessmentMissionModal",
    components: { 
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
        url: {
          add: "/smartAssessmentMission/smartAssessmentMission/add",
          edit: "/smartAssessmentMission/smartAssessmentMission/edit",
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