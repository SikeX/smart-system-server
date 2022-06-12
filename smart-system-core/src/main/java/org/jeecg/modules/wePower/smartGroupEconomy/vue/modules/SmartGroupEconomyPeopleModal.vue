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
            <a-form-model-item label="所在村" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="depart">
              <j-select-depart v-model="model.depart"multi />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name"placeholder="请输入姓名" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="职务" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="job">
              <a-input v-model="model.job"placeholder="请输入职务" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任期开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="termStartDate">
              <j-date placeholder="请选择任期开始时间" v-model="model.termStartDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任期结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="termEndDate">
              <j-date placeholder="请选择任期结束时间" v-model="model.termEndDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="身份证号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idNumber">
              <a-input v-model="model.idNumber"placeholder="请输入身份证号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="照片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pic">
              <j-image-upload isMultiple  v-model="model.pic"></j-image-upload>
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
    name: "SmartGroupEconomyPeopleModal",
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
           depart: [
              { required: true, message: '请输入所在村!'},
           ],
           name: [
              { required: true, message: '请输入姓名!'},
           ],
           job: [
              { required: true, message: '请输入职务!'},
           ],
           termStartDate: [
              { required: true, message: '请输入任期开始时间!'},
           ],
           termEndDate: [
              { required: true, message: '请输入任期结束时间!'},
           ],
           idNumber: [
              { required: true, message: '请输入身份证号!'},
              { pattern: /^\d{6,16}$/, message: '请输入6到16位数字!'},
           ],
           pic: [
              { required: true, message: '请输入照片!'},
           ],
        },
        url: {
          add: "/smartGroupEconomy/smartGroupEconomy/addSmartGroupEconomyPeople",
          edit: "/smartGroupEconomy/smartGroupEconomy/editSmartGroupEconomyPeople",
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
            this.model['mainId'] = this.mainId
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
