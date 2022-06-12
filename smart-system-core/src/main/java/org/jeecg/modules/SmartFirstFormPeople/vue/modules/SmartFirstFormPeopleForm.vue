<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="案件标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="caseName">
              <a-input v-model="model.caseName" placeholder="请输入案件标题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="案件（线索来源）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="caseSource">
              <a-textarea v-model="model.caseSource" rows="4" placeholder="请输入案件（线索来源）" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人工号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweeNo">
              <a-input v-model="model.intervieweeNo" placeholder="请输入被谈话人工号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweeDept">
              <a-input v-model="model.intervieweeDept" placeholder="请输入被谈话人单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweeName">
              <a-input v-model="model.intervieweeName" placeholder="请输入被谈话人姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人性别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweeSex">
              <a-input v-model="model.intervieweeSex" placeholder="请输入被谈话人性别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人民族" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweeEthnicity">
              <a-input v-model="model.intervieweeEthnicity" placeholder="请输入被谈话人民族"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweePolsta">
              <a-input v-model="model.intervieweePolsta" placeholder="请输入被谈话人政治面貌"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人入党时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweeJpt">
              <a-input v-model="model.intervieweeJpt" placeholder="请输入被谈话人入党时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人职务" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweePost">
              <a-input v-model="model.intervieweePost" placeholder="请输入被谈话人职务"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="被谈话人职级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="intervieweePostrank">
              <a-input v-model="model.intervieweePostrank" placeholder="请输入被谈话人职级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否党政正职" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="principal">
              <j-dict-select-tag type="radio" v-model="model.principal" dictCode="yn" placeholder="请选择是否党政正职" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否国家检查队形" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="country">
              <j-dict-select-tag type="radio" v-model="model.country" dictCode="yn" placeholder="请选择是否国家检查队形" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="干部管理权限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="authority">
              <a-input v-model="model.authority" placeholder="请输入干部管理权限"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否是纪检监察干部" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervision">
              <j-dict-select-tag type="radio" v-model="model.supervision" dictCode="yn" placeholder="请选择是否是纪检监察干部" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="谈话人工号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="talkerNo">
              <a-input v-model="model.talkerNo" placeholder="请输入谈话人工号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="谈话人单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="talkerDept">
              <a-input v-model="model.talkerDept" placeholder="请输入谈话人单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="谈话人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="talkerName">
              <a-input v-model="model.talkerName" placeholder="请输入谈话人姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="谈话人职务" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="talkerPost">
              <a-input v-model="model.talkerPost" placeholder="请输入谈话人职务"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="谈话人职级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="talkerPostrank">
              <a-input v-model="model.talkerPostrank" placeholder="请输入谈话人职级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="简要案情" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="caseAbs">
              <a-textarea v-model="model.caseAbs" rows="4" placeholder="请输入简要案情" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="办理部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handlerDepart">
              <j-select-depart v-model="model.handlerDepart" multi  />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="办理状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="state">
              <a-input v-model="model.state" placeholder="请输入办理状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <a-input v-model="model.type" placeholder="请输入类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="情形" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="situation">
              <a-input v-model="model.situation" placeholder="请输入情形"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="谈话时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="talkTime">
              <j-date placeholder="请选择谈话时间" v-model="model.talkTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="组织措施" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="measures">
              <a-input v-model="model.measures" placeholder="请输入组织措施"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="采取组织措施决定机关" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="decisionOrgan">
              <a-input v-model="model.decisionOrgan" placeholder="请输入采取组织措施决定机关"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="annex">
              <j-upload v-model="model.annex"   ></j-upload>
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
    name: 'SmartFirstFormPeopleForm',
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
           annex: [
              { required: true, message: '请输入附件!'},
           ],
        },
        url: {
          add: "/SmartFirstFormPeople/smartFirstFormPeople/add",
          edit: "/SmartFirstFormPeople/smartFirstFormPeople/edit",
          queryById: "/SmartFirstFormPeople/smartFirstFormPeople/queryById"
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