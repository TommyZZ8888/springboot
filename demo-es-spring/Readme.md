es7.*版本必须restHighLevelClient； es8版本最低适配jdk11，最好17以上

推荐使用es client方式，不推荐spring方式

（Spring：高度封装，用着舒服。缺点是更新不及时，有可能无法使用 ES 的新 API
ES 官方：更新及时，灵活，缺点是太灵活了，基本是一比一复制 REST APIs，项目中使用需要二次封装。）