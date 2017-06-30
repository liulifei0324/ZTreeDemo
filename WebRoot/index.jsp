<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">

<title>zTree异步加载&编辑功能共存--DEMO（中国行政区划）</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/zTreeStyle.css">
<script type="text/javascript" src="js/jquery-1.4.4.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/jquery.ztree.core.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/jquery.ztree.excheck.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/jquery.ztree.exedit.js" charset="UTF-8"></script>

<style type="text/css">
	body {
		background-color: #F1FAFA;
		background-image: url(img/china.png);
		background-repeat: no-repeat;
		background-attachment: fixed;
		background-position: 50% 8px;
	}
	.ztree li span.button.add {
		margin-left: 2px;
		margin-right: -1px;
		background-position: -144px 0;
		vertical-align: top;
		*vertical-align: middle
	}
</style>
<script type="text/javascript">
	var setting = {
			async: {
				enable: true,// 设置 zTree 是否开启异步加载模式; 默认值：false
				url:"ZTreeServlet?method=init&timestamp=" + new Date().getTime(),// Ajax 获取数据的 URL 地址。[setting.async.enable = true 时生效]; 默认值：""
				autoParam:["id", "name", "level"],// 异步加载时需要自动提交父节点属性的参数。[setting.async.enable = true 时生效]; 默认值：[ ]
				//otherParam:{"otherParam":"zTreeAsyncTest"},// Ajax 请求提交的静态参数键值对。[setting.async.enable = true 时生效]; 默认值：[ ]
				dataFilter: null// 用于对 Ajax 返回数据进行预处理的函数。[setting.async.enable = true 时生效]; 默认值：null
			},
			view: {expandSpeed:"normal",// zTree 节点展开、折叠时的动画速度，设置方法同 JQuery 动画效果中 speed 参数; IE6 下会自动关闭动画效果，以保证 zTree 的操作速度; 默认值："fast"
				addHoverDom: addHoverDom,// 当鼠标移动到节点上时，显示用户自定义控件
				removeHoverDom: removeHoverDom,// 当鼠标移出节点时，隐藏用户自定义控件
				selectedMulti: false// 设置是否允许同时选中多个节点
			},
			edit: {
				enable: true// 设置 zTree 是否处于编辑状态
			},
			data: {
				simpleData: {
					enable: true// 确定 zTree 初始化时的节点数据、异步加载时的节点数据、或 addNodes 方法中输入的 newNodes 数据是否采用简单数据模式 (Array) 不需要用户再把数据库中取出的 List; 强行转换为复杂的 JSON嵌套格式; 默认值：false
				}
			},
			callback: {
				beforeRemove: beforeRemove,// 用于捕获节点被删除之前的事件回调函数，并且根据返回值确定是否允许删除操作
				beforeRename: beforeRename// 用于捕获节点编辑名称结束（Input 失去焦点 或 按下 Enter 键）之后，更新节点名称数据之前的事件回调函数，并且根据返回值确定是否允许更改名称的操作
			}
		};

		// 用于对 Ajax 返回数据进行预处理的函数
		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		}
		
		// 用于捕获节点被删除之前的事件回调函数，并且根据返回值确定是否允许删除操作
		function beforeRemove(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("area");
			zTree.selectNode(treeNode);
			var wantDelete = confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
			if (!wantDelete) {
				return false;
			}
			//设置条件判断是否能删除
			var canDelete = false;
			switch (treeNode.level) {
			case 1://如果删除的节点是省
				var url = 'ZTreeServlet?method=delProvinceByPid&timestamp=' + new Date().getTime();
				//设置成同步
				$.ajaxSetup({
					async : false
				});
				$.getJSON(url,
						{
							"provId":treeNode.id
						},
						function(result){
							if (result) {
								alert("删除成功！");
								canDelete = true;
							} else {
								alert(treeNode.name + "下不为空，不能删除！");
							}
						}
				)
				
				break;
			case 2://如果删除的节点是市
				var url = 'ZTreeServlet?method=delCityByCid&timestamp=' + new Date().getTime();
				//设置成同步
				$.ajaxSetup({
					async : false
				});
				$.getJSON(url,
						{
							"cityId":treeNode.id
						},
						function(result){
							if (result) {
								alert("删除成功！");
								canDelete = true;
							} else {
								alert(treeNode.name + "下不为空，不能删除！");
							}
						}
				)
				
				break;
			case 3://如果删除的节点是区
				var url = 'ZTreeServlet?method=delDistrictByDid&timestamp=' + new Date().getTime();
				//设置成同步
				$.ajaxSetup({
					async : false
				});
				$.getJSON(url,
						{
							"distId":treeNode.id
						},
						function(result){
							if (result) {
								alert("删除成功！");
								canDelete = true;
							} else {
								alert(treeNode.name + "下不为空，不能删除！");
							}
						}
				)
				
				break;

			default:
				alert("根节点不允许删除！");
				break;
			}
			return canDelete;
		}
		
		// 用于捕获节点编辑名称结束（Input 失去焦点 或 按下 Enter 键）之后，更新节点名称数据之前的事件回调函数，并且根据返回值确定是否允许更改名称的操作
		function beforeRename(treeId, treeNode, newName) {
			if (newName.length == 0) {
				setTimeout(function() {
					var zTree = $.fn.zTree.getZTreeObj("area");
					zTree.cancelEditName();
					alert("节点名称不能为空.");
				}, 0);
				return false;
			}
			//如果名字未变化则不修改
			if (newName == treeNode.name) {
				alert("新名字与旧名字相同，未做修改！");
				return true;
			}
			//开始改名
			//设置条件判断是否能修改
			var canModify = false;
			switch (treeNode.level) {
			case 1://如果改名的节点是省
				var url = 'ZTreeServlet?method=updateProvName&timestamp=' + new Date().getTime();
				//设置成同步
				$.ajaxSetup({
					async : false
				});
				$.getJSON(url,
						{
							"provinceid":treeNode.id,
							"provincename":newName
						},
						function(result){
							if (result) {
								alert("修改成功！");
								canModify = true;
							} else {
								alert("修改失败，名字不能超过50个中文字符！");
							}
						}
				)
				
				break;
			case 2://如果改名的节点是市
				var url = 'ZTreeServlet?method=updateCityName&timestamp=' + new Date().getTime();
				//设置成同步
				$.ajaxSetup({
					async : false
				});
				$.getJSON(url,
						{
							"cityid":treeNode.id,
							"cityname":newName
						},
						function(result){
							if (result) {
								alert("修改成功！");
								canModify = true;
							} else {
								alert("修改失败，名字不能超过50个中文字符！");
							}
						}
				)
				
				break;
			case 3://如果改名的节点是区
				var url = 'ZTreeServlet?method=updateDistName&timestamp=' + new Date().getTime();
				//设置成同步
				$.ajaxSetup({
					async : false
				});
				$.getJSON(url,
						{
							"districtid":treeNode.id,
							"districtname":newName
						},
						function(result){
							if (result) {
								alert("修改成功！");
								canModify = true;
							} else {
								alert("修改失败，名字不能超过50个中文字符！");
							}
						}
				)
				
				break;

			default:
				alert("根节点不允许修改名称！");
				break;
			}
			return canModify;
		}

		// 当鼠标移动到节点上时，显示用户自定义控件
		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='add node' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			// 为btn绑定点击事件
			if (btn) btn.bind("click", function(){//下面开始添加节点
				var zTree = $.fn.zTree.getZTreeObj("area");
				switch (treeNode.level) {
				case 0://如果添加的节点是省
					var newNodeName = window.prompt("请输入省名","省名");
					while(newNodeName == null || $.trim(newNodeName) == ""){
						alert("名称不能为空！");
						newNodeName = window.prompt("请输入省名","省名");
					}
					var url = 'ZTreeServlet?method=addProvince&timestamp=' + new Date().getTime();
					$.getJSON(url,
							{
								"provincename":newNodeName
							},
							function(pId){
								zTree.addNodes(treeNode, {id:pId, pId:treeNode.id, name:newNodeName, icon:"css/img/diy/sheng.png"});
							}
					)
					
					break;
				case 1://如果添加的节点是市
					var newNodeName = window.prompt("请输入市名","市名");
					while(newNodeName == null || $.trim(newNodeName) == ""){
						alert("名称不能为空！");
						newNodeName = window.prompt("请输入市名","市名");
					}
					var url = 'ZTreeServlet?method=addCity&timestamp=' + new Date().getTime();
					$.getJSON(url,
							{
								"cityname":newNodeName,
								"provinceid":treeNode.id
							},
							function(cId){
								zTree.addNodes(treeNode, {id:cId, pId:treeNode.id, name:newNodeName, icon:"css/img/diy/shi.png"});
							}
					)
					
					break;
				case 2://如果添加的节点是区
					var newNodeName = window.prompt("请输入区名","区名");
					while(newNodeName == null || $.trim(newNodeName) == ""){
						alert("名称不能为空！");
						newNodeName = window.prompt("请输入区名","区名");
					}
					var url = 'ZTreeServlet?method=addDistrict&timestamp=' + new Date().getTime();
					$.getJSON(url,
							{
								"districtname":newNodeName,
								"cityid":treeNode.id
							},
							function(dId){
								zTree.addNodes(treeNode, {id:dId, pId:treeNode.id, name:newNodeName, icon:"css/img/diy/qu.png"});
							}
					)
					
					break;

				default:
					alert("数据库中无对应表，不能添加新节点！");
					break;
				}
				return false;
			});
		};
		
		// 当鼠标移出节点时，隐藏用户自定义控件
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
		};

	$(document).ready(function() {
		$.fn.zTree.init($("#area"), setting);
	});
</script>
</head>

<body>
	
	<div>
		<ul id="area" class="ztree"></ul>
	</div>
</body>
</html>
