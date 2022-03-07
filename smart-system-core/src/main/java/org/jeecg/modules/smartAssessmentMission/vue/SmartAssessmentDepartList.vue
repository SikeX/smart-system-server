<template>
  <a-card :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->
    <div class="table-operator" v-if="mainId">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('考核任务被考核单位')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel">
          <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <smartAssessmentDepart-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId"></smartAssessmentDepart-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SmartAssessmentDepartModal from './modules/SmartAssessmentDepartModal'

  export default {
    name: "SmartAssessmentDepartList",
    mixins:[JeecgListMixin],
    components: { SmartAssessmentDepartModal },
    props:{
      mainId:{
        type:String,
        default:'',
        required:false
      }
    },
    watch:{
      mainId:{
        immediate: true,
        handler(val) {
          if(!this.mainId){
            this.clearList()
          }else{
            this.queryParam['missionId'] = val
            this.loadData(1);
          }
        }
      }
    },
    data () {
      return {
        description: '考核任务表管理页面',
        disableMixinCreated:true,
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'被考核单位',
            align:"center",
            dataIndex: 'assessmentDepart_dictText',
          },
          {
            title:'被考核单位登录账号',
            align:"center",
            dataIndex: 'departUser'
          },
          {
            title:'截止时间',
            align:"center",
            dataIndex: 'deadline'
          },
          {
            title:'签收状态',
            align:"center",
            dataIndex: 'signStatus'
          },
          {
            title:'签收时间',
            align:"center",
            dataIndex: 'signTime'
          },
          {
            title:'签收人',
            align:"center",
            dataIndex: 'signUser'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/smartAssessmentMission/smartAssessmentMission/listSmartAssessmentDepartByMainId",
          delete: "/smartAssessmentMission/smartAssessmentMission/deleteSmartAssessmentDepart",
          deleteBatch: "/smartAssessmentMission/smartAssessmentMission/deleteBatchSmartAssessmentDepart",
          exportXlsUrl: "/smartAssessmentMission/smartAssessmentMission/exportSmartAssessmentDepart",
          importUrl: "/smartAssessmentMission/smartAssessmentMission/importSmartAssessmentDepart",
        },
        dictOptions:{
        },
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl(){
        return `${window._CONFIG['domianURL']}/${this.url.importUrl}/${this.mainId}`;
      }
    },
    methods: {
      clearList(){
        this.dataSource=[]
        this.selectedRowKeys=[]
        this.ipagination.current = 1
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'missionName',text:'任务名称',dictCode:''})
        fieldList.push({type:'string',value:'assessmentYear',text:'考核年份',dictCode:''})
        fieldList.push({type:'datetime',value:'assessmentTime',text:'考核时间'})
        fieldList.push({type:'int',value:'totalPoint',text:'总分',dictCode:''})
        fieldList.push({type:'string',value:'missionStatus',text:'任务状态',dictCode:''})
        fieldList.push({type:'int',value:'keyPointsAmount',text:'考核要点总数',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
