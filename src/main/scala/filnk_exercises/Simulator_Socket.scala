package filnk_exercises

import java.io.ObjectOutputStream
import java.net.ServerSocket

object Simulator_Socket extends App{
  val listener: ServerSocket = new ServerSocket(9999)
  val socket = listener.accept()
  while (true){
    //val inputStream = new ObjectInputStream(socket.getInputStream)
    val outputStream = new ObjectOutputStream(socket.getOutputStream)
    //val msg = inputStream.readObject()
    val r = new util.Random()
    val msg = new StringBuffer()
    msg.append("::goodsID-" + r.nextInt(20))
    msg.append("::")
    msg.append(r.nextInt(6))
    msg.append("::")
    msg.append(r.nextInt(10)+r.nextFloat())
    msg.append("::")
    msg.append(r.nextInt(2))
    msg.append("::")
    msg.append(r.nextInt(2))
    msg.append("\n")
    outputStream.writeObject(msg.toString)
    Thread.sleep(500)
    outputStream.flush()
  }

}
