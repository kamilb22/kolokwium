package edu.iis.mto.oven;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    @Mock
    Fan fan;
    @Mock
    HeatingModule heatingModule;
    ProgramStage programStage1;
    ProgramStage programStage2;
    ProgramStage programStage3;
    Oven oven;

    @BeforeEach
    void setUp(){
        programStage1 = ProgramStage.builder()
                .withHeat(HeatType.HEATER)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        programStage2 = ProgramStage.builder()
                .withHeat(HeatType.GRILL)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        programStage3 = ProgramStage.builder()
                .withHeat(HeatType.THERMO_CIRCULATION)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        oven = new Oven(heatingModule,fan);
    }

    @Test
    void ifHeatTypeIsThermoCirculationFanShouldBeTurnedOn() {
        int anyTemp = 20;
        List<ProgramStage> stagesList = List.of(programStage3);
        BakingProgram bakingProgram = buildBakingProgram(anyTemp, stagesList);
        oven.start(bakingProgram);
        Mockito.verify(fan).on();
    }

    @Test
    void ifHeatTypeIsNotThermoCirculationFanShouldNotBeTurnedOn() {
        int anyTemp = 20;
        List<ProgramStage> stagesList = List.of(programStage1, programStage2);
        BakingProgram bakingProgram = buildBakingProgram(anyTemp, stagesList);
        oven.start(bakingProgram);
        Mockito.verify(fan, Mockito.never()).on();
    }

    @Test
    void fanShouldBeTurnedOffOnEveryStage() {
        int anyTemp = 20;
        List<ProgramStage> stagesList = List.of(programStage1, programStage2, programStage3);
        BakingProgram bakingProgram = buildBakingProgram(anyTemp, stagesList);
        oven.start(bakingProgram);
        Mockito.verify(fan,Mockito.times(3)).off();
    }

    @Test
    void ProgramShouldThrowOvenExceptionIfHeatModuleThrowHeatingException() {
        int anyTemp = 20;
        List<ProgramStage> stagesList = List.of(programStage3);
        BakingProgram bakingProgram = buildBakingProgram(anyTemp, stagesList);

        Assertions.assertThrows(OvenException.class, ()->{
            Mockito.doThrow(new HeatingException()).when(heatingModule).termalCircuit(Mockito.any());
            oven.start(bakingProgram);
        });

    }


    @Test
    void OvenShouldTurnOffFanIfIsThereAnyException() {
        int anyTemp = 20;
        List<ProgramStage> stagesList = List.of(programStage3);
        BakingProgram bakingProgram = buildBakingProgram(anyTemp, stagesList);

        Assertions.assertThrows(OvenException.class, ()->{
            Mockito.doThrow(new HeatingException()).when(heatingModule).termalCircuit(Mockito.any());
            oven.start(bakingProgram);
        });
        Mockito.verify(fan).off();
    }

    private BakingProgram buildBakingProgram(int anyTemp, List<ProgramStage> stagesList) {
        return BakingProgram.builder().withInitialTemp(anyTemp).withStages(stagesList).build();
    }
}
