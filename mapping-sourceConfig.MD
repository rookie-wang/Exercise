## 1. 输入
#### 1.1 抽象：
    sourceID +源数据（json/文本/音视频，或者mysql某条数据的ID等等）+BDGID+typeId(定位到sourceConfig+mapper)
#### 1.2 具象 
#### 1.2.1 对于MRP数据
    sourceID+结构化JSON+BDGID+typeID，json里面有一部分可以直接使用的标签，另外一部分需要从文本中抽取

## 2. sourceConfig核心
#### 2.1 抽象
    - 需要定义输入数据的类型t，可以唯一定位到一种类型的源数据,比如json，图片分析的json,音视频分析的json
    - 源数据可能有多种，所以sc里面应该是一个对象数组
    - 需要定义源数据->BDG实体的mapperId，对应mapping里面的一个mapper，用于转换器转换数据
    - 根据之前的讨论结果，如果ETL放在后面做（解决IO问题），那么应该还要保存后面ETL的数据源地址（表，数据库等等）
    - 还要标识是已经ETL的JOSN，还是放在后续去ETL
    - 是否需要一个字段来描述这种数据类型
    ```
    {
          	"sourceConfig":[
          		{
          			"type":"json/text-json/pic-josn/mp4-json",//表明那种数据源
          			"describe":"数据源的描述",//描述数据源，后续易于阅读
          			"isEtl":"true/false", //是否已经ETL，是，直接mapping，否，后面根据source来获取数据，再mapping
          			"source":{},//配置数据源的基本信息，当isEtl=false的时候匹配
          			"mapperId":"" //指定的mapper
          		}
          		]
    }
    ```
#### 2.2 具象
#### 2.2.1 MRP DataFrame的sourceConfig
    {
          	"sourceConfig":[
          		{
          			"type":"json",
          			"describe":"mrp源数据，已经结构化的JSON，包含:author,crawlTime，editor等标签"
          			"isEtl":"true", 
          			"source":{},
          			"mapperId":"1"
          		},
          		{
          			"type":"text-json",
          			"describe":"mrp源数据中的text数据，经过分析提取后的json源数据，包含：内容人，机构等等标签"
          			"isEtl":"true", 
          			"source":{},
          			"mapperId":"2"
          		},
          		{
          			"type":"pic-json",
          			"describe":"mrp源数据，分析图片得到的JSON源数据，包含:内容地点，媒体等标签"
          			"isEtl":"true", 
          			"source":{},
          			"mapperId":"3"
          		},
          		{
          			"type":"mp4-json",
          			"describe":"mrp源数据，分析视频得到的JSON源数据，包含:x,y,z等标签"
          			"isEtl":"true", 
          			"source":{},
          			"mapperId":"4"
          		},          		          		
          		]
    }
    ```
## 3. mapping核心
#### 3.1 抽象
   * 对于mapping，核心就是把一个json通过规则转换为另外一个json，由于实体的来源可能有多个源json，每个json提取一部分实体
    的属性，所以，mapping是一个包含多个mapper的数组
   * 如果是基本类型的映射，直接赋值,target.x.y.z = source.a.b.c
   * 如果是基本类型的数组的映射，把数组里面的值赋给目标实体属性：target.x.y[] = source.a.b[]
   * 如果是对象类型的数组的映射，得先把数组里面的对象转为目标实体的对象，再把新的数组赋给目标实体的属性
   
    ```
    {
        	"mapping":[
        		{
        			"mapperId":"",//标识一个具体的mapper，跟sourceConfig里面定义相同
        			"describe":"",//mapper描述，便于阅读
        			"mapper":[
        				{
        					"source":"x.y.z",//源json的key,支持嵌套
        					"target":"a.b",//目标json的key，支持嵌套
        					"type":"array/simple/objArray",//数据类型:基本,基本数组,对象数组
        					"childMapper":{ //当为对象数组的时候有此属性，用于把源对象数组转换为目标对象数组
        						"source":"a",
        						"target":"x",
        						"type":"array/simple/objArray",//如果有嵌套，支持嵌套
        						"childMapper":{}
        					}
        					
        				}
        				]
        		}]
    }
    ```
    
#### 3.2 具象
##### 3.2.1 MRP DataFrame结构的mapping
    ```
    {
        	"mapping":[
        		{
        			"mapperId":"1",
        			"describe":"mrp源数据映射到实体"
        			"mapper":[
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
        		]
    }
    ```