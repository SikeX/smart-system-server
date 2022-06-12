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
      <a-button @click="handleAdd2" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('陪同用餐人员表')">导出</a-button>
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
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
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

    <smart_8-escorted-meal-modal ref="modalForm" @ok="modalFormOk"></smart_8-escorted-meal-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  // import { mixinDevice } from '@/utils/mixin'
  // import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import Smart_8EscortedMealModal from './Smart_8EscortedMealModal'
  import { deleteAction, getAction,downFile,getFileAccessHttpUrl } from '@/api/manage'
  import Vue from "vue";
  import { filterObj } from '@/utils/util';

  export default {
    name: 'Smart_8EscortedMealList',
    components: {
      Smart_8EscortedMealModal
    },
    data () {
      return {
        description: '陪同用餐人员表管理页面',
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
            title:'姓名',
            align:"center",
            dataIndex: 'name_dictText'
          },
          {
            title:'职务',
            align:"center",
            dataIndex: 'jobTitle'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'department_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        mainId: '',
        url: {
          list: `/smart_8_escorted_meal/smart_8EscortedMeal/getListByMainId`,
          add: "/smart_8_escorted_meal/smart_8EscortedMeal/addByMainId",
          delete: "/smart_8_escorted_meal/smart_8EscortedMeal/delete",
          deleteBatch: "/smart_8_escorted_meal/smart_8EscortedMeal/deleteBatch",
          exportXlsUrl: "/smart_8_escorted_meal/smart_8EscortedMeal/exportXls",
          importExcelUrl: "smart_8_escorted_meal/smart_8EscortedMeal/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
        /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
        queryParam: {},
        /* 数据源 */
        dataSource:[],
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        /* 排序参数 */
        isorter:{
          column: 'createTime',
          order: 'desc',
        },
        /* 筛选参数 */
        filters: {},
        /* table加载状态 */
        loading:false,
        /* table选中keys*/
        selectedRowKeys: [],
        /* table选中records*/
        selectionRows: [],
        /* 查询折叠 */
        toggleSearchStatus:false,
        /* 高级查询条件生效状态 */
        superQueryFlag:false,
        /* 高级查询条件 */
        superQueryParams: '',
        /** 高级查询拼接方式 */
        superQueryMatchType: 'and',
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        console.log(this.mainId)
        var params = this.getQueryParams();//查询条件
        params = Object.assign(params, {mainId: this.mainId})
        this.loading = true;
        console.log(params)
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records||res.result;
            if(res.result.total)
            {
              this.ipagination.total = res.result.total;
            }else{
              this.ipagination.total = 0;
            }
            //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          }else{
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loading = false
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'sel_user',value:'name',text:'姓名'})
        fieldList.push({type:'string',value:'jobTitle',text:'职务',dictCode:''})
        fieldList.push({type:'sel_depart',value:'department',text:'单位'})
        this.superFieldList = fieldList
      },
      edit(record) {
        this.mainId = record.id
        this.loadData()
        console.log(record)
        // this.mainId = record.id
        // this.model.mainId = record.id
      },
      handleAdd2() {
        console.log(this.mainId)
        this.$refs.modalForm.add(this.mainId);
      },
      handleSuperQuery(params, matchType) {
        //高级查询方法
        if(!params){
          this.superQueryParams=''
          this.superQueryFlag = false
        }else{
          this.superQueryFlag = true
          this.superQueryParams=JSON.stringify(params)
          this.superQueryMatchType = matchType
        }
        this.loadData(1)
      },
      getQueryParams() {
        //获取查询条件
        let sqp = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
          sqp['superQueryMatchType'] = this.superQueryMatchType
        }
        var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      getQueryField() {
        //TODO 字段权限控制
        var str = "id,";
        this.columns.forEach(function (value) {
          str += "," + value.dataIndex;
        });
        return str;
      },

      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      searchQuery() {
        this.loadData(1);
      },
      superQuery() {
        this.$refs.superQueryModal.show();
      },
      searchReset() {
        this.queryParam = {}
        this.loadData(1);
      },
      batchDel: function () {
        if(!this.url.deleteBatch){
          this.$message.error("请设置url.deleteBatch属性!")
          return
        }
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  //重新计算分页问题
                  that.reCalculatePage(that.selectedRowKeys.length)
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      handleDelete: function (id) {
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!")
          return
        }
        var that = this;
        deleteAction(that.url.delete, {id: id}).then((res) => {
          if (res.success) {
            //重新计算分页问题
            that.reCalculatePage(1)
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      reCalculatePage(count){
        //总数量-count
        let total=this.ipagination.total-count;
        //获取删除后的分页数
        let currentIndex=Math.ceil(total/this.ipagination.pageSize);
        //删除后的分页数<所在当前页
        if(currentIndex<this.ipagination.current){
          this.ipagination.current=currentIndex;
        }
        console.log('currentIndex',currentIndex)
      },
      handleEdit: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      // 添加审核表单
      handleVerify: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "审核";
        this.$refs.modalForm.disableSubmit = true;
        this.$refs.modalForm.verifyDisableSubmit = false;
      },
      handleAdd: function () {
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        console.log(pagination)
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.loadData();
      },
      handleToggleSearch(){
        this.toggleSearchStatus = !this.toggleSearchStatus;
      },
      // 给popup查询使用(查询区域不支持回填多个字段，限制只返回一个字段)
      getPopupField(fields){
        return fields.split(',')[0]
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData();
        //清空列表选中
        this.onClearSelected()
      },
      handleDetail:function(record){
        // console.log(record)
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title="详情";
        this.$refs.modalForm.disableSubmit = true;
      },
      /* 导出 */
      handleExportXls2(){
        let paramsStr = encodeURI(JSON.stringify(this.getQueryParams()));
        let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?paramsStr=${paramsStr}`;
        window.location.href = url;
      },
      handleExportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        let param = this.getQueryParams();
        if(this.selectedRowKeys && this.selectedRowKeys.length>0){
          param['selections'] = this.selectedRowKeys.join(",")
        }
        console.log("导出参数",param)
        downFile(this.url.exportXlsUrl,param).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        })
      },
      /* 导入 */
      handleImportExcel(info){
        this.loading = true;
        if (info.file.status !== 'uploading') {
          console.log(info.file, info.fileList);
        }
        if (info.file.status === 'done') {
          this.loading = false;
          if (info.file.response.success) {
            // this.$message.success(`${info.file.name} 文件上传成功`);
            if (info.file.response.code === 201) {
              let { message, result: { msg, fileUrl, fileName } } = info.file.response
              let href = window._CONFIG['domianURL'] + fileUrl
              this.$warning({
                title: message,
                content: (<div>
                    <span>{msg}</span><br/>
                    <span>具体详情请 <a href={href} target="_blank" download={fileName}>点击下载</a> </span>
                  </div>
                )
              })
            } else {
              this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
            }
            this.loadData()
          } else {
            this.$message.error(`${info.file.name} ${info.file.response.message}.`);
          }
        } else if (info.file.status === 'error') {
          this.loading = false;
          if (info.file.response.status === 500) {
            let data = info.file.response
            const token = Vue.ls.get(ACCESS_TOKEN)
            if (token && data.message.includes("Token失效")) {
              this.$error({
                title: '登录已过期',
                content: '很抱歉，登录已过期，请重新登录',
                okText: '重新登录',
                mask: false,
                onOk: () => {
                  store.dispatch('Logout').then(() => {
                    Vue.ls.remove(ACCESS_TOKEN)
                    window.location.reload();
                  })
                }
              })
            }
          } else {
            this.$message.error(`文件上传失败: ${info.file.msg} `);
          }
        }
      },
      /* 图片预览 */
      getImgView(text){
        if(text && text.indexOf(",")>0){
          text = text.substring(0,text.indexOf(","))
        }
        return getFileAccessHttpUrl(text)
      },
      /* 文件下载 */
      // update--autor:lvdandan-----date:20200630------for：修改下载文件方法名uploadFile改为downloadFile------
      downloadFile(text){
        if(!text){
          this.$message.warning("未知的文件")
          return;
        }
        if(text.indexOf(",")>0){
          text = text.substring(0,text.indexOf(","))
        }
        let url = getFileAccessHttpUrl(text)
        window.open(url);
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>