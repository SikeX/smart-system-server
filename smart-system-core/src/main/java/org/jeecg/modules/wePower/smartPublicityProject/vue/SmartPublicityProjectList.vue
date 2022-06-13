<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('项目管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
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
        class="j-table-force-nowrap"
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
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <smart-publicity-project-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SmartPublicityProjectModal from './modules/SmartPublicityProjectModal'
  import { loadCategoryData } from '@/api/api'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "SmartPublicityProjectList",
    mixins:[JeecgListMixin],
    components: {
      SmartPublicityProjectModal
    },
    data () {
      return {
        description: '项目管理管理页面',
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
            title:'项目名称',
            align:"center",
            dataIndex: 'title'
          },
          {
            title:'项目分类',
            align:"center",
            dataIndex: 'type',
            customRender: (text) => (text ? filterMultiDictText(this.dictOptions['type'], text) : '')
          },
          {
            title:'建设单位',
            align:"center",
            dataIndex: 'location_dictText'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'创建部门',
            align:"center",
            dataIndex: 'sysOrgCode'
          },
          {
            title:'参会人员',
            align:"center",
            dataIndex: 'people1'
          },
          {
            title:'会议照片',
            align:"center",
            dataIndex: 'video1'
          },
          {
            title:'参会人员',
            align:"center",
            dataIndex: 'people2'
          },
          {
            title:'会议照片',
            align:"center",
            dataIndex: 'video2'
          },
          {
            title:'参会人员',
            align:"center",
            dataIndex: 'people3'
          },
          {
            title:'会议照片',
            align:"center",
            dataIndex: 'video3'
          },
          {
            title:'参会人员',
            align:"center",
            dataIndex: 'people4'
          },
          {
            title:'会议照片',
            align:"center",
            dataIndex: 'video4'
          },
          {
            title:'村两委会议商议',
            align:"center",
            dataIndex: 'meetFile2'
          },
          {
            title:'党员大会审议',
            align:"center",
            dataIndex: 'meetFile3'
          },
          {
            title:'村民会议或者村民代表会议决议',
            align:"center",
            dataIndex: 'meetFile4'
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
          list: "/smartPublicityProject/smartPublicityProject/list",
          delete: "/smartPublicityProject/smartPublicityProject/delete",
          deleteBatch: "/smartPublicityProject/smartPublicityProject/deleteBatch",
          exportXlsUrl: "/smartPublicityProject/smartPublicityProject/exportXls",
          importExcelUrl: "smartPublicityProject/smartPublicityProject/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        loadCategoryData({code:"B03"}).then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'type', res.result)
          }
        })
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'title',text:'项目名称',dictCode:''})
        fieldList.push({type:'string',value:'type',text:'项目分类'})
        fieldList.push({type:'sel_depart',value:'location',text:'建设单位'})
        fieldList.push({type:'string',value:'constructDep',text:'施工单位',dictCode:''})
        fieldList.push({type:'Text',value:'projectContent',text:'简要说明',dictCode:''})
        fieldList.push({type:'string',value:'money',text:'合同金额',dictCode:''})
        fieldList.push({type:'string',value:'period',text:'服务年限',dictCode:''})
        fieldList.push({type:'date',value:'endTime',text:'完成时限'})
        fieldList.push({type:'date',value:'signTime',text:'合同签订日期'})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'datetime',value:'createTime',text:'创建日期'})
        fieldList.push({type:'string',value:'sysOrgCode',text:'创建部门',dictCode:'sys_depart,depart_name,org_code'})
        fieldList.push({type:'Text',value:'meetFile1',text:'村党支部提议文件',dictCode:''})
        fieldList.push({type:'Text',value:'people1',text:'参会人员',dictCode:''})
        fieldList.push({type:'Text',value:'video1',text:'会议照片',dictCode:''})
        fieldList.push({type:'Text',value:'file2',text:'村集体经济组织相关材料',dictCode:''})
        fieldList.push({type:'Text',value:'people2',text:'参会人员',dictCode:''})
        fieldList.push({type:'Text',value:'video2',text:'会议照片',dictCode:''})
        fieldList.push({type:'Text',value:'file3',text:'合同',dictCode:''})
        fieldList.push({type:'Text',value:'people3',text:'参会人员',dictCode:''})
        fieldList.push({type:'Text',value:'video3',text:'会议照片',dictCode:''})
        fieldList.push({type:'Text',value:'file4',text:'验收材料',dictCode:''})
        fieldList.push({type:'Text',value:'people4',text:'参会人员',dictCode:''})
        fieldList.push({type:'Text',value:'video4',text:'会议照片',dictCode:''})
        fieldList.push({type:'Text',value:'meetFile2',text:'村两委会议商议',dictCode:''})
        fieldList.push({type:'Text',value:'meetFile3',text:'党员大会审议',dictCode:''})
        fieldList.push({type:'Text',value:'meetFile4',text:'村民会议或者村民代表会议决议',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>