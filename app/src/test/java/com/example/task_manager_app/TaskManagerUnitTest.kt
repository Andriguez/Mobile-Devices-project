import androidx.test.core.app.ActivityScenario
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4)
class LandingActivityTest {

    @Test
    fun landingActivity_launches() {
        ActivityScenario.launch(LandingActivity::class.java)
    }
}
