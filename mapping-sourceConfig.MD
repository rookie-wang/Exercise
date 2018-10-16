## 1. 输入
#### 1.1 抽象：
   1. 配置驱动，由ETL定时读取配置文件中的配置源，和BDGID（通过哪个mapping转化为哪个实体），拉取数据转换为json
   加上BDGID，生成对应实体的实例
   2. 开放接口，由外部传入json数据+BDGID给ETL的接口，走后面相同的流程
   3. 对于源数据转换成的JSON中的基础标签，直接json+配置的多个mapperId转为实体实例
   4. 对于其中音视频图片文本，通过分析变为目标json+配置的多个mapperId转为实体实例
   5. 数据源拉取/api打入的SourceJson抽象格式为：
   
    ```
    {
        "baseLabel1":"",
        "baseLabelN":"",
        "pic":".png",
        "video":".mp4",
        "mp3":".mp3"，
        "text":"<html></html>"
    }
    
    ```
  
    
#### 1.2 具象 
#### 1.2.1 对于MRP数据
    {
    	"depthfromSeed": 0,
    	"count": 0,
    	"sourceRegion": "",
    	"tag": "丹寨县",
    	"crawlTime": 1534508777000,
    	"type": "",
    	"startTime": "",
    	"statusCode": 200,
    	"author": "潘皇权　",
    	"title": "【脱贫行动】丹寨县国春银饰定期分红助农增收",
    	"sourceName": "",
    	"tid": "Qou3ZXlsXnM2QSjjLuEkv08080pmcmnn",
    	"tags": {
    		"empty": false,
    		"traversableAgain": true
    	},
    	"text": "..",
    	"publishTime": "2018-06-27 00:00:00",
    	"sourceTypeId": "DflJUx4T5o-1481684743598-4LQn8BLEw2",
    	"mediaName": "多彩贵州网",
    	"url": "http://qdn.gog.cn/system/2018/06/27/016661382.shtml",
    	"parsedData": "...",
    	"fetched": true,
    	"pass": 0,
    	"imgUrl": "http://cloudpioneer-test.oss-cn-shanghai.aliyuncs.com/Qou3ZXlsXnM2QSjjLuEkv08080pmcmnn/qdngogcnpic00303072400303072414_5a3cb3e9jpg.jpg",
    	"rootUrl": "http://qdn.gog.cn/danz/index.shtml",
    	"textPTag": "...",
    	"fromUrl": "http://qdn.gog.cn/danz/index.shtml",
    	"html": "",
    	"categories": {
    		"empty": false,
    		"traversableAgain": true
    	},
    	"docId": "Qou3ZXlsXnM2QSjjLuEkv08080pmcmnn|1534508483560|be3ad"
    }
    其中，text,imgUrl需要额外分析，其他的标签可以直接做映射

## 2. sourceConfig核心
#### 2.1 抽象
    整个程序由ETL驱动，ETL定时从配置文件中，读取配置的数据源，把数据源读取成SourceJson，
    然后把SourceJson基本标签+BDGID传给转换器生成实体实例，SourceJson音视频标签+BDGID传给分析器，
    分析完成以后，再把分析的基本标签+BDGID传给转换器生成实体实例
    
   ```
{
	"sourceConfig":{
		"type":"",//我们系统能够处理的数据源，比如HBASE，HDFS，MySQL等，一个类型对应ETL里面的一种处理逻辑
		"source":{},//某一种类型的数据源需要的配置信息，比如HDFS地址，HBASE地址，MYSQL地址等等，每种类型有自己的配置信息格式
		"sourceJson":{},//数据源转换为的目标json，这个JSON里面的key，要跟BDGID对应里面的source key相对应,这个转换关系也跟具体的数据源类型相关,
		"mapperIds":["BDGID.mapperId"]//sourceJson能够通过多个映射成的多个实体，每个id对应一个实体的一个mapper(json,pic-json等等)
		"picSourceJson":{},//图片需要解析成的目标JSON？
		"mp4SourceJson":{},//视频需要解析成的目标JSON？
		"mp3SourceJson":{},//音频需要解析成的目标JSON？
		"textSourceJson":{}//文本需要解析成的目标JSON？
	}
}  
   ```
   json+mapper=实体，json是一份数据，多个mapper生成多个实体
#### 2.2 具象
#### 2.2.1 MRP DataFrame的sourceConfig
   ```
{
	"sourceConfig":{
		"type":"hdfs",
		"source":{
			"type":"parquet",
			"address":"hdfs://90.90.90.5:8020/user/lsx/AnalysisResult/removed.parquet"
		},
		"sourceJson":["author","categories","count","crawlTime","depthfromSeed","docId","fetched","fromUrl","html","imgUrl","mediaName","parsedData","pass","publishTime","rootUrl","sourceName","sourceRegion","sourceTypeId","startTime","statusCode","tag","tags","text","textPTag","tid","title","type","url"]
		"mapperIds":["0e74bce9-b091-497f-9049-72e9eb63608e.1","0e74bce9-b091-497f-9049-72e9eb63608e.2"],
		"picSourceJson":{},
		"mp4SourceJson":{},
		"mp3SourceJson":{},
		"textSourceJson":{}
	}
} 
   ```
## 3. mapping核心
#### 3.1 抽象
    定义一个JSON如何通过一个mapper转换成为一个实体的实例
   ```
   {
   	"BDGStandard":{},//BDG标准
   	"BDGID":"",//唯一定位到此条数据的ID
   	"mappers":[
   		{
   			"mapperId":"",//定位到这个BDG标准
   			"describe":"",//关于这个mapper的描述
   			"mapper":{
       			"source":"x.y.z",//源json的key,支持嵌套
       			"target":"a.b",//目标json的key，支持嵌套
       			"type":"array/simple/objArray",//数据类型:基本,基本数组,对象数组
       			"childMapper"://当为对象数组的时候有此属性，用于把源对象数组转换为目标对象数组
       			{ 
       				"source":"a",
       				"target":"x",
       				"type":"array/simple/objArray",//如果有嵌套，支持嵌套
       				"childMapper":{}
       			}
       					
       		}
   		}
   		]
   }
   ```
    
#### 3.2 具象
##### 3.2.1 MRP DataFrame结构的mapping
   ```
   {
   	"BDGStandard":{},
   	"BDGID":"0e74bce9-b091-497f-9049-72e9eb63608e",
   	"mappers":[
                  				{
                  					"source":"title",
                  					"target":"标题/bt",
                  					"type":"simple"
                  				},
                  				{
                  					"source":"publishTime",
                  					"target":"发布时间/fbsj",
                  					"type":"simple"
                  				}   
                  				{
                  					"source":"crawlTime",
                  					"target":"发布时间/fbsj",
                  					"type":"simple"
                  				},
                  				{
                  					"source":"url",
                  					"target":"原文链接/ywlj",
                  					"type":"simple"
                  				},        				          				     				
                  				]
                  		},
                  		{
                  			"mapperId":"2",
                  			"describe":"mrp文本分析结果映射到实体"
                  			"mapper":[
                  				{
                  					"source":"contentPerson",
                  					"target":"内容人.名称/nrr.mc",
                  					"type":"simple"
                  				},
                  				{
                  					"source":"contentPersonId",
                  					"target":"内容人.ID/nrr.id",
                  					"type":"simple"
                  				}   
                  				{
                  					"source":"contentLocaltion",
                  					"target":"内容地点.名称",
                  					"type":"simple"
                  				},
                  				{
                  					"source":"contentOrigazation",
                  					"target":"内容机构.名称",
                  					"type":"simple"
                  				},        				          				     				
                  				]
   }
   ```