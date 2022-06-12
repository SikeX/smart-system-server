<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="报备人ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="peopleId">
              <a-input v-model="model.peopleId" placeholder="请输入报备人ID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportSex">
              <a-input v-model="model.reportSex" placeholder="请输入性别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="年龄" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportAge">
              <a-input v-model="model.reportAge" placeholder="请输入年龄"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="politics">
              <a-input v-model="model.politics" placeholder="请输入政治面貌"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="工作单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="departId">
              <a-input v-model="model.departId" placeholder="请输入工作单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="职务" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhiwu">
              <a-input v-model="model.zhiwu" placeholder="请输入职务"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="职级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhiji">
              <a-input v-model="model.zhiji" placeholder="请输入职级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phone">
              <a-input v-model="model.phone" placeholder="请输入联系电话"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="逝世人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="theDeadName">
              <a-input v-model="model.theDeadName" placeholder="请输入逝世人姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="与本人关系" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="relation">
              <a-input v-model="model.relation" placeholder="请输入与本人关系"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="葬礼时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="funeralTime">
              <j-date placeholder="请选择葬礼时间" v-model="model.funeralTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="葬礼地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="funeralAddress">
              <a-input v-model="model.funeralAddress" placeholder="请输入葬礼地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="宴请人数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="peopleAccount">
              <a-input v-model="model.peopleAccount" placeholder="请输入宴请人数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="宴请场所名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="groundName">
              <a-input v-model="model.groundName" placeholder="请输入宴请场所名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="宴请场所地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="address">
              <a-input v-model="model.address" placeholder="请输入宴请场所地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="宴请人员范围" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="peopleType">
              <a-input v-model="model.peopleType" placeholder="请输入宴请人员范围"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="葬礼用车数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carAccount">
              <a-input v-model="model.carAccount" placeholder="请输入葬礼用车数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="其中公车数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publicCarAccount">
              <a-input v-model="model.publicCarAccount" placeholder="请输入其中公车数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="不符合规定收受礼金（元）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unusualMoney">
              <a-input v-model="model.unusualMoney" placeholder="请输入不符合规定收受礼金（元）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="不符合规定收受礼品件数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unusualCount">
              <a-input v-model="model.unusualCount" placeholder="请输入不符合规定收受礼品件数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="违规收受礼金礼品处置情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="punishContent">
              <a-input v-model="model.punishContent" placeholder="请输入违规收受礼金礼品处置情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="有否其他违规情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isElse">
              <a-input v-model="model.isElse" placeholder="请输入有否其他违规情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="其他需要说明事项" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="elseState">
              <a-input v-model="model.elseState" placeholder="请输入其他需要说明事项"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="报备时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportTime">
              <j-date placeholder="请选择报备时间"  v-model="model.reportTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
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
    name: 'SmartPostFuneralReportForm',
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
           peopleId: [
              { required: true, message: '请输入报备人ID!'},
           ],
           theDeadName: [
              { required: true, message: '请输入逝世人姓名!'},
           ],
           relation: [
              { required: true, message: '请输入与本人关系!'},
           ],
           funeralTime: [
              { required: true, message: '请输入葬礼时间!'},
           ],
           funeralAddress: [
              { required: true, message: '请输入葬礼地点!'},
           ],
           peopleAccount: [
              { required: true, message: '请输入宴请人数!'},
           ],
           groundName: [
              { required: true, message: '请输入宴请场所名称!'},
           ],
           address: [
              { required: true, message: '请输入宴请场所地址!'},
           ],
           peopleType: [
              { required: true, message: '请输入宴请人员范围!'},
           ],
           carAccount: [
              { required: true, message: '请输入葬礼用车数量!'},
           ],
           publicCarAccount: [
              { required: true, message: '请输入其中公车数量!'},
           ],
           unusualMoney: [
              { required: true, message: '请输入不符合规定收受礼金（元）!'},
           ],
           unusualCount: [
              { required: true, message: '请输入不符合规定收受礼品件数!'},
           ],
           punishContent: [
              { required: true, message: '请输入违规收受礼金礼品处置情况!'},
           ],
           isElse: [
              { required: true, message: '请输入有否其他违规情况!'},
           ],
           reportTime: [
              { required: true, message: '请输入报备时间!'},
           ],
        },
        url: {
          add: "/smartPostFuneralReport/smartPostFuneralReport/add",
          edit: "/smartPostFuneralReport/smartPostFuneralReport/edit",
          queryById: "/smartPostFuneralReport/smartPostFuneralReport/queryById"
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