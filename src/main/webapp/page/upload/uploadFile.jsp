<%--上传文件页面--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div>
        <form id="addOrUpdate" >
            <div class="form-group">
                <label class="col-xs-2 control-label text-right">导入方式:</label>
                <div class="col-xs-10">
                    <div class="radio-inline">
                        <label class="fw-n">
                            <input type="radio"  value="0" name="uploadType"> 增量导入
                        </label>
                    </div>
                    <div class="radio-inline">
                        <label class="fw-n">
                            <input type="radio" value="1" name="uploadType">覆盖导入
                        </label>
                    </div>
                </div>
            </div>
            <%--<div id="namedModel" class="form-group">
                <label class="col-xs-2 control-label text-right">下载模板:</label>
                <div class="col-xs-10">
                    <a id="downLoad" class="btn btn-link" href="downModel/xls/LTE_MRS.xlsx" target="_blank">
                        LTE_MRS.xlsx
                    </a>
                </div>
            </div>--%>
            <div class="form-group">
                <label class="col-xs-2 control-label text-right">选择文件:</label>
                <div class="col-sm-10">
					<span>
                        <div>
                            <div>
                                <span>
                                    <input id="file" name="file" type="file"/>
                                </span>
                            </div>
                                <span>
                                     仅支持xls、xlsx、csv格式的文件上传!!
                                </span>
                        </div>
					</span>
                </div>
            </div>
            <div>
                <div>
                    <button id="startUpload" type="button" onclick="upload()">开始上传</button>
                </div>
            </div>
        </form>
    </div>
</body>
<script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.8.0.js"></script>
<script>
    function upload(){
        var form = new FormData(document.getElementById("addOrUpdate"));
//             var req = new XMLHttpRequest();
//             req.open("post", "${pageContext.request.contextPath}/public/testupload", false);
//             req.send(form);
        $.ajax({
            url:"${pageContext.request.contextPath}/uploadFile/uploadFileByExcelReader",
            type:"post",
            data:form,
            processData:false,
            contentType:false,
            success:function(data){
                alert("上传成功");
            },
            error:function(e){
                alert("上传失败");
            }
        });
    }
</script>
</html>
