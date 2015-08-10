<%@ page contentType="text/html;charset=windows-1252"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>untitled</title>
  </head>
  <body>
    <%
      java.util.Vector v=new java.util.Vector();
      for ( int i=1; i<=100000000; i++ ) {
        for (int j=0; j<1000; j++) {
          v.add(new String("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        }
        Thread.sleep(1);
      }
    %>
  
  </body>
</html>
