<?php 
include 'SpellCorrector.php';
include 'PorterStemmer.php';
$stopwords = array("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the");

$param = $_GET['query'];
$mWords = array();
$finalTerms = array();
$compare = array();
$mWords = explode(" ",$param);
$length = count($mWords);
$term = end($mWords);
foreach($mWords as $word){
    if(strcmp($word,$term)==0)break;
    else{
        $final = $final . $word . " ";
    }
}
$newData = null;
$correctedWord = "";
$terms = null;
$data = file_get_contents("http://localhost:8983/solr/myexample/suggest?q=".$term."&wt=json&indent=true");
$list = json_decode($data);
$numberFound = $list->suggest->suggest->$term->numFound;
if($numberFound == 0){
    $correctedWord = SpellCorrector::correct($term);
    $newData = file_get_contents("http://localhost:8983/solr/myexample/suggest?q=".$correctedWord."&wt=json&indent=true");
}
if($newData != null){
    $newList = json_decode($newData);
    $num = $newList->suggest->suggest->$correctedWord->numFound;
    if($num != 0){
        $terms = $newList->suggest->suggest->$correctedWord;
    }
}else{
$terms = $list->suggest->suggest->$term;
}
$words = $terms->suggestions;
$hashMap = array();
$i=0;
$newCompare = array();
$compare = array();
$i = 0;
foreach($words as $word){
    if($i<5){
    $text = $word->term;
    $stemWord = PorterStemmer::Stem($text);
    if($length == 1 && in_array($stemWord,$stopwords)){
        continue;
    }
    $checkIfPresent = $compare[$stemWord];
    if($checkIfPresent == null){
        $i = $i + 1;
        $compare[$stemWord] = $text;
        $weight = $word->weight;
        $newCompare[$stemWord]= $weight;
    }
 }else{
        break;
    }
    
}
arsort($newCompare);
foreach($newCompare as $key => $value){
    $newWord = $final . $key;
    array_push($finalTerms,$newWord);
}
echo json_encode($finalTerms);
?>
