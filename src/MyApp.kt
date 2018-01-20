import javafx.collections.ObservableList
import javafx.geometry.Orientation
import tornadofx.*
import kotlin.reflect.jvm.internal.impl.javax.inject.Inject

class MyApp : App(MyView::class)


class MyView : View(){
    
    val copyd : CopyController by inject()
    val copydat: ObservableList<CopyData>
        get() = copyd.all()

    override val root = splitpane{
        orientation = Orientation.HORIZONTAL

        tableview(copydat)
        {
            column("data ", CopyData::dat)
            smartResize()
        }
    }
}

data class CopyData(val dat: String)

class CopyController : Controller(){
    fun all(): ObservableList<CopyData> {
        val observable = listOf(CopyData("Sure thing bud"), CopyData("two"), CopyData("three")).observable()
        return observable
    }
}
