class TrafficLight(val id: Int, val initState: String) {
  var state: State = Attention(this)
  val stop: State = Stop(this)
  val stopToGo: State = StopToGo(this)
  val goToStop: State = GoToStop(this)
  val go: State = Go(this)

  init {
    reset()
  }

  fun next() {
      state.next()
  }

  fun signal() {
    state.signal()
  }

  fun attentionOn() {
    state = Attention(this)
  }

  fun attentionOff() {
    reset()
  }

  fun reset() {
    when(initState) {
      "red" -> state = GoToStop(this)
      "green" -> state = StopToGo(this)
      else -> state = Attention(this)
    }
  }
}

abstract class State(val light: TrafficLight) {
  open fun next(): Unit = throw UnsupportedOperationException("not supported")
  open fun signal(): Unit = throw UnsupportedOperationException("not supported")
}

class Stop(light: TrafficLight) : State(light) {
  override fun next() {
      light.state = light.stopToGo;
  }

  override fun signal() {
    println("light ${light.id} is Red")
  }
}

class StopToGo(light: TrafficLight) : State(light) {
  override fun next() {
      light.state = light.go;
  }

  override fun signal() {
    println("light ${light.id} is Yellow")
  }
}

class GoToStop(light: TrafficLight) : State(light) {
  override fun next() {
    light.state = light.stop;
  }

  override fun signal() {
    println("light ${light.id} is Yellow")
  }
}

class Go(light: TrafficLight) : State(light) {
  override fun next() {
    light.state = light.goToStop;
  }

  override fun signal() {
    println("light ${light.id} is Green")
  }
}

class Attention(light: TrafficLight) : State(light) {
  override fun signal() {
    println("light ${light.id} is Yellow blinking")
  }
}

class TrafficCtrl {
  val lights: Array<TrafficLight> = arrayOf(TrafficLight(1, "red"), TrafficLight(2, "red"), TrafficLight(3, "green"), TrafficLight(4, "green"))

  fun switch() {
    for (light in lights) light.next()
  }

  fun signal() {
    for (light in lights) light.signal()
  }

  fun attentionOn() {
    for (light in lights) light.attentionOn()
  }

  fun attentionOff() {
    for (light in lights) light.attentionOff()
  }

}

fun main(args: Array<String>) {
  val ctrl  = TrafficCtrl()

  // one cycle
  ctrl.signal()
  ctrl.switch()
  ctrl.signal()
  ctrl.switch()
  ctrl.signal()
  ctrl.switch()
  ctrl.signal()

  // alert situation
  ctrl.attentionOn()
  ctrl.signal()
  ctrl.attentionOff()

  // one cycle
  ctrl.signal()
  ctrl.switch()
  ctrl.signal()
  ctrl.switch()
  ctrl.signal()
  ctrl.switch()
  ctrl.signal()
}