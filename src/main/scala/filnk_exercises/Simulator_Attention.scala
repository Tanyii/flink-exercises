package filnk_exercises

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._


object Simulator_Attention {

  def main(args: Array[String]): Unit = {
    //Obtain an execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //create the initial data from socket
    //please run Simular_Socket object
    var text  = env.socketTextStream("127.0.0.1",9999)
    //transformations
    var Counts = text.map(x=>{
      val result = x.split("::").filter(!_.contains("t")).toList
      (result(0),(result(1).toInt+result(2).toFloat)*result(4).toInt)
    }).keyBy(0).sum(1)

    //print transformations result
    Counts.print()
    //Trigger the program execution
    env.execute("Simulator")
  }
}
