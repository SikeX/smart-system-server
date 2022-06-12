<template>
  <a-card :body-style="{padding: '24px 32px'}" :bordered="false">
    <a-form @submit="handleSubmit" :form="form"  ref="evaluateForm">
      <a-form-item
        label="主管单位"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }">
        <a-input
          v-decorator="[
            'exeDept',
            {rules: [{ required: true, message: '主管单位' }]}
          ]"
          name="exeDept"                 
          placeholder="主管单位" />
      </a-form-item>
      <!--<a-form-item
        label="起止日期"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }">
        <a-range-picker
          name="buildTime"
          style="width: 100%"
          v-decorator="[
            'buildTime',
            {rules: [{ required: true, message: '请选择起止日期' }]}
          ]" />
      </a-form-item>-->
<!--      <a-form-item
      label="政务服务大厅名称"
      :labelCol="{lg: {span: 7}, sm: {span: 7}}"
      :wrapperCol="{lg: {span: 10}, sm: {span: 17} }">
      <a-input
        placeholder="政务服务大厅名称"
        v-decorator="[
            'windowsName',
            {rules: [{ required: true, message: '政务服务大厅名称' }]}
          ]" />
    </a-form-item>-->
      <a-form-item
        label="窗口服务大厅名称"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }">
        <a-input
          placeholder="窗口服务大厅名称"
          v-decorator="[
            'windowsName',
            {rules: [{ required: true, message: '窗口服务大厅名称' }]}
          ]" />
      </a-form-item>
      <a-form-item
        label="人员名称"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }"


      >
        <a-input
          v-decorator="[
        'personName',
        {rules: [{ required: true, message: '人员名称' }]}
        ]" placeholder="人员名称" />
      </a-form-item>
      <a-form-item
        label="评价人姓名"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }"
      >
        <a-input v-decorator="[
            'evaluateName',
            {rules: [{ required: true, message: '请输入评价人姓名' }]}
          ]" placeholder="请输入评价人姓名" />
      </a-form-item>
      <a-form-item
        label="评价人手机号"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }"

      >
        <a-input v-decorator="[
            'evaluatePhone',
            {rules: [{ required: true, message: '请输入评价人手机号' }]}
          ]" placeholder="请输入评价人手机号" />
      </a-form-item>
      <a-form-item label="满意度评价"
                   :labelCol="{lg: {span: 7}, sm: {span: 7}}"
                   :wrapperCol="{lg: {span: 10}, sm: {span: 17} }">
          <span>
            <a-rate v-decorator="['evaluateResult']"
                    :tooltips="desc"
                    />
            </span>
      </a-form-item>

      <a-form-item
        :wrapperCol="{ span: 24 }"
        style="text-align: center"
      >
        <a-button htmlType="submit" type="primary">提交</a-button>
        <a-button style="margin-left: 8px">保存</a-button>
      </a-form-item>
    </a-form>
  </a-card>
</template>
<script>
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  export default {
    name: 'EvaluateForm',
    components: { AFormItem },
    data () {
      return {
        //value: 1,
        evaluateResult: 3,
        desc: ['不满意', '基本满意', '满意', '非常满意', '完全满意'],
        // form
        form: this.$form.createForm(this),

      }
    },
    mounted(){

    },
    methods: {
      //评价分数
      getGrade(evaluateResult){
        let that = this
        if(evaluateResult == 1){
          that.form.setFieldsValue({
            evaluateResult:'2'
          })
        }else if(evaluateResult == 2){
          that.form.setFieldsValue({
            evaluateResult:'4'})
        }else if(evaluateResult == 3){
          that.form.setFieldsValue({
            evaluateResult:'6'})
        }else if(evaluateResult == 4){
          that.form.setFieldsValue({
            evaluateResult:'8'})
        }else if(evaluateResult == 5){
          that.form.setFieldsValue({
            evaluateResult:'10'})
        }
      },

      // handler
      handleSubmit (e) {
        let that = this
        console.log(that.form.getFieldValue('evaluateResult'))
        that.getGrade(that.form.getFieldValue('evaluateResult'))
        console.log(that.form.getFieldValue('evaluateResult'))
        e.preventDefault()
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values)
            this.$emit('result',values)
          }
        })
      }
    }
  }
</script>