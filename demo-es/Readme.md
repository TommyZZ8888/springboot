es7.6版本，高版本可使用
<dependency>
<groupId>co.elastic.clients</groupId>
<artifactId>elasticsearch-java</artifactId>
<version>8.12.1</version>
</dependency>

esClient功能更完善，简单易用，可参考：https://juejin.cn/post/7349863595730583587






```elasticsearch
GET _analyze
{
"analyzer": "ik_max_word",
"text": "第七人民医院"
}

DELETE /jd_data


PUT /tom
{
"settings": {
"index": {
"analysis": {
"analyzer": {
"ik_max_word_analyzer": {
"type": "custom",
"tokenizer": "ik_max_word"
}
}
}
}
},
"mappings": {
"properties": {
"id": {
"type": "keyword"
},
"title": {
"type": "text",
"analyzer": "ik_max_word_analyzer",
"search_analyzer": "ik_max_word_analyzer",
"fields": {
"keyword": {
"type": "keyword",
"ignore_above": 256
}
}
},
"name": {
"type": "text",
"analyzer": "ik_max_word_analyzer",
"search_analyzer": "ik_max_word_analyzer",
"fields": {
"keyword": {
"type": "keyword",
"ignore_above": 256
}
}
}
}
}
}


PUT tom/_doc/1
{
"id": 1,
"title": "第七人民医院",
"name": "这是一条测试数据"
}

POST tom/_doc/1
{
"title": "第七人民医院"
}

GET tom/_doc/1


GET tom/_search
{
"query": {
"match": {
"title": "七室主任"
}
}
}


PUT jd_data/jdData/1
{
"name": "hahah",
"id": 1

}```