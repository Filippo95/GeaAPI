# GeaAPI ![Build jar, create and push DockerImage](https://github.com/Filippo95/GeaAPI/workflows/Build%20jar,%20create%20and%20push%20DockerImage/badge.svg)
Small backend for some Elasticsearch index, using sping-boot \
Gea Project is a IoT Project that allows you to collect data from temperature and humidity sensors, and Sonoff device using MQTT.\
As IoT gateway it uses HomeAssistant, to show locally some datas and it allows you to integrate with many other addons. \
Home Assistant, runs a Mosquitto MQTT broker with QoS 0 and than a logstash instance with this configuration:
```bash
input {
  mqtt {
    host => "mqttbrokerhost_ip"
    port => mqqtbroker_port
    topic => "#"
    qos => 0
    username => "mqqt_username"
    password => "mqtt_user_password"
  }

}
filter {
  mutate {
        id => "mutate_filter"
        # copy only important fields
        copy => {
          "[topic]" => "name"
          "[timestamp]" => "sensor_timestamp"
          "[message]" => "value"
        }

        # remove unused fields
        remove_field => ["host","topic","message","timestamp"]
       }
  date {
        id => "date_filter"
        match => [ "timestamp", "ISO8601" ]
        target => "timestamp"
       }
  grok{
        match=>{"name"=>"%{GREEDYDATA:pre}%{MAC:id_home}/%{MAC:id_sensor}/%{WORD:metric}"}
      }

  if [metric]=="POWER" and [pre]=="stat/" and [value]=="ON"{
     mutate{
           add_field=>{"[status]"=>"1"}
     }
  }
  if [metric]=="POWER" and [pre]=="stat/" and [value]=="OFF"{
     mutate{
           add_field=>{"[status]"=>"0"}
     }
  }
}

output {
  stdout {
        id => "stdout_output"
  }

if [metric] == "temperature"{
  elasticsearch {
          hosts => ["elastic_host:elastic_port"]
          index => "temperature"
        }
}
if [metric] == "humidity"{
  elasticsearch {
          hosts => ["elastic_host:elastic_port"]
          index => "humidity"
        }
}
if [metric] == "POWER" and [pre]=="stat/"{
  elasticsearch {
          hosts => ["elastic_host:elastic_port"]
          index => "switch"
        }
}

}
```
In that way you can save every single mqtt message into elasticsearch indexes. \
This project, allows you to query the elastisearch idexes, and provide a ReST API. 
## Dependencies
This project is developed using Spring Boot Framwork, and Maven as a software project management and comprehension tool. \
The pom.xml file specifies many dependency as: 
* Spring boot
* elasticsearch-rest-high-level-client
* org.cyclonedx


## Architecture
This application it's a simple ReST API, that allows users to show data, than update, create, and delete operations are not supported. 

### Model
Models are simple java classes, that implements many method, such as getters, setters, toString, and the costructor

### Controller
Controllers class are just simple spring-boot controllers that maps urls and get parameters, those parameters are needed to use service classes. 
Controller just filter request selecting if parameter are correect and then pass them to the service class. 

### Service
Services class use elasticsearch client library to comunicate with the elasticsearch server and then performing queries. 

## Tests
under the folder test/java/API/ there are 3 folters: controller, model and service, those folters contains a test class for each entity. 
Tests are developed as simple spring boot tests, for controller test, are realized using Mockito to emulate services classes\
For service test mockito does not support mocking class defined as final in library elastisearch-hight-level-client, than service test check only if correct execptions are thorwn

## ReST Interface
This API provide many endpoint for each entity such as:
### Humidity
#### Lasts
```
host_ip:port/humidity/[HOME_ASSISTANT_MAC_address]/lasts
```
This endpoint takes one mac, the mac address of the home assistant instance, and in returns a json with the last misurations of each sensor, for example:
```
[
  {
    "id": "TRYPeHQBpzGAIAYg9Qtt",
    "home_mac": "HOME_ASSISTANT_MAC",
    "sensor_mac": "SENSOR1_MAC",
    "timestamp": "2020-11-11T12:49:05.830+00:00",
    "value": 47.56
  },
  {
    "id": "yRYUeHQBpzGAIAYgpwr6",
    "home_mac": "HOME_ASSISTANT_MAC",
    "sensor_mac": "SENSOR2_MAC",
    "timestamp": "2020-11-11T12:54:13.650+00:00",
    "value": 49.38
  },
  {
    "id": "Dxb7d3QBpzGAIAYgDgg6",
    "home_mac": "HOME_ASSISTANT_MAC",
    "sensor_mac": "SENSOR3_MAC",
    "timestamp": "2020-11-11T12:26:15.890+00:00",
    "value": 61.02
  }
]
```
#### Search
```
host_ip:port/humidity/[HOME_ASSISTANT_MAC_address]/search
```
This endpoint takes one mac, the mac address of the home assistant instance, and in returns a json with last 10000 records

#### Average
```
host_ip:port/humidity/[HOME_ASSISTANT_MAC_address]/[MAC_address_Sensor]/average/[timestamp_start]/[timestamp_stop]
```
This endpoint takes two mac, the mac address of the home assistant instance, and the mac address of the sensor, and it returns the average of metrics during the period between timestamp_start and timestamp_stop


### Temperature
#### Lasts
```
host_ip:port/temperature/[HOME_ASSISTANT_MAC_address]/lasts
```
This endpoint takes one mac, the mac address of the home assistant instance, and in returns a json with the last misurations of each sensor, for example:
```
[
  {
    "id": "TRYPeHQBpzGAIAYg9Qtt",
    "home_mac": "HOME_ASSISTANT_MAC",
    "sensor_mac": "SENSOR1_MAC",
    "timestamp": "2020-11-11T12:49:05.830+00:00",
    "value":  3.14
  },
  {
    "id": "yRYUeHQBpzGAIAYgpwr6",
    "home_mac": "HOME_ASSISTANT_MAC",
    "sensor_mac": "SENSOR2_MAC",
    "timestamp": "2020-11-11T12:54:13.650+00:00",
    "value": 19.94
  },
  {
    "id": "Dxb7d3QBpzGAIAYgDgg6",
    "home_mac": "HOME_ASSISTANT_MAC",
    "sensor_mac": "SENSOR3_MAC",
    "timestamp": "2020-11-11T12:26:15.890+00:00",
    "value": 23.1
  }
]
```
#### Search
```
host_ip:port/temperature/[HOME_ASSISTANT_MAC_address]/search
```
This endpoint takes one mac, the mac address of the home assistant instance, and in returns a json with last 10000 records

#### Average
```
host_ip:port/temperature/[HOME_ASSISTANT_MAC_address]/[MAC_address_Sensor]/average/[timestamp_start]/[timestamp_stop]
```
This endpoint takes two mac, the mac address of the home assistant instance, and the mac address of the sensor, and it returns the average of metrics during the period between timestamp_start and timestamp_stop


### Switch 
This metric is about a little device that allows you to turn on or off some lights, so the API allows you to know many information about changes of light's state
#### getGraphData
```
host_ip:port/switch/[HOME_ASSISTANT_MAC_address]/[MAC_address_Sensor]/getGraphData/[timestamp_start]/[timestamp_stop]
```
This endpoint returns a Json Array that show how the state ( 0/off 1/on) have been changed, and simplify a graphical rappresentation \
example of returned Json
```
[
  {
    "timestamp": "2020-09-10T13:01:12.009+00:00",
    "value": 0
  },
  {
    "timestamp": "2020-09-10T13:01:49.737+00:00",
    "value": 1
  },
  {
    "timestamp": "2020-09-10T13:23:42.598+00:00",
    "value": 0
  },
  {
    "timestamp": "2020-09-10T14:20:54.116+00:00",
    "value": 0
  },
  {
    "timestamp": "2020-09-10T14:21:21.589+00:00",
    "value": 0
  },
  {
    "timestamp": "2020-09-10T13:25:12.831+00:00",
    "value": 1
  },
  {
    "timestamp": "2020-09-10T14:21:11.592+00:00",
    "value": 1
  },
  {
    "timestamp": "2020-09-10T14:23:12.964+00:00",
    "value": 1
  },
  {
    "timestamp": "2020-09-10T14:23:24.085+00:00",
    "value": 0
  },
  {
    "timestamp": "2020-09-10T14:26:27.091+00:00",
    "value": 0
  },
  {
    "timestamp": "2020-09-10T14:24:13.093+00:00",
    "value": 1
  }
]
```
#### Lights
```
host_ip:port/switch/[HOME_ASSISTANT_MAC_address]/getLights 
```
This endpoint return a Json Array that show all light (sonoff device) into a certain Home Assistant. 

#### getHours
```
host_ip:port/switch/[HOME_ASSISTANT_MAC_address]/[MAC_address_Sensor]/getHours/[timestamp_start]/[timestamp_stop]
```
This endpoint return a value, the number of hours that the sensor have been on, between timestamp_start and timestamp_stop

#### getAverageONHours
```
host_ip:port/switch/[HOME_ASSISTANT_MAC_address]/[MAC_address_Sensor]/getHours/[timestamp_start]/[timestamp_stop]
```
This endpoint return a value, the average of how many hours the sensor have been on in each day 



