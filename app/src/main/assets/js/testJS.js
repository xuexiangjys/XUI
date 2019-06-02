//测试JS调用Android方法
function sendHelloToAndroid() {
  if (window.Android != null && typeof (window.Android) != "undefined") {
    window.Android.callAndroid("你好，Android! ");
  } else {
    alert(typeof (window.android));
  }
}

//测试方法1 : 无参方法
function callByAndroidNoParam() {
  console.log("callByAndroidNoParam")
  alert("Js收到消息");
  showElement("Js收到消息-->无参方法callByAndroidNoParam被调用");
}

//测试方法2 : 一个参方法
function callByAndroidOneParam(msg1) {
  console.log("callByAndroidOneParam")
  alert("Js收到消息：" + msg1);
  showElement("Js收到消息-->方法callByAndroidOneParam被调用,参数:" + msg1);
}

//测试方法3 : 多个参方法
function callByAndroidMoreParams(obj, msg2, msg3) {
  alert("Js收到消息：" + "id:" + obj.id.toString() + " name:" + obj.name + " age:" + obj.age.toString() + msg2 + msg3);
  showElement("Js收到消息-->方法callByAndroidMoreParams被调用 , 参数1:" + obj + "  参数2:" + msg2 + "  参数3:" + msg3);
  return "ok";
}

function clearLog() {
  var div = document.getElementById("div_box"); //获取div
  div.innerHTML = "";
}


function showElement(msg) {
  var div = document.getElementById("div_box"); //获取div
  var ele = document.createElement('h6'); //创建h2元素节点
  ele.innerHTML = msg; //设置h2节点的内容
  div.appendChild(ele); //添加子节点ele
}