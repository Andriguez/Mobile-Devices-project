import androidx.test.core.app.ActivityScenario
import org.junit.runner.RunWith
import org.junit.Test

@RunWith(AndroidJUnit4)
class LandingActivityTest {

    @Test
    fun landingActivity_launches() {
        ActivityScenario.launch(LandingActivity::class.java)
    }
}
