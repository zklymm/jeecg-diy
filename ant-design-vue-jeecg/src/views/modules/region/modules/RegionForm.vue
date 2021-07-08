<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="行政区编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionCode">
              <a-input v-model="model.regionCode" placeholder="请输入行政区编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="行政区名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionName">
              <a-input v-model="model.regionName" placeholder="请输入行政区名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类型1.省级 2.市级 3.县区 4.镇 5.村" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="regionType">
              <a-input-number v-model="model.regionType" placeholder="请输入类型1.省级 2.市级 3.县区 4.镇 5.村" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="城乡分类标识111主城区 112城乡结合区 121镇中心区 122镇乡结合区 123特殊区域 210乡中心区 220村庄" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="countyType">
              <a-input v-model="model.countyType" placeholder="请输入城乡分类标识111主城区 112城乡结合区 121镇中心区 122镇乡结合区 123特殊区域 210乡中心区 220村庄"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上级code" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parentCode">
              <a-input v-model="model.parentCode" placeholder="请输入上级code"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="全部上级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parentPath">
              <a-input v-model="model.parentPath" placeholder="请输入全部上级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="全部上级名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parentPathName">
              <a-input v-model="model.parentPathName" placeholder="请输入全部上级名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="省级名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="proviceName">
              <a-input v-model="model.proviceName" placeholder="请输入省级名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="市级名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cityName">
              <a-input v-model="model.cityName" placeholder="请输入市级名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="区县级名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="countyName">
              <a-input v-model="model.countyName" placeholder="请输入区县级名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="乡镇级名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="townName">
              <a-input v-model="model.townName" placeholder="请输入乡镇级名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="村级名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="villageName">
              <a-input v-model="model.villageName" placeholder="请输入村级名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="longitude">
              <a-input v-model="model.longitude" placeholder="请输入经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="latitude">
              <a-input v-model="model.latitude" placeholder="请输入纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="desc">
              <a-textarea v-model="model.desc" rows="4" placeholder="请输入描述" />
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
    name: 'RegionForm',
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
           regionCode: [
              { required: true, message: '请输入行政区编号!'},
           ],
           regionName: [
              { required: true, message: '请输入行政区名称!'},
           ],
        },
        url: {
          add: "/region/region/add",
          edit: "/region/region/edit",
          queryById: "/region/region/queryById"
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