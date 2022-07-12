<!DOCTYPE html>
<html>
  <head>
    <title>Service Request</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" rel="stylesheet">
    <style>
      html, body {
        display: flex;
        justify-content: center;
        font-family: Roboto, Arial, sans-serif;
        font-size: 15px;
      }
      form {
        border: 5px solid #f1f1f1;
      }
      input[type=text], input[type=password] {
        width: 100%;
        padding: 16px 8px;
        margin: 8px 0;
        display: inline-block;
        border: 1px solid #ccc;
        box-sizing: border-box;
      }
      button {
        background-color: #0d4a9a;
        color: white;
        padding: 14px 0;
        margin: 10px 0;
        border: none;
        cursor: grabbing;
        width: 100%;
      }
      h1 {
        text-align:center;
        fone-size:18;
      }
      button:hover {
        opacity: 0.8;
      }
      .formcontainer {
        text-align: left;
        margin: 24px 50px 12px;
      }
      .container {
        padding: 16px 0;
        text-align:left;
      }
      span.psw {
        float: right;
        padding-top: 0;
        padding-right: 15px;
      }
      table {
        border-collapse: collapse;
        border: 1px solid #f1f1f1;
        width: 100%;
      }
      th, td {
        border: 1px solid #f1f1f1;
      }
      /* Change styles for span on extra small screens */
      @media screen and (max-width: 300px) {
        span.psw {
        display: block;
        float: none;
        }
      }
    </style>
  </head>
  <body>
    <?php
        $host    = "localhost";
        $user    = "keyworld";
        $pass    = "keyworld";
        $db_name = "keyworlddb";

        //create connection
        mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
        $connection = mysqli_connect($host, $user, $pass, $db_name);

        //check connection
        if ($connection->connect_error) {
          die("Connection failed: " . $connection->connect_error);
        }

        //get results from database
        $result = $connection->query("SELECT * FROM reviews");
        $all_property = array();  //declare an array for saving property

        //showing property
        echo '<table>
                <tr>';  //initialize table tag
        while ($property = $result->fetch_field()) {
            echo '<th>' . $property->name . '</th>';  //get field name for header
            $all_property[] = $property->name;  //save those to array
        }
        echo '</tr>'; //end tr tag

        //showing all data
        while ($row = mysqli_fetch_array($result)) {
            echo "<tr>";
            foreach ($all_property as $item) {
                echo '<td>' . $row[$item] . '</td>'; //get items using property value
            }
            echo '</tr>';
        }
        echo "</table>";
    ?>

  </body>
</html>
