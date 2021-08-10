<!DOCTYPE html>
<html lang="en">
<head>
    <title>Table_Simple CSS for HTML tables</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="../../../css/table.css">
</head>
<body onload="iframeHeight()">
<table class="pure-table pure-table-horizontal">
    <thead>
        <tr>
            <td colspan="4">
                <a href="../../regionIndex.html?year=${year!''}&path=99&name=index_${year!''}" target="全国">全国</a>
                <#if parentList??>
                    <#list parentList as region>
                        ><a href="../../regionIndex.html?year=${year!''}&path=${region.regionCode[0..1]}&name=${region.regionCode!''}_${year!''}" target="${region.regionName!''}">${region.regionName!''}</a>
                    </#list>
                </#if>
                <span class="year_flag">${year!''}</span>
            </td>
        </tr>
        <#if regionData??>
            <tr>
                <th colspan="4"><div class="region_name">${regionData.regionName!''}</div>
                行政区划代码：<span class="region_code">${regionData.regionCode!''}</span></th>
            </tr>
        </#if>
        <tr>
            <th>#</th>
            <th>所辖行政区</th>
            <th>行政区划代码</th>
            <th>城乡分类</th>
        </tr>
    </thead>
    <tbody>
        <#if regionList??>
            <#list regionList as region>
                <#if region_index % 2 == 0>
                    <tr>
                        <#if region.regionType == 5>
                            <td>${region_index+1}</td>
                            <td>${region.regionName!''}</td>
                            <td>${region.regionCode!''}</td>
                            <td>${region.countyType!''}</td>
                        <#else>
                            <td>${region_index+1}</td>
                            <td><a href="../../regionIndex.html?year=${year!''}&path=${region.regionCode[0..1]}&name=${region.regionCode!''}_${year!''}" target="${region.regionName!''}">${region.regionName!''}</a></td>
                            <td><a href="../../regionIndex.html?year=${year!''}&path=${region.regionCode[0..1]}&name=${region.regionCode!''}_${year!''}" target="${region.regionName!''}">${region.regionCode!''}</a></td>
                            <td>${region.countyType!''}</td>
                        </#if>
                    </tr>
                <#else>
                    <tr class="pure-table-odd">
                        <#if region.regionType == 5>
                            <td>${region_index+1}</td>
                            <td>${region.regionName!''}</td>
                            <td>${region.regionCode!''}</td>
                            <td>${region.countyType!''}</td>
                        <#else>
                            <td>${region_index+1}</td>
                            <td><a href="../../regionIndex.html?year=${year!''}&path=${region.regionCode[0..1]}&name=${region.regionCode!''}_${year!''}" target="${region.regionName!''}">${region.regionName!''}</a></td>
                            <td><a href="../../regionIndex.html?year=${year!''}&path=${region.regionCode[0..1]}&name=${region.regionCode!''}_${year!''}" target="${region.regionName!''}">${region.regionCode!''}</a></td>
                            <td>${region.countyType!''}</td>
                        </#if>
                    </tr>
                </#if>
            </#list>
        <#else>
            <tr>
                <td colspan="4">暂无数据</td>
            </tr>
        </#if>

    </tbody>
</table>
<script type="text/javascript">
    function iframeHeight(){
        // obj 这里是要获取父页面的iframe对象
        var obj = parent.document.getElementById('iframe_id');
        // 调整父页面的高度为此页面的高度
        obj.height = this.document.body.scrollHeight + 20;
    }
</script>
</body>
</html>
