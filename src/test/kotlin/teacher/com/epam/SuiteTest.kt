package teacher.com.epam

import org.junit.runner.RunWith
import org.junit.runners.Suite
import teacher.com.epam.task1.Task1Test
import teacher.com.epam.task2.Task2Test


@RunWith(Suite::class)
@Suite.SuiteClasses(Task1Test::class, Task2Test::class)
internal class SuiteTest