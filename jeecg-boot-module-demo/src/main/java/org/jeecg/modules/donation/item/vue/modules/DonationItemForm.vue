<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="项目图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="donationPic">
              <j-image-upload isMultiple  v-model="model.donationPic" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="捐赠项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="donationItemName">
              <a-input v-model="model.donationItemName" placeholder="请输入捐赠项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="donationClass">
              <j-dict-select-tag type="list" v-model="model.donationClass" dictCode="" placeholder="请选择项目分类" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="捐赠项目描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="donationItemDesc">
              <j-editor v-model="model.donationItemDesc" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="捐赠故事" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="donationItemStory">
              <j-editor v-model="model.donationItemStory" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endTime">
              <j-date placeholder="请选择截止日期" v-model="model.endTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="目标金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="targetMoney">
              <a-input v-model="model.targetMoney" placeholder="请输入目标金额"  ></a-input>
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
    name: 'DonationItemForm',
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
          add: "/item/donationItem/add",
          edit: "/item/donationItem/edit",
          queryById: "/item/donationItem/queryById"
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