/// Class to control traffic light
/// Detail: implements state pattern to switch signals
/// States:
/// Stop     - traffic has to stop, red signal is indicated
/// Go       - traffic has to go, green signal is indicated
/// GoToStop - notify traffic about signal change Go to Stop indicating yellow signal
/// StopToGo - notify traffic about signal change Stop to Go indicating yellow signal
class TrafficLight(val id: Int, val initState: String) {
  var state: State = Attention(this)
  val stop: State = Stop(this)
  val stopToGo: State = StopToGo(this)
  val goToStop: State = GoToStop(this)
  val go: State = Go(this)

  // initialize first state
  init {
    reset()
  }

  // switch to next state
  fun next() {
      state.next()
  }

  // signal state (hardware control)
  fun signal() {
    state.signal()
  }

  // switch to attention state
  fun attentionOn() {
    state = Attention(this)
  }

  // switch to initial state for normal operation
  fun attentionOff() {
    reset()
  }

  // reset to initial state
  fun reset() {
    when(initState) {
      "red" -> state = GoToStop(this)
      "green" -> state = StopToGo(this)
      else -> state = Attention(this)
    }
  }
}

/// Interface for traffic light state machine
abstract class State(val light: TrafficLight) {
  open fun next(): Unit = throw UnsupportedOperationException("not supported")
  open fun signal(): Unit = throw UnsupportedOperationException("not supported")
}

/// Class Stop state
class Stop(light: TrafficLight) : State(light) {
  override fun next() {
      light.state = light.stopToGo;
  }

  override fun signal() {
    println("light ${light.id} is Red")
  }
}

/// Class StopToGo state
class StopToGo(light: TrafficLight) : State(light) {
  override fun next() {
      light.state = light.go;
  }

  override fun signal() {
    println("light ${light.id} is Yellow")
  }
}

/// Class GoToStop state
class GoToStop(light: TrafficLight) : State(light) {
  override fun next() {
    light.state = light.stop;
  }

  override fun signal() {
    println("light ${light.id} is Yellow")
  }
}

/// Class Go state
class Go(light: TrafficLight) : State(light) {
  override fun next() {
    light.state = light.goToStop;
  }

  override fun signal() {
    println("light ${light.id} is Green")
  }
}

/// Class Attention state
/// Note: This state does switch off normal operation (hence no next override)
class Attention(light: TrafficLight) : State(light) {
  override fun signal() {
    println("light ${light.id} is Yellow blinking")
  }
}

/// Class to control traffic lights
/// Description: This class controls traffic lights by switching signals and indicating Signals and Attention situation
/// The lane control is achieved by complementary initialization of the traffic light states
/// TODO: This could be an interface which is specialized for +-crossings, T-crossings, Predestrian-crossings
class TrafficCtrl {
  // initialize for +-crossing where id 1,2 are assigned lane1 and id 3,4 are assigned lane2
  val lights: Array<TrafficLight> = arrayOf(TrafficLight(1, "red"), TrafficLight(2, "red"), TrafficLight(3, "green"), TrafficLight(4, "green"))

  // switch all lights to next signal state
  fun switch() {
    for (light in lights) light.next()
  }

  // indicate current signal
  fun signal() {
    for (light in lights) light.signal()
  }

  // turn on attention mode (normal operation is switch off)
  fun attentionOn() {
    for (light in lights) light.attentionOn()
  }

  // turn off attention mode (going back to normal operation)
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