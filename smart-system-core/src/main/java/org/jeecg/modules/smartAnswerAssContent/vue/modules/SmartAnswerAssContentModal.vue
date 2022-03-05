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
            <a-form-model-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pid">
              <a-input v-model="model.pid" placeholder="请输入父级节点" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否有子节点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hasChild">
              <a-input v-model="model.hasChild" placeholder="请输入是否有子节点" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="考核内容节点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="assContentId">
              <a-input v-model="model.assContentId" placeholder="请输入考核内容节点" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="要点状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contentStatus">
              <a-input v-model="model.contentStatus" placeholder="请输入要点状态" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最低得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lowestScore">
              <a-input-number v-model="model.lowestScore" placeholder="请输入最低得分" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最高得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="highestScore">
              <a-input-number v-model="model.highestScore" placeholder="请输入最高得分" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="averageScore">
              <a-input-number v-model="model.averageScore" placeholder="请输入平均得分" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最终得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="finalScore">
              <a-input-number v-model="model.finalScore" placeholder="请输入最终得分" style="width: 100%" />
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
    name: "SmartAnswerAssContentModal",
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
        },
        url: {
          add: "/smartAnswerAssContent/smartAnswerAssContent/add",
          edit: "/smartAnswerAssContent/smartAnswerAssContent/edit",
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