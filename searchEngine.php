<?php
    header("Access-Control-Allow-Origin: *");
    include 'SpellCorrector.php';
    ini_set('memory_limit','-1');
    // make sure browsers see this page as utf-8 encoded HTML
     $file = fopen("/home/sanika/public_html/hash.csv","r");
    $myArray = array();
    $newArray = array();
    while(! feof($file))
    {
        $myArray = fgetcsv($file);
        $key = $myArray[0];
        $value = $myArray[1];
        $newArray[$value]=$key;
        
    }
    fclose($file);
    header('Content-Type: text/html; charset=utf-8');
    $limit = 10000;
    $query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;  
   $results = false;
    
    if ($query)
    {
        // The Apache Solr Client library should be on the include path
        // which is usually most easily accomplished by placing in the
        // same directory as this script ( . or current directory is a default // php include path entry in the php.ini)
        require_once('/home/sanika/Downloads/solr-php-client-master/Apache/Solr/Service.php');
        // create a new solr service instance - host, port, and corename
        // path (all defaults in this example)
        $solr = new Apache_Solr_Service('localhost', 8983, '/solr/myexample/');
        // if magic quotes is enabled then stripslashes will be needed
        if (get_magic_quotes_gpc() == 1) {
        $query = stripslashes($query); 
        }
        // in production code you'll always want to use a try /catch for any // possible exceptions emitted by searching (i.e. connection
        // problems or a query parsing error)
	
	$newquery = "";
    $checkField = isset($_REQUEST['checkField']) ? $_REQUEST['checkField'] : false;
	$splitQuery = array();
    $splitQuery = explode(" ",$query);
    foreach($splitQuery as $term){
        $cor = SpellCorrector::correct($term);
        $correct = $correct . $cor . " ";
    }
    $correct = trim($correct);
	if($checkField != "YES"){
    $splitAgain = array();
    $splitAgain = explode(" ",$query);
    foreach($splitAgain as $terms){
        $correctWord = SpellCorrector::correct($terms);
        $newquery = $newquery . $correctWord . " ";
    }
    $newquery = trim($newquery);
    }else{
	$newquery = $query;
	}
	$flag = 0;
	if(strcmp($newquery,$query)!=0 && $checkField !="YES"){
		$flag = 1;
	}
	if(strcmp($newquery,$query)==0 && $checkField == "YES"){
		$flag = 2;	
	}
        
        if(isset($_REQUEST['pagerank']) && $_REQUEST['pagerank'] == "YES" ){
        
                try{

                    $results = $solr->search($newquery, 0, $limit, array('sort'=>'pageRankFile desc'));
                }
                catch (Exception $e) {
                    // in production you'd probably log or email this error to an admin
                    // and then show a special message to the user but for this example
                    // we're going to show the full exception
                    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
                }
        }else{
            
                try{
                    $results = $solr->search($newquery, 0, $limit);
                }
                catch (Exception $e) {
                    // in production you'd probably log or email this error to an admin
                    // and then show a special message to the user but for this example
                    // we're going to show the full exception
                    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
                }
      }
    }
?> 
<html>
    <head>
      <title>PHP Solr Client Example</title>
      <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
      <link rel="stylesheet" href="/resources/demos/style.css">
      <script src="//code.jquery.com/jquery-1.10.2.js"></script>
      <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>	
       <script type="text/javascript">

	function submitForm(){
	  var getvalue = document.getElementById("correctQuery");
	  if(getvalue){
	  var correctQuery = getvalue.text;
	   }
	  var docnew = document.getElementById("query");
	  if(docnew){
	  var query = docnew.text;}
	  if( correctQuery != null){
	  var getId = document.getElementById("q");
          getId.value = correctQuery;
	  }
	  else if(query != null){
	  var setId = document.getElementById("q");
	  setId.value = query;
	  }
	  document.myform.submit();
	}

	function resubmitForm(){
	 document.getElementById('checkField').value = "YES";
         document.myform.submit(); 

	}
        $(document).ready(function(){
        $('#q').autocomplete({
          source: function(request,callback){
             var value = $('#q').val();
              value = value.trim();
              value = value.toLowerCase();
             var values = [];
             $.ajax({
                 type : 'GET',
                 data : { "query" : value },
                 url : "retrieveData.php",
                 success: function (response) {
                 var obj = JSON.parse(response);
                callback(obj);
             },
                 error: function(response){
                     alert(JSON.stringify(response));
                 }
        });
        }    
        });
        });
           
    </script> 	
    </head> 
    <body>
    <form accept-charset="utf-8" method="post" autocomplete="off" name="myform">
        <label for="q">Search:</label>
        <input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query,ENT_NOQUOTES,'utf-8');?>"/> 
	<input type="hidden" name="checkField" id="checkField"/>
        <input type="submit"/><br/>
        <input type="radio" name="pagerank" value="YES" <?php if(isset($_POST['pagerank']) && ($_POST['pagerank'] == "YES") ){ echo "checked"; } else if(!isset($_POST['pagerank'])){ echo "checked";} ?>/>Sort using pagerank
        <input type="radio" name="pagerank" value="NO" <?php if(isset($_POST['pagerank']) && ($_POST['pagerank'] == "NO") ){ echo "checked"; }?>/> No pagerank
    </form> 
    <?php
		if($flag == 1){
	?>
	Showing results for <a id="correctQuery" href="javascript:submitForm()" value="<?php echo $newquery?>"><I><?php echo $newquery?></I></a><br>
	Search instead for <a id="incorrectQuery" href="javascript:resubmitForm()" value="<?php echo $query?>"><?php echo $query?></a><br>
	<?php
	}?>
    <?php 

		if($flag == 2){
	?>
	Did you mean : <a id="query" href="javascript:submitForm()" value="<?php echo $correct?>"><I><?php echo $correct?><I></a><br>
	<?php
	}
	?>   
    <?php
        // display results
        if($results) 
        {
        $total = (int) $results->response->numFound;
        $start = min(1, $total);
        $end = min(10, $total);
    ?>
    <div>Total Number of Documents Found = <?php echo $total; ?></div>
    <ol> 
        <?php
            // iterate result documents
            $p = 0;
            $titleArray = array();
            foreach ($results->response->docs as $doc)
            { 
            if($p>9){break;}else{
            $getTitle = $doc->title;
           if(!in_array($getTitle,$titleArray)){
                $p++;
                $field = $doc->id;
                $author = $doc->author;
                $dateCreated = $doc->date;
                $size = $doc->stream_size;
                $size = (($size)/1000) . "KB";
            foreach($newArray as $key => $value){
                if($field == $key){
                    $urlAddress = $value;
                }
            }
            if($urlAddress == null){ continue;} 
            else{
            if($getTitle == ""){$getTitle="N.A";}
            if($author == ""){$author="N.A";}
            if($dateCreated == ""){$dateCreated="N.A";}
            if($size == ""){$size="N.A";}
            
        ?>
        <li style="border:1px solid black;">		  	
	<table style="text-align:left">
        <tr>
        <th><a href="<?php echo $urlAddress; ?>" target="_blank">Document <?php echo $p;?></a></th>
        <td><?php echo htmlspecialchars($getTitle,ENT_NOQUOTES,'utf-8');?></td>
        </tr>
        <tr>
        <td>Author : <?php echo htmlspecialchars($author,ENT_NOQUOTES,'utf-8');?></td>    
        <td>Date Created : <?php echo htmlspecialchars($dateCreated,ENT_NOQUOTES,'utf-8');?></td>    
        <td>Size : <?php echo htmlspecialchars($size,ENT_NOQUOTES,'utf-8');?></td>    
        </tr>
    </table> 
        </li>
            <?php 
                array_push($titleArray,$getTitle);}}
            else{ continue; }
            }}
            ?> 
    </ol>
    <?php 
        }
    ?>
    </body> 
</html>
            
