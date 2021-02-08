package teacher.com.epam.functions

import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(TestOne::class, TestTwo::class)
internal class SuiteTest