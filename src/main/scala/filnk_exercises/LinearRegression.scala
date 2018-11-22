package filnk_exercises

import org.apache.flink.api.scala._
import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.ml.regression.MultipleLinearRegression

object LinearRegression extends App {
  val mlr = MultipleLinearRegression()
  val env = ExecutionEnvironment.getExecutionEnvironment

  val train : DataSet[LabeledVector]= env.fromElements(LabeledVector(1,DenseVector(2)),LabeledVector(2,DenseVector(4)),LabeledVector(3,DenseVector(6)))
  val test : DataSet[DenseVector]= env.fromElements(DenseVector(12),DenseVector(14))

  val model =  mlr.fit(train)

  val result = mlr.predict(test)

  result.map(x => x._2.formatted("%.2f")).print()

}
