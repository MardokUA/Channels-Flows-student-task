@file:Suppress("TestFunctionName")

package teacher.com.epam.functions.task1

import com.epam.functions.task1.*
import com.epam.functions.task1.data.*
import com.epam.functions.task1.factory.ChosenBody
import com.epam.functions.task1.factory.ChosenEquipment
import com.epam.functions.task1.utils.*
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class WorkShopTest {

    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @Before
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testActorBodyLine() {
        runBlocking {
            //given
            val line = BodyLine1
            val prepareBodyRequestMockk = mockk<PrepareBodyRequest>(relaxed = true)
            val bodyPartsChannelMockk = mockk<SendChannel<BodyParts>>(relaxed = true)
            every { prepareBodyRequestMockk.bodyPartsChannel } returns bodyPartsChannelMockk
            //when
            val bodyLine: SendChannel<PrepareBodyRequest> = createBodyLine(line)
            bodyLine.send(prepareBodyRequestMockk)
            //then
            Assert.assertThat( "actor in createBodyLine doesn't return correct log, please check your implementation",
                outputStreamCaptor.toString().trim(), CoreMatchers.containsString(line))
            bodyLine.close()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testActorEquipmentLine() {
        runBlocking {
            //given
            val line = EquipmentLine1
            val prepareEquipmentRequestMockk = mockk<PrepareEquipmentRequest>(relaxed = true)
            val equipmentPartsChannelMockk = mockk<SendChannel<EquipmentParts>>(relaxed = true)
            every { prepareEquipmentRequestMockk.equipmentChannel } returns equipmentPartsChannelMockk
            //when
            val equipmentLine: SendChannel<PrepareEquipmentRequest> = createEquipmentLine(line)
            equipmentLine.send(prepareEquipmentRequestMockk)
            //then
            Assert.assertThat( "actor in createEquipmentLine doesn't return correct log, please check your implementation",
                outputStreamCaptor.toString().trim(), CoreMatchers.containsString(line))
            equipmentLine.close()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSelectOnCombineBodyWithLineTwo() {
        runBlocking {
            //given
            val combineBodyLineTwo = combineBodyBodyLine2
            val bodyLine1Name = BodyLine1
            val bodyLine2Name = BodyLine2
            val chosenBody = mockk<ChosenBody>()
            val bodyLine1: SendChannel<PrepareBodyRequest> = createBodyLine(bodyLine1Name)
            val bodyLine2: SendChannel<PrepareBodyRequest> = createBodyLine(bodyLine2Name)
            val prepareBodyRequestMockk = mockk<PrepareBodyRequest>(relaxed = true)

            //when
            bodyLine1.send(prepareBodyRequestMockk)
            combineBody(chosenBody, bodyLine1, bodyLine2)

            //then
            Assert.assertThat("select in combineBody has to work with $combineBodyBodyLine2 but in your implementation it doesn't, and it is sadly ",
                outputStreamCaptor.toString().trim(), CoreMatchers.containsString(combineBodyLineTwo))
            bodyLine1.close()
            bodyLine2.close()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSelectOnCombineBodyWithLineOne() {
        runBlocking {
            //given
            val combineBodyLineOne = combineBodyBodyLine1
            val bodyLine1Name = BodyLine1
            val bodyLine2Name = BodyLine2
            val chosenBody = mockk<ChosenBody>()
            val bodyLine1: SendChannel<PrepareBodyRequest> = createBodyLine(bodyLine1Name)
            val bodyLine2: SendChannel<PrepareBodyRequest> = createBodyLine(bodyLine2Name)

            //when
            combineBody(chosenBody, bodyLine1, bodyLine2)

            //then
            Assert.assertThat("select in combineBody has to work with $combineBodyBodyLine1 but in your implementation it doesn't, and it is sadly ",
                outputStreamCaptor.toString().trim(), CoreMatchers.containsString(combineBodyLineOne))
            bodyLine1.close()
            bodyLine2.close()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSelectOnCombineEquipmentWithLineTwo() {
        runBlocking {
            //given
            val combineEquipmentEquipmentLineTwo = combineEquipmentEquipmentLine2
            val equipmentLine1Name = EquipmentLine1
            val equipmentLine2Name = EquipmentLine2
            val chosenEquipment = mockk<ChosenEquipment>()
            val equipmentLine1: SendChannel<PrepareEquipmentRequest> = createEquipmentLine(equipmentLine1Name)
            val equipmentLine2: SendChannel<PrepareEquipmentRequest> = createEquipmentLine(equipmentLine2Name)
            val prepareBodyRequestMockk = mockk<PrepareEquipmentRequest>(relaxed = true)

            //when
            equipmentLine1.send(prepareBodyRequestMockk)
            combineEquipment(chosenEquipment, equipmentLine1, equipmentLine2)

            //then
            Assert.assertThat("select in combineEquipment has to work with $combineEquipmentEquipmentLine2 but in your implementation it doesn't, and it is sadly ",
                outputStreamCaptor.toString().trim(),
                CoreMatchers.containsString(combineEquipmentEquipmentLineTwo)
            )
            equipmentLine1.close()
            equipmentLine2.close()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSelectOnCombineEquipmentWithLineOne() {
        runBlocking {
            //given
            val combineEquipmentEquipmentLineOne = combineEquipmentEquipmentLine1
            val equipmentLine1Name = EquipmentLine1
            val equipmentLine2Name = EquipmentLine2
            val chosenEquipment = mockk<ChosenEquipment>()
            val equipmentLine1: SendChannel<PrepareEquipmentRequest> = createEquipmentLine(equipmentLine1Name)
            val equipmentLine2: SendChannel<PrepareEquipmentRequest> = createEquipmentLine(equipmentLine2Name)

            //when
            combineEquipment(chosenEquipment, equipmentLine1, equipmentLine2)

            //then
            Assert.assertThat("select in combineEquipment has to work with $combineEquipmentEquipmentLine1 but in your implementation it doesn't, and it is sadly ",
                outputStreamCaptor.toString().trim(),
                CoreMatchers.containsString(combineEquipmentEquipmentLineOne)
            )
            equipmentLine1.close()
            equipmentLine2.close()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testShotDown() {
        runBlocking {
            //given
            val equipmentLine1Name = EquipmentLine1
            val equipmentLine2Name = EquipmentLine2
            val bodyLine1Name = BodyLine1
            val bodyLine2Name = BodyLine2
            val equipmentLine1: SendChannel<PrepareEquipmentRequest> = createEquipmentLine(equipmentLine1Name)
            val equipmentLine2: SendChannel<PrepareEquipmentRequest> = createEquipmentLine(equipmentLine2Name)
            val bodyLine1: SendChannel<PrepareBodyRequest> = createBodyLine(bodyLine1Name)
            val bodyLine2: SendChannel<PrepareBodyRequest> = createBodyLine(bodyLine2Name)

            //when
            val isShutDown = shutdown(bodyLine1, bodyLine2, equipmentLine1, equipmentLine2)
            //then
            Assert.assertEquals("one, all or non channels were not closed ",true, isShutDown)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testIsThatOrderedIsProduced() {
        runBlocking {
            val orders = listOf(
                Car(Part.Body.Sedan, Part.Equipment.Premium),
                Car(Part.Body.SportCar, Part.Equipment.Family),
                Car(Part.Body.Van, Part.Equipment.LowCost),
            )
            startWorkShopWork(orders)
            Assert.assertThat( "result output doesn't contain first ordered element",
                outputStreamCaptor.toString().trim(),
                CoreMatchers.containsString(orders[0].toString())
            )
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testIsConstructorsTeamsWorkInParallel() {
        runBlocking {
            val orders = listOf(
                Car(Part.Body.Sedan, Part.Equipment.Premium),
                Car(Part.Body.SportCar, Part.Equipment.Family),
                Car(Part.Body.Sedan, Part.Equipment.LowCost),
                Car(Part.Body.Van, Part.Equipment.Premium),
                Car(Part.Body.Sedan, Part.Equipment.LowCost),
                Car(Part.Body.Van, Part.Equipment.LowCost),
                Car(Part.Body.Van, Part.Equipment.LowCost)
            )
            startWorkShopWork(orders)
            Assert.assertThat("Constructors team 1 doesn't take part in the process ",
                outputStreamCaptor.toString().trim(),
                CoreMatchers.containsString(ProvidedByConstructorTeam1)
            )
            Assert.assertThat("Constructors team 2 doesn't take part in the process ",
                outputStreamCaptor.toString().trim(),
                CoreMatchers.containsString(ProvidedByConstructorTeam2)
            )
        }
    }

    @After
    fun tearDown() {
        System.setOut(standardOut)
    }
}
