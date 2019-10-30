var totalData=0;//表格数据总数
var page=1; //当前页数
var pageSize=5; //每页显示数据条数
//表格传值
window.onload=function () {
    $.ajax({
        async : false,
        url: "/system/weekSummary/list",
        type: "POST",
        data:{
            page:page,
            pageSize:pageSize
        },
        datatype: "json",
        success: function (res) {
            // var size=res.monthlyPlanExtends.length;
            totalData = res.pp;
            $("tbody").html("");
            $.each(res.weekSummaryExtendDOS, function (index, obj) {
                $("#table_id").append("<tr onmodify='false' id='trOne'>"+
                    "<td name='operate_check_column'><input name='operate_check'type='checkbox' value=''></td>"+
                    "<td name='column' id='"+obj["weekSummaryId"]+"\"'>" + obj["jobNumber"] + "</td>" +
                    "<td name='column'>" + obj["peoName"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_time"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_entry_time"] + "</td>" +
                    "<td><a href='#' id='"+obj["weekSummaryId"]+"\' class='month_a'>查看</a></td>" +
                    "<td id='"+obj["week_summary_code"]+"abc"+"\'>默认</td>"+
                    "<td><input type=\"button\" value=\"审核\" name='"+obj["weekSummaryId"]+"\' class=\"btn btn-primary btn-lg active btn btn-primary btn-sm\" id='btn' onclick='sss(this.name)'/></td>" +
                    // "<td><a href='#' id='week_a' class='week_a'>周计划-总结</a></td>"+
                    // "<td><a href='#' id='day_a' class='day_a'>日计划-总结</a></td>"+
                    "</tr>"
                );
                //判断日志状态
                if(obj["week_summary_state"]==1){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "以保存";
                }else if (obj["week_summary_state"]==2){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "待审核";
                }else if (obj["week_summary_state"]==3){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "请重写";
                }else if (obj["week_summary_state"]==4){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "已审核";
                }
            })
            topage(totalData); //分页
        }
    })
}

//展示层的隐藏与显示
$(document).ready(function() {

    //月展示层
    // a[id='month_a' : 改为属性选择器，而不是id选择器，这样不会让id唯一
    $("a[class='month_a']").click(function(event) {

        var id=$(this).attr("id");
        var category={"id":id};
        var jsonData = JSON.stringify(category);
        $.ajax({
            async:false,
            url:"/system/weekSummary/getWeekSummaryById",
            data:jsonData,
            type:"post",
            datatype:"json",
            contentType : "application/json;charset=UTF-8",
            success:function (res) {
                if (res.code==null){
                    alert("您的权限不足！")
                }else {
                    //取消事件冒泡
                    event.stopPropagation();
                    //设置弹出层位置
                    var offset = $(event.target).offset();
                    $("#divPop").css({ top: offset.top + $(event.target).height() + "px", left: offset.left });
                    //动画显示
                    $("#divPop").toggle('slow');
                    var trSeq = $(this).parent().parent().parent().find("tr").index($(this).parent().parent()[0]);
                    // alert(trSeq)
                    //周总结
                    oneP_summary_week.value = res.weekSummaryExtendDO.week_summary_content;
                    oneP_summaryend_week.value = res.weekSummaryExtendDO.week_summary_entry_time;
                    //周计划
                    oneP_plan_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_content;
                    oneP_end_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_entry_time;
                    //月计划
                    oneP_plan.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_content;
                    oneP_end.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_entry_time;
                }

            }
        })
    });
    //单击空白区域隐藏弹出层
    $(document).click(function(event) { $("#divPop").hide('slow') });

});


// 全选与取消全选
var flag = 1;
function checkedAll(obj) {
    if (flag == 1) {
        obj.value = "取消全选";
        // input:checkbox[name='operate_check'] : 取得选中框的数目
        $("input:checkbox[name='operate_check']").each(function () {
            this.checked = true;
        });
        flag = 2;
    }
    else {
        obj.value = "全选";
        $("input:checkbox[name='operate_check']").each(function () {
            this.checked = false;
        });
        flag = 1;
    }


}
// :checked 自动勾选新添加项
function delSome() {
    $("input[name='operate_check']:checked").each(function () {
        del(this);
    });
}

//各种查询
function monthplanSelect() {
    var peoName = $("#data_text01").val();
    var bmName = $("#data_text02").val();
    var gsName = $("#data_text03").val();
    var time = $("#data_text04").val();
    var totalData=0;//表格数据总数
    var page=1; //当前页数
    var pageSize=5; //每页显示数据条数

//    ajax
    $.ajax({
        url:"/system/weekSummary/compositeQueryWeekSummary",
        type:"post",
        datatype:"json",
        data:{"peoName":peoName, "bmName":bmName, "gsName":gsName, "time":time,"page":page,"pageSize":pageSize},
        success:function (res) {
            totalData = res.pp;
            $("tbody").html("");
            $("#table_id tr").each(function (index,el) {
                if(index>0){
                    $(this).remove();
                }
            });

            $.each(res.weekSummaryExtendDOS, function (index, obj) {
                $("#table_id").append("<tr onmodify='false' id='trOne'>"+
                    "<td name='operate_check_column'><input name='operate_check'type='checkbox' value=''></td>"+
                    "<td name='column' id='"+obj["weekSummaryId"]+"\"'>" + obj["jobNumber"] + "</td>" +
                    "<td name='column'>" + obj["peoName"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_time"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_entry_time"] + "</td>" +
                    "<td><a href='#' id='"+obj["id"]+"\' class='month_a'>查看</a></td>" +
                    "<td id='"+obj["week_summary_code"]+"abc"+"\'>默认</td>"+
                    "<td><input type=\"button\" value=\"审核\" name='"+obj["weekSummaryId"]+"\' class=\"btn btn-primary btn-lg active btn btn-primary btn-sm\" id='btn'  onclick='sss(this.name)'/></td>" +
                    "</tr>"
                );
                //判断日志状态
                if(obj["week_summary_state"]==1){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "以保存";
                }else if (obj["week_summary_state"]==2){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "待审核";
                }else if (obj["week_summary_state"]==3){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "请重写";
                }else if (obj["week_summary_state"]==4){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "已审核";
                }else {
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "错误";
                }
            })
            // $("#size").setAttribute("onclick","monthplanSelect("+size+")");

            //月展示层
            // a[id='month_a' : 改为属性选择器，而不是id选择器，这样不会让id唯一
            $("a[class='month_a']").click(function(event) {

                var id=$(this).attr("id");
                var category={"id":id};
                var jsonData = JSON.stringify(category);
                $.ajax({
                    async:false,
                    url:"/system/weekSummary/getWeekSummaryById",
                    data:jsonData,
                    type:"post",
                    datatype:"json",
                    contentType : "application/json;charset=UTF-8",
                    success:function (res) {
                        if (res.code==null){
                            alert("您的权限不足！")
                        }else {
                            //取消事件冒泡
                            event.stopPropagation();
                            //设置弹出层位置
                            var offset = $(event.target).offset();
                            $("#divPop").css({ top: offset.top + $(event.target).height() + "px", left: offset.left });
                            //动画显示
                            $("#divPop").toggle('slow');
                            var trSeq = $(this).parent().parent().parent().find("tr").index($(this).parent().parent()[0]);
                            // alert(trSeq)
                            //周总结
                            oneP_summary_week.value = res.weekSummaryExtendDO.week_summary_content;
                            oneP_summaryend_week.value = res.weekSummaryExtendDO.week_summary_entry_time;
                            //周计划
                            oneP_plan_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_content;
                            oneP_end_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_entry_time;
                            //月计划
                            oneP_plan.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_content;
                            oneP_end.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_entry_time;
                        }

                    }
                })
            });
            //单击空白区域隐藏弹出层
            $(document).click(function(event) { $("#divPop").hide('slow') });
            queryTopage(totalData,pageSize); //分页

        }
    })
}

function sss(res) {

    $('#cancel[name="4"]').attr("name",res)
    $('#confirm[name="3"]').attr("name",res)
    //显示弹窗的主界面
    $('.pop_main').show()
    //设置animate动画初始值
    $('.pop_con').css({'top':0,'opacity':0})
    $('.pop_con').animate({'top':'50%','opacity':1})

    //取消按钮和关闭按钮添加事件
    $('#cancel').click(function(){
        $('.pop_con').animate({'top':0,'opacity':0})
    })
    //确定按钮关闭
    $("#confirm").click(function () {
        $('.pop_con').animate({'top':0,'opacity':0.8});
        $('.pop_main').hide();
    })


}


//审核ajax
function Get_cancel(res) { //审核通过
    var Get_cancel =4;
    var id =Number(res);
    var category={"id":id,"week_summary_state":Get_cancel};
    var jsonData = JSON.stringify(category);

    $.ajax({
        url:"/system/weekSummary/updateWeekSummaryStateById",
        type:"post",
        data:jsonData,
        datatype:"json",
        contentType : "application/json;charset=UTF-8",
        success:function (data) {
            if (data.code==null){
                alert("您的权限不足！")
            }else {
                if (data.code==0){
                    alert("审核成功！");
                }else {
                    alert("系统异常！");
                }
            }
        }
    })
}
function Get_confirm(res) { //审核不通过
    var Get_cancel =3;
    var id =Number(res);
    var category={"id":id,"week_summary_state":Get_cancel};
    var jsonData = JSON.stringify(category);
    $.ajax({
        url:"/system/weekSummary/updateWeekSummaryStateById",
        type:"post",
        data:jsonData,
        datatype:"json",
        contentType : "application/json;charset=UTF-8",
        success:function (data) {
            if (data.code==null){
                alert("您的权限不足！")
            }else {
                if (data.code==0){
                    alert("审核成功！");
                }else {
                    alert("系统异常！");
                }
            }
        }
    })
}
//回到顶部
var scrollTopSmooth = function (position) {
    // 不存在原生`requestAnimationFrame`，用`setTimeout`模拟替代
    if (!window.requestAnimationFrame) {
        window.requestAnimationFrame = function (cb) {
            return setTimeout(cb, 17);
        };
    }
    // 当前滚动高度
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    // step
    var step = function () {
        var distance = position - scrollTop;
        scrollTop = scrollTop + distance / 5;
        if (Math.abs(distance) < 1) {
            window.scrollTo(0, position);
        } else {
            window.scrollTo(0, scrollTop);
            requestAnimationFrame(step);
        }
    };
    step();
}
$backToTop = document.querySelector('#back-to-top')
$backToTop.addEventListener('click', function () {
    scrollTopSmooth(0);
}, false);

//默认查询每个人的最新的月计划
//分页
function topage(pp) {
    layui.use('laypage',function () {
        var laypage = layui.laypage;
        $(".end").each(function (i,the) {
            laypage.render({
                elem:"page",
                count:totalData,
                limit:pageSize,
                limits:[2,5,15]
                , curr: 1                        //起始页
                , groups: 2                     //连续页码个数
                , prev: '上一页'                 //上一页文本
                , netx: '下一页'                 //下一页文本
                , first: 1                      //首页文本
                , last: (pp+pageSize-1)/pageSize   ,                  //尾页文本
                layout: ['prev', 'page', 'next', 'limit', 'refresh', 'skip'],
                jump:function (obj,first) {
                    var curr = obj.curr;
                    var limit = obj.limit;
                    if (!first){
                        handleData(curr,limit)
                    }
                }
            })

        })
    })

}
function handleData(data1,data2) {
    $.ajax({
        url: "/system/weekSummary/list",
        type: "POST",
        data:{
            page:data1,
            pageSize:data2
        },
        datatype:"json",
        success:function (res) {
            //读取后端数据，更新数据总数供分页使用
            totalData = res.pp;
            $("tbody").html("");
            $.each(res.weekSummaryExtendDOS, function (index, obj) {
                $("#table_id").append("<tr onmodify='false' id='trOne'>"+
                    "<td name='operate_check_column'><input name='operate_check'type='checkbox' value=''></td>"+
                    "<td name='column' id='"+obj["weekSummaryId"]+"\"'>" + obj["jobNumber"] + "</td>" +
                    "<td name='column'>" + obj["peoName"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_time"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_entry_time"] + "</td>" +
                    "<td><a href='#' id='"+obj["weekSummaryId"]+"\' class='month_a'>查看</a></td>" +
                    "<td id='"+obj["week_summary_code"]+"abc"+"\'>默认</td>"+
                    "<td><input type=\"button\" value=\"审核\" name='"+obj["weekSummaryId"]+"\' class=\"btn btn-primary btn-lg active btn btn-primary btn-sm\" id='btn' onclick='sss(this.name)'/></td>" +
                    // "<td><a href='#' id='week_a' class='week_a'>周计划-总结</a></td>"+
                    // "<td><a href='#' id='day_a' class='day_a'>日计划-总结</a></td>"+
                    "</tr>"
                );
                //判断日志状态
                if(obj["week_summary_state"]==1){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "以保存";
                }else if (obj["week_summary_state"]==2){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "待审核";
                }else if (obj["week_summary_state"]==3){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "请重写";
                }else if (obj["week_summary_state"]==4){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "已审核";
                }
            })

            //月展示层
            $("a[class='month_a']").click(function(event) {

                var id=$(this).attr("id");
                var category={"id":id};
                var jsonData = JSON.stringify(category);
                $.ajax({
                    async:false,
                    url:"/system/weekSummary/getWeekSummaryById",
                    data:jsonData,
                    type:"post",
                    datatype:"json",
                    contentType : "application/json;charset=UTF-8",
                    success:function (res) {
                        if (res.code==null){
                            alert("您的权限不足！")
                        }else {
                            //取消事件冒泡
                            event.stopPropagation();
                            //设置弹出层位置
                            var offset = $(event.target).offset();
                            $("#divPop").css({ top: offset.top + $(event.target).height() + "px", left: offset.left });
                            //动画显示
                            $("#divPop").toggle('slow');
                            var trSeq = $(this).parent().parent().parent().find("tr").index($(this).parent().parent()[0]);
                            // alert(trSeq)
                            //周总结
                            oneP_summary_week.value = res.weekSummaryExtendDO.week_summary_content;
                            oneP_summaryend_week.value = res.weekSummaryExtendDO.week_summary_entry_time;
                            //周计划
                            oneP_plan_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_content;
                            oneP_end_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_entry_time;
                            //月计划
                            oneP_plan.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_content;
                            oneP_end.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_entry_time;
                        }

                    }
                })
            });
            //单击空白区域隐藏弹出层
            $(document).click(function(event) { $("#divPop").hide('slow') });
        }

    })
}
//组合查询分页
function queryTopage(totalData,pageSize) {
    layui.use('laypage',function () {
        var laypage = layui.laypage;
        $(".end").each(function (i,the) {
            laypage.render({
                elem:"page",
                count:totalData,
                limit:pageSize,
                limits:[5,10,15]
                , curr: 1                        //起始页
                , groups: 2                     //连续页码个数
                , prev: '上一页'                 //上一页文本
                , next: '下一页'                 //下一页文本
                , first: 1                      //首页文本
                , last: (totalData+pageSize-1)/pageSize   ,                  //尾页文本
                layout: ['prev', 'page', 'next', 'limit', 'refresh', 'skip'],
                jump:function (obj,first) {
                    var curr = obj.curr;
                    var limit = obj.limit;
                    if (!first){
                        queryHandleData(curr,limit)
                    }
                }
            })
        })
    })
}
function queryHandleData(data1,data2) {
    var peoName = $("#data_text01").val();
    var bmName = $("#data_text02").val();
    var gsName = $("#data_text03").val();
    var time = $("#data_text04").val();
//    ajax
    $.ajax({
        url:"/system/weekSummary/compositeQueryWeekSummary",
        type:"post",
        datatype:"json",
        data:{"peoName":peoName, "bmName":bmName, "gsName":gsName, "time":time,"page":data1,"pageSize":data2},
        success:function (res) {
            //读取后端数据，更新数据总数供分页使用
            totalData = res.pp;
            $("tbody").html("");

            $("#table_id tr").each(function (index,el) {
                if(index>0){
                    $(this).remove();
                }
            });
            $.each(res.weekSummaryExtendDOS, function (index, obj) {
                $("#table_id").append("<tr onmodify='false' id='trOne'>"+
                    "<td name='operate_check_column'><input name='operate_check'type='checkbox' value=''></td>"+
                    "<td name='column' id='"+obj["weekSummaryId"]+"\"'>" + obj["jobNumber"] + "</td>" +
                    "<td name='column'>" + obj["peoName"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_time"] + "</td>" +
                    "<td name='column'>" + obj["week_summary_entry_time"] + "</td>" +
                    "<td><a href='#' id='"+obj["id"]+"\' class='month_a'>查看</a></td>" +
                    "<td id='"+obj["week_summary_code"]+"abc"+"\'>默认</td>"+
                    "<td><input type=\"button\" value=\"审核\" name='"+obj["weekSummaryId"]+"\' class=\"btn btn-primary btn-lg active btn btn-primary btn-sm\" id='btn'  onclick='sss(this.name)'/></td>" +
                    "</tr>"
                );
                //判断日志状态
                if(obj["week_summary_state"]==1){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "以保存";
                }else if (obj["week_summary_state"]==2){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "待审核";
                }else if (obj["week_summary_state"]==3){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "请重写";
                }else if (obj["week_summary_state"]==4){
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "已审核";
                }else {
                    document.getElementById(obj["week_summary_code"]+"abc").innerHTML = "错误";
                }
            })
            // $("#size").setAttribute("onclick","monthplanSelect("+size+")");

            //月展示层
            $("a[class='month_a']").click(function(event) {
                var id=$(this).attr("id");
                var category={"id":id};
                var jsonData = JSON.stringify(category);
                $.ajax({
                    async:false,
                    url:"/system/weekSummary/getWeekSummaryById",
                    data:jsonData,
                    type:"post",
                    datatype:"json",
                    contentType : "application/json;charset=UTF-8",
                    success:function (res) {
                        if (res.code==null){
                            alert("您的权限不足！")
                        }else {
                            //取消事件冒泡
                            event.stopPropagation();
                            //设置弹出层位置
                            var offset = $(event.target).offset();
                            $("#divPop").css({ top: offset.top + $(event.target).height() + "px", left: offset.left });
                            //动画显示
                            $("#divPop").toggle('slow');
                            var trSeq = $(this).parent().parent().parent().find("tr").index($(this).parent().parent()[0]);
                            // alert(trSeq)
                            //周总结
                            oneP_summary_week.value = res.weekSummaryExtendDO.week_summary_content;
                            oneP_summaryend_week.value = res.weekSummaryExtendDO.week_summary_entry_time;
                            //周计划
                            oneP_plan_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_content;
                            oneP_end_week.value=res.weekSummaryExtendDO.weekPlanDO.week_plan_entry_time;
                            //月计划
                            oneP_plan.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_content;
                            oneP_end.value=res.weekSummaryExtendDO.monthlyPlan.monthly_plan_entry_time;
                        }

                    }
                })
            });
            //单击空白区域隐藏弹出层
            $(document).click(function(event) { $("#divPop").hide('slow') });
        }
    })
}


