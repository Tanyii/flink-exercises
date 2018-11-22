package filnk_exercises

import java.net.{InetAddress, InetSocketAddress}

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction
import org.apache.flink.streaming.connectors.elasticsearch5.ElasticsearchSink
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

object Simulator_ES_Sink {


  def main(args: Array[String]) : Unit = {

    //val env = StreamExecutionEnvironment.createRemoteEnvironment("10.10.*",6123,"*.jar")
    //val env = ExecutionEnvironment.createRemoteEnvironment("10.10.*",8081,"*.jar")
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val text = env.readTextFile("/var/log/messages")

    val windowCounts = text.flatMap(_.split("\\W+")).filter(_.nonEmpty)

    val config =new java.util.HashMap[String,String]

    config.put("cluster.name","my_es_cluster")

    val transportAddresses =new java.util.ArrayList[InetSocketAddress]
    transportAddresses.add(new InetSocketAddress(InetAddress.getByName("10.10.*"),9300))

    windowCounts.addSink(new ElasticsearchSink(config,transportAddresses,new ElasticsearchSinkFunction[String] {

      def createIndexRequest(element:String): IndexRequest = {
        val json =new java.util.HashMap[String,String]
        json.put("data",element)
        Requests.indexRequest().index("flink-index").`type`("test").source(json)
      }

      import org.apache.flink.api.common.functions.RuntimeContext
      import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer

      def process(element:String,ctx: RuntimeContext,indexer: RequestIndexer):Unit= {
        indexer.add(createIndexRequest(element))
      }

    }))

    env.execute("batch Socket Window test")
  }}
