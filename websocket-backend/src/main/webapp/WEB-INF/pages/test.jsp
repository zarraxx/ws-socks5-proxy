<%--
  Created by IntelliJ IDEA.
  User: xiashenpin
  Date: 15/9/26
  Time: 上午11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
  <script lang="javascript">


    var createConnect = function(url){
      var sock = new WebSocket(url);
      sock.onopen = function() {
        console.log('open');
        //sock.send([])
      };
      sock.onmessage = function(e) {
        console.log('message', e.data);
      };
      sock.onclose = function() {
        console.log('close');
      };

      return sock;
    }

    createConnect('ws://218.80.1.144:18000/proxy2/bridge')

    //sock.send('test');
    //sock.close();
  </script>
</head>
<body>

</body>
</html>
