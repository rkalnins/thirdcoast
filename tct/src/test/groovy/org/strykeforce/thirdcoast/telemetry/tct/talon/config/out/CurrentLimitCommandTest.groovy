package org.strykeforce.thirdcoast.telemetry.tct.talon.config.out

import org.strykeforce.thirdcoast.telemetry.tct.Command
import org.strykeforce.thirdcoast.telemetry.tct.talon.config.AbstractDoubleConfigCommand
import org.strykeforce.thirdcoast.telemetry.tct.talon.config.AbstractTalonConfigCommandTest
import spock.lang.Ignore

class CurrentLimitCommandTest extends AbstractTalonConfigCommandTest {

    Command command

    @Override
    void setup() {
        command = new CurrentLimitCommand(reader, talonSet)
    }

    def "invalid or no input"() {
        when:
        command.perform()

        then:
        3 * reader.readLine(_) >>> ["ABC", "27.67", ""]
        0 * talon._
    }

    def "enables a current limit"() {
        when:
        command.perform()

        then:
        2 * reader.readLine(_) >>> ["ABC", "2767"]

        1 * writer.println("please enter an integer") // ABC

        1 * talon.configContinuousCurrentLimit(2767, AbstractDoubleConfigCommand.TIMEOUT_MS) // 2767
        1 * talon.enableCurrentLimit(true)
        1 * talon.getDescription()
        0 * talon._
    }

    def "disables current limit"() {
        when:
        command.perform()

        then:
        1 * reader.readLine(_) >> "0"

        1 * talon.enableCurrentLimit(false)
        1 * talon.getDescription()
        0 * talon._
    }
}
