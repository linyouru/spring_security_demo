<%--
webupload 上传文件,支持文件分片上传，断点续传
后续添加:
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!--引入CSS-->
    <link rel="stylesheet" type="text/css" href="/webupload/webuploader.css">
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!--引入JS-->
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="/webupload/webuploader.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <title>Title</title>
</head>
<body>
<div id="uploader" class="wu-example">
    <!--用来存放文件信息-->
    <div>
        <h3>大文件分片上传</h3>
    </div>
    <div id="thelist" class="uploader-list"></div>
    <div class="btns">
        <div id="picker">选择文件</div>
        <button id="ctlBtn" class="btn btn-default" onclick="start()">开始上传</button>
        <button id="cancel" class="btn btn-default" onclick="cancel()">取消上传</button>
    </div>
</div>
</body>
<script>
    var fileMd5;
    //监听分块上传过程中的三个时间点
    WebUploader.Uploader.register({
        "before-send-file":"beforeSendFile",
        "before-send":"beforeSend",
        "after-send-file":"afterSendFile",
    },{
        //时间点1：所有分块进行上传之前调用此函数
        beforeSendFile:function(file){
            var deferred = WebUploader.Deferred();
            //1、计算文件的唯一标记，用于断点续传
            (new WebUploader.Uploader()).md5File(file,0,10*1024*1024)
                .progress(function(percentage){
                }).then(function(val){
                    fileMd5=val;
                    //获取文件MD5值后进入下一步
                    deferred.resolve();
                });
            return deferred.promise();
        },

        //时间点2：如果有分块上传，则每个分块上传之前调用此函数
        beforeSend:function(block){
            var deferred = WebUploader.Deferred();
            $.ajax({
                type:"POST",
                url:"/uploadFile/checkChunk",
                data:{
                    //文件唯一标记
                    fileMd5:fileMd5,
                    fileName:block.file.name,
                    //当前分块下标
                    chunk:block.chunk,
                    //当前分块大小
                    chunkSize:block.end-block.start
                },
                dataType: "json",
                success:function(response){
                    if(response.ifExist){
                        //分块存在，跳过
                        deferred.reject();
                    }else{
                        //分块不存在或不完整，重新发送该分块内容
                        deferred.resolve();
                    }
                }
            });
            this.owner.options.formData.fileMd5 = fileMd5;
            return deferred.promise();
        },

        //时间点3：所有分块上传成功后调用此函数
        afterSendFile:function (file) {
            //合并分片
            $.ajax({
                url:"/uploadFile/mergeFile",
                data:{
                    fileName:file.name,
                },
            });
        }
    });


    var $list = $("#thelist");
    var fileIdArray = new Array();    //文件Id数组
    var uploader = WebUploader.create({
        // swf文件路径
        swf:'/webupload/Uploader.swf',
        // 文件接收服务端。
        server: '/uploadFile/webUpload',
        pick: {
            // 选择文件的按钮。可选。
            id:'#picker',
            //是否开起同时选择多个文件能力
            multiple:true,
        },
        //指定接受哪些类型的文件
        accept:{
            //文字描述
            title:"Excel",
            //允许的文件后缀，不带点，多个用逗号分割
            extensions:"txt,xls,xlsx",
            mimeTypes:"text/plain,application/vnd.ms-excel/tapplication/x-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        },
        //是否要分片处理大文件上传,貌似设置文件分片后默认使用文件队列一个个分片上传,虽然用到线程但是不能多文件并发上传
        chunked:true,
        //分片大小
        chunkSize: 5*1024*1024,
        //如果某个分片由于网络问题出错，允许自动重传多少次
        chunkRetry:2,
        //上传并发数。允许同时最大上传进程数
        threads:3,
        //设置为 true 后，不需要手动调用上传，有文件选择即开始上传。
        // auto:true,
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,
        //验证文件总数量, 超出则不允许加入队列
        // fileNumLimit:1,
        //文件上传请求的参数表，每次发送都会发送此对象中的参数
        // formData:{
        //     time: new Date(),
        // },
    });
    // 当有文件被添加进队列的时候
    uploader.on( 'fileQueued', function( file ) {
        fileIdArray.push(file.id);
        $list.append(
            '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<p class="state">等待上传...</p>' +
            '<div class="progress">' +
            '     <div class="progress-bar">' +
            '     </div>' +
            '</div>'+
            '</div>' );
    });
    // 文件上传过程中创建进度条实时显示
    uploader.on( 'uploadProgress', function( file, percentage ) {

        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo( $li ).find('.progress-bar');
        }

        $li.find('p.state').text('上传中');

        $percent.css( 'width', percentage * 100 + '%' );
        $percent.text(Math.round(percentage*100)+'%');
    });
    //当所有文件上传结束时触发
    uploader.on('uploadFinished',function(){
        //上传完成后清空页面
        fileIdArray.forEach(function (id) {
            uploader.removeFile(id,true);
            $( '#'+id ).remove();
        });
        fileIdArray.length = 0;
        $("#ctlBtn").attr("onclick","start()");
        $("#ctlBtn").text("开始上传");
    });
    //上传成功处理
    uploader.on( 'uploadSuccess', function( file ) {
        console.log(file.name+" 上传成功");
        $( '#'+file.id ).find('p.state').text('已上传');
    });
    //上传失败处理
    uploader.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
    });
    //上传传完毕，不管成功失败都会调用该事件，主要用于关闭进度条
    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').remove();
    });
    //当validate不通过时，会以派送错误事件的形式通知调用者
    uploader.on('error',function (type) {
        if(type=='Q_TYPE_DENIED') {
            alert("上传文件格式错误！");
        }
    });

    //开始上传
    function start() {
        uploader.upload();
        $("#ctlBtn").attr("onclick","stop()");
        $("#ctlBtn").text("暂停上传");
    }

    //暂停上传
    function stop() {
        uploader.stop();
        $("#ctlBtn").attr("onclick","start()");
        $("#ctlBtn").text("继续上传");
    }

    //取消上传
    function cancel() {
        if(fileIdArray.length>0){
            fileIdArray.forEach(function (id) {
                uploader.removeFile(id,true);   //移除某一文件, 默认只会标记文件状态为已取消，如果第二个参数为 true 则会从 queue 中移除。
                $( '#'+id ).remove();  //清空页面
            });
            fileIdArray.length = 0;
            $("#ctlBtn").attr("onclick","start()");
            $("#ctlBtn").text("开始上传");
            //后端清除上传的临时文件
            $.ajax({
                //暂不实现
            })
        }
    }
</script>
</html>
