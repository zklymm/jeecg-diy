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
      <a-button type="primary" icon="download" @click="handleExportXls('行政区划')">导出</a-button>
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

    <region-modal ref="modalForm" @ok="modalFormOk"></region-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import RegionModal from './modules/RegionModal'

  export default {
    name: 'RegionList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      RegionModal
    },
    data () {
      return {
        description: '行政区划管理页面',
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
            title:'行政区编号',
            align:"center",
            dataIndex: 'regionCode'
          },
          {
            title:'行政区名称',
            align:"center",
            dataIndex: 'regionName'
          },
          {
            title:'类型1.省级 2.市级 3.县区 4.镇 5.村',
            align:"center",
            dataIndex: 'regionType'
          },
          {
            title:'城乡分类标识111主城区 112城乡结合区 121镇中心区 122镇乡结合区 123特殊区域 210乡中心区 220村庄',
            align:"center",
            dataIndex: 'countyType'
          },
          {
            title:'上级code',
            align:"center",
            dataIndex: 'parentCode'
          },
          {
            title:'全部上级',
            align:"center",
            dataIndex: 'parentPath'
          },
          {
            title:'全部上级名称',
            align:"center",
            dataIndex: 'parentPathName'
          },
          {
            title:'省级名称',
            align:"center",
            dataIndex: 'proviceName'
          },
          {
            title:'市级名称',
            align:"center",
            dataIndex: 'cityName'
          },
          {
            title:'区县级名称',
            align:"center",
            dataIndex: 'countyName'
          },
          {
            title:'乡镇级名称',
            align:"center",
            dataIndex: 'townName'
          },
          {
            title:'村级名称',
            align:"center",
            dataIndex: 'villageName'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'longitude'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'latitude'
          },
          {
            title:'描述',
            align:"center",
            dataIndex: 'desc'
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
        url: {
          list: "/region/region/list",
          delete: "/region/region/delete",
          deleteBatch: "/region/region/deleteBatch",
          exportXlsUrl: "/region/region/exportXls",
          importExcelUrl: "region/region/importExcel",
          
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
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'regionCode',text:'行政区编号'})
        fieldList.push({type:'string',value:'regionName',text:'行政区名称'})
        fieldList.push({type:'int',value:'regionType',text:'类型1.省级 2.市级 3.县区 4.镇 5.村'})
        fieldList.push({type:'string',value:'countyType',text:'城乡分类标识111主城区 112城乡结合区 121镇中心区 122镇乡结合区 123特殊区域 210乡中心区 220村庄'})
        fieldList.push({type:'string',value:'parentCode',text:'上级code'})
        fieldList.push({type:'string',value:'parentPath',text:'全部上级'})
        fieldList.push({type:'string',value:'parentPathName',text:'全部上级名称'})
        fieldList.push({type:'string',value:'proviceName',text:'省级名称'})
        fieldList.push({type:'string',value:'cityName',text:'市级名称'})
        fieldList.push({type:'string',value:'countyName',text:'区县级名称'})
        fieldList.push({type:'string',value:'townName',text:'乡镇级名称'})
        fieldList.push({type:'string',value:'villageName',text:'村级名称'})
        fieldList.push({type:'string',value:'longitude',text:'经度'})
        fieldList.push({type:'string',value:'latitude',text:'纬度'})
        fieldList.push({type:'string',value:'desc',text:'描述'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>